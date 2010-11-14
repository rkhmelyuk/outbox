package outbox.subscriber.search

import outbox.member.Member
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.Subscriber
import outbox.subscriber.SubscriberService
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldStatus
import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.field.DynamicFieldValue
import outbox.subscriber.search.condition.Conditions
import outbox.subscriber.search.condition.DynamicFieldCondition
import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.ValueCondition

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberSearchServiceTests extends GroovyTestCase {

    SubscriberService subscriberService
    DynamicFieldService dynamicFieldService
    SubscriberSearchService subscriberSearchService

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
}
