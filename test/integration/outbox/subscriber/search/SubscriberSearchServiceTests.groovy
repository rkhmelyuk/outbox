package outbox.subscriber.search

import org.codehaus.groovy.grails.plugins.springsecurity.SecurityRequestHolder
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import outbox.member.Member
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.Subscriber
import outbox.subscriber.SubscriberService
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldStatus
import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.field.DynamicFieldValue
import outbox.subscriber.search.condition.DynamicFieldCondition
import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.SubscriptionCondition
import outbox.subscriber.search.condition.ValueCondition
import outbox.subscription.Subscription
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService
import outbox.subscription.SubscriptionStatus

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberSearchServiceTests extends GroovyTestCase {

    SubscriberService subscriberService
    DynamicFieldService dynamicFieldService
    SubscriberSearchService subscriberSearchService
    SubscriptionListService subscriptionListService

    def member

    protected void setUp() {
        super.setUp();

        member = new Member(
                firstName: 'Test',
                lastName: 'Member',
                email: 'test+member@mailsight.com',
                username: 'username',
                password: 'password')

        member.save()

        def request = new MockHttpServletRequest()
        def response = new MockHttpServletResponse()
        request.addPreferredLocale Locale.ENGLISH
        SecurityRequestHolder.set request, response
    }

    protected void tearDown() {
        member.delete()

        super.tearDown();
    }

    void testSearchBySubscribersFields() {
        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)
        def subscriber3 = createTestSubscriber(3)
        subscriber3.firstName = 'Mark'

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)
        assertTrue subscriberService.saveSubscriber(subscriber3)

        def conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('FirstName', ValueCondition.equal('John')))

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 2, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertTrue subscribers.list.contains(subscriber2)
        assertFalse subscribers.list.contains(subscriber3)
    }

    void testSearchByDynamicFields() {
        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def field = createDynamicField(1)

        assertTrue dynamicFieldService.addDynamicField(field)

        assertTrue dynamicFieldService.saveDynamicFieldValue(new DynamicFieldValue(
                subscriber: subscriber1, dynamicField: field, value: 'jack'))
        assertTrue dynamicFieldService.saveDynamicFieldValue(new DynamicFieldValue(
                subscriber: subscriber2, dynamicField: field, value: 'john'))

        def conditions = new Conditions()
        conditions.and(new DynamicFieldCondition(field, ValueCondition.notEqual('john')))

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertFalse subscribers.list.contains(subscriber2)
    }

    void testSearchBySubscriberFields_Empty() {
        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        subscriber1.lastName = ''
        subscriber2.firstName = null

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        // null
        def conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('FirstName', ValueCondition.empty()))

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber2)
        assertFalse subscribers.list.contains(subscriber1)

        // empty line
        conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('LastName', ValueCondition.empty()))

        subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertFalse subscribers.list.contains(subscriber2)
    }

    void testSearchBySubscriberFields_Filled() {
        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        subscriber1.lastName = ''
        subscriber2.firstName = null

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        // null
        def conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('FirstName', ValueCondition.filled()))

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertFalse subscribers.list.contains(subscriber2)

        // empty line
        conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('LastName', ValueCondition.filled()))

        subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber2)
        assertFalse subscribers.list.contains(subscriber1)
    }

    void testSearchBySubscribersFields_Order() {
        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)
        def subscriber3 = createTestSubscriber(3)

        subscriber1.firstName = 'John'
        subscriber2.firstName = 'Mark'
        subscriber3.firstName = 'Don'

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)
        assertTrue subscriberService.saveSubscriber(subscriber3)

        def conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('LastName', ValueCondition.equal('Doe')))
        conditions.orderBy('FirstName', Sort.Asc)

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 3, subscribers.total
        assertEquals subscriber3, subscribers.list[0]
        assertEquals subscriber1, subscribers.list[1]
        assertEquals subscriber2, subscribers.list[2]
    }

    void testSearchByMultipleDynamicFields() {
        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def field1 = createDynamicField(1)
        def field2 = createDynamicField(2)

        assertTrue dynamicFieldService.addDynamicField(field1)
        assertTrue dynamicFieldService.addDynamicField(field2)

        assertTrue dynamicFieldService.saveDynamicFieldValue(new DynamicFieldValue(
                subscriber: subscriber1, dynamicField: field1, value: 'jack'))
        assertTrue dynamicFieldService.saveDynamicFieldValue(new DynamicFieldValue(
                subscriber: subscriber2, dynamicField: field1, value: 'john'))

        assertTrue dynamicFieldService.saveDynamicFieldValue(new DynamicFieldValue(
                subscriber: subscriber1, dynamicField: field2, value: 'rabit'))
        assertTrue dynamicFieldService.saveDynamicFieldValue(new DynamicFieldValue(
                subscriber: subscriber2, dynamicField: field2, value: 'smith'))

        def conditions = new Conditions()
        conditions.and(new DynamicFieldCondition(field1, ValueCondition.notEqual('john')))
        conditions.and(new DynamicFieldCondition(field2, ValueCondition.notEqual('smith')))

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertFalse subscribers.list.contains(subscriber2)
    }

    void testSearchBySubscription() {
        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def subscriptionStatus = new SubscriptionStatus(id: 1, name: 'test').save()

        SubscriptionStatus.metaClass.static.subscribed = {
            return subscriptionStatus
        }

        def sl1 = createTestSubscriptionList(1)
        def sl2 = createTestSubscriptionList(2)
        assertTrue subscriptionListService.saveSubscriptionList(sl1)
        assertTrue subscriptionListService.saveSubscriptionList(sl2)

        def subscription11 = new Subscription(
                subscriber: subscriber1,
                subscriptionList: sl1,
                status: subscriptionStatus)

        def subscription21 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl1,
                status: subscriptionStatus)
        def subscription22 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl2,
                status: subscriptionStatus)

        assertTrue subscriptionListService.addSubscription(subscription11)
        assertTrue subscriptionListService.addSubscription(subscription21)
        assertTrue subscriptionListService.addSubscription(subscription22)

        def conditions = new Conditions()
        conditions.and(SubscriptionCondition.subscribed(sl1))
        conditions.and(SubscriptionCondition.notSubscribed(sl2))

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertFalse subscribers.list.contains(subscriber2)
    }

    void testSearchBySubscription_SubscribedOnly() {
        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def subscriptionStatus = new SubscriptionStatus(id: 1, name: 'test').save()

        SubscriptionStatus.metaClass.static.subscribed = {
            return subscriptionStatus
        }

        def sl1 = createTestSubscriptionList(1)
        def sl2 = createTestSubscriptionList(2)
        assertTrue subscriptionListService.saveSubscriptionList(sl1)
        assertTrue subscriptionListService.saveSubscriptionList(sl2)

        def subscription11 = new Subscription(
                subscriber: subscriber1,
                subscriptionList: sl1,
                status: subscriptionStatus)

        def subscription21 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl1,
                status: subscriptionStatus)
        def subscription22 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl2,
                status: subscriptionStatus)

        assertTrue subscriptionListService.addSubscription(subscription11)
        assertTrue subscriptionListService.addSubscription(subscription21)
        assertTrue subscriptionListService.addSubscription(subscription22)

        def conditions = new Conditions()
        conditions.and(SubscriptionCondition.subscribed(sl1))
        conditions.and(SubscriptionCondition.subscribed(sl2))

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber2)
        assertFalse subscribers.list.contains(subscriber1)
    }

    void testDescribe() {
        def conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('FirstName', ValueCondition.empty()))
        def description = subscriberSearchService.describe(conditions)
        assertEquals "Field 'First Name' is empty", description
    }

    Subscriber createTestSubscriber(id) {
        def subscriber = new Subscriber()
        subscriber.firstName = 'John'
        subscriber.lastName = 'Doe'
        subscriber.email = "john.doe_$id@nowhere.com"
        subscriber.gender = null
        subscriber.timezone = null
        subscriber.language = null
        subscriber.namePrefix = null
        subscriber.enabled = true
        subscriber.member = member
        return subscriber
    }

    DynamicField createDynamicField(def id) {
        def dynamicField = new DynamicField()

        dynamicField.name = 'dynamicField' + id
        dynamicField.type = DynamicFieldType.String
        dynamicField.status = DynamicFieldStatus.Active
        dynamicField.sequence = 0
        dynamicField.label = 'Dynaic Field Label'
        dynamicField.mandatory = true
        dynamicField.owner = member
        dynamicField.max = 10
        dynamicField.min = -5
        dynamicField.maxlength = 120

        return dynamicField
    }

    SubscriptionList createTestSubscriptionList(id) {
        SubscriptionList subscriptionList = new SubscriptionList()
        subscriptionList.name = 'Subscribers list' + id
        subscriptionList.description = 'Subscribers list description'
        subscriptionList.owner = member
        subscriptionList.subscribersNumber = 100
        return subscriptionList
    }
}
