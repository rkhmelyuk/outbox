package outbox.subscriber

import org.hibernate.Session
import outbox.member.Member
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldStatus
import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.field.DynamicFieldValue
import outbox.subscription.Subscription
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService
import outbox.subscription.SubscriptionStatus

/**
 * {@link SubscriberService} tests.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriberServiceTests extends GroovyTestCase {

    SubscriberService subscriberService
    SubscriptionListService subscriptionListService
    DynamicFieldService dynamicFieldService

    Member member

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

    void testAddSubscriber() {
        Subscriber subscriber = createTestSubscriber() 
        assertTrue 'Subscriber is not added.', subscriberService.saveSubscriber(subscriber)
        assertNotNull 'Subscriber id is not generated.', subscriber.id
        assertEquals subscriber, subscriberService.getSubscriber(subscriber.id)
    }

    void testWrongEmailSubscriber() {
        Subscriber subscriber = new Subscriber(email: 'test@mailsight', member: member)
        assertFalse subscriberService.saveSubscriber(subscriber)
    }

    void testEmailRequiredOnlySubscriber() {
        Subscriber subscriber = new Subscriber(email: null, member: member)
        assertFalse subscriberService.saveSubscriber(subscriber)
    }

    void testRequiredEmailSubscriber() {
        Subscriber subscriber = new Subscriber(email: 'test@mailsight.com', member: member)
        assertTrue subscriberService.saveSubscriber(subscriber)
    }

    void testSaveSubscriber() {
        Subscriber subscriber = createTestSubscriber()
        assertTrue 'Subscriber is not added.', subscriberService.saveSubscriber(subscriber)
        assertNotNull 'Subscriber id is not generated.', subscriber.id

        subscriber.firstName = 'First'
        subscriber.lastName = 'Last'
        subscriber.email = 'email@mail.com'

        assertTrue 'Subscriber is not changed.', subscriberService.saveSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull 'Subscriber is not found.', found
        assertEquals subscriber, found
    }

    void testGetSubscriber() {
        Subscriber subscriber = createTestSubscriber()

        assertTrue 'Subscriber is not added.', subscriberService.saveSubscriber(subscriber)
        
        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull 'Subscriber is not found.', found
        assertEquals subscriber, found
    }

    void testEnableSubscriber() {
        Subscriber subscriber = createTestSubscriber()
        subscriber.enabled = false

        assertTrue subscriberService.saveSubscriber(subscriber)
        assertTrue subscriberService.enableSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull found
        assertTrue subscriber.enabled
    }

    void testDisableSubscriber() {
        Subscriber subscriber = createTestSubscriber()

        assertTrue subscriberService.saveSubscriber(subscriber)
        assertTrue subscriberService.disableSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull found
        assertFalse subscriber.enabled
    }

    void testEnableEnabledSubscriber() {
        Subscriber subscriber = createTestSubscriber()

        assertTrue subscriberService.saveSubscriber(subscriber)
        assertTrue subscriberService.enableSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull found
        assertTrue subscriber.enabled
    }

    void testDisableDisabledSubscriber() {
        Subscriber subscriber = createTestSubscriber()
        subscriber.enabled = false
        assertTrue subscriberService.saveSubscriber(subscriber)
        assertTrue subscriberService.disableSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull found
        assertFalse subscriber.enabled
    }

    void testSaveDuplicateSubscriber() {
        Subscriber subscriber1 = createTestSubscriber()
        Subscriber subscriber2 = createTestSubscriber()

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertFalse subscriberService.saveSubscriber(subscriber2)
    }

    void testGetMemberSubscriberTypes() {
        def types = [
                new SubscriberType(name: 'Type1', member: member),
                new SubscriberType(name: 'Type2', member: member),
                new SubscriberType(name: 'Type3', member: member),
                new SubscriberType(name: 'Type4', member: member),
                new SubscriberType(name: 'Type5', member: member)]

        types.each { it ->
            assertTrue 'Not added subscriber type.', subscriberService.addSubscriberType(it)
        }

        def foundTypes = subscriberService.getSubscriberTypes(member)

        assertNotNull foundTypes
        assertEquals 5, foundTypes.size()
    }

    void testAddSubscriberType() {
        def subscriberType = new SubscriberType(name: 'Type', member: member)
        subscriberService.addSubscriberType subscriberType
        subscriberType = SubscriberType.get(subscriberType.id)

        assertEquals 'Type', subscriberType.name
        assertEquals member.id, subscriberType.member.id
    }

    void testSaveSubscriberType() {
        def subscriberType = new SubscriberType(name: 'Type', member: member)
        subscriberService.addSubscriberType subscriberType
        subscriberType = SubscriberType.get(subscriberType.id)

        subscriberType.name = 'Type2'
        subscriberService.saveSubscriberType subscriberType
        subscriberType = SubscriberType.get(subscriberType.id)

        assertEquals 'Type2', subscriberType.name
        assertEquals member.id, subscriberType.member.id
    }

    void testDeleteSubscriberType() {
        def subscriberType = new SubscriberType(name: 'Type', member: member)
        assertTrue 'Not added subscriber type.', subscriberService.addSubscriberType(subscriberType)

        def subscriber = createTestSubscriber()
        subscriber.subscriberType = subscriberType
        assertTrue 'Not added subscriber.', subscriberService.saveSubscriber(subscriber)

        subscriberService.deleteSubscriberType subscriberType

        assertNull subscriberService.getMemberSubscriberType(member.id, subscriberType.id)

        Subscriber.withSession {Session session -> session.clear() }

        subscriber = subscriberService.getSubscriber(subscriber.id)
        assertNotNull subscriber
        assertNull subscriber.subscriberType
    }

    void testGetSubscribersWithoutSubscriptionCount() {
        def subscriptionStatus = new SubscriptionStatus(id: 1, name: 'test').save()

        def subscriber1 = createTestSubscriber()
        def subscriber2 = createTestSubscriber()
        def subscriber3 = createTestSubscriber()

        subscriber2.email = 'subscriber2@mailsight.com'
        subscriber3.email = 'subscriber3@mailsight.com'

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)
        assertTrue subscriberService.saveSubscriber(subscriber3)

        def list = new SubscriptionList(name: 'Test list', owner: member)
        assertTrue subscriptionListService.saveSubscriptionList(list)

        def subscription1 = new Subscription(subscriber: subscriber1, subscriptionList: list, status: subscriptionStatus)
        def subscription2 = new Subscription(subscriber: subscriber2, subscriptionList: list, status: subscriptionStatus)

        assertNotNull subscription1.save()
        assertNotNull subscription2.save()

        assertEquals 1, subscriberService.getSubscribersWithoutSubscriptionCount(member)

        def subscription3 = new Subscription(subscriber: subscriber3, subscriptionList: list, status: subscriptionStatus)
        assertNotNull subscription3.save()

        assertEquals 0, subscriberService.getSubscribersWithoutSubscriptionCount(member)
    }

    void testGetSubscriberDynamicFields() {
        def field1 = createDynamicField(1)
        def field2 = createDynamicField(2)
        def field3 = createDynamicField(3)
        def field4 = createDynamicField(4)
        def field5 = createDynamicField(5)
        field5.status = DynamicFieldStatus.Hidden

        assertTrue dynamicFieldService.addDynamicField(field1)
        assertTrue dynamicFieldService.addDynamicField(field2)
        assertTrue dynamicFieldService.addDynamicField(field3)
        assertTrue dynamicFieldService.addDynamicField(field4)
        assertTrue dynamicFieldService.addDynamicField(field5)

        def subscriber = createTestSubscriber()
        assertTrue subscriberService.saveSubscriber(subscriber)

        def value1 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field1, value: '1')
        def value2 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field2, value: '2')
        def value3 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field3, value: '3')
        def value5 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field5, value: '5')

        assertTrue dynamicFieldService.saveDynamicFieldValue(value1)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value2)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value3)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value5)

        def values = subscriberService.getSubscriberDynamicFields(subscriber)

        assertEquals 5, values.fields.size()
        assertEquals 4, values.values.size()

        assertEquals '1', values.value(field1)
        assertEquals '2', values.value(field2)
        assertEquals '3', values.value(field3)
        assertEquals '5', values.value(field5)
        assertNull values.value(field4)
    }

    void testActiveGetSubscriberDynamicFields() {
        def field1 = createDynamicField(1)
        def field2 = createDynamicField(2)
        def field3 = createDynamicField(3)
        def field4 = createDynamicField(4)
        def field5 = createDynamicField(5)
        field5.status = DynamicFieldStatus.Hidden

        assertTrue dynamicFieldService.addDynamicField(field1)
        assertTrue dynamicFieldService.addDynamicField(field2)
        assertTrue dynamicFieldService.addDynamicField(field3)
        assertTrue dynamicFieldService.addDynamicField(field4)
        assertTrue dynamicFieldService.addDynamicField(field5)

        def subscriber = createTestSubscriber()
        assertTrue subscriberService.saveSubscriber(subscriber)

        def value1 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field1, value: '1')
        def value2 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field2, value: '2')
        def value3 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field3, value: '3')
        def value5 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field5, value: '5')

        assertTrue dynamicFieldService.saveDynamicFieldValue(value1)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value2)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value3)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value5)

        def values = subscriberService.getActiveSubscriberDynamicFields(subscriber)

        assertEquals 4, values.fields.size()
        assertEquals 3, values.values.size()

        assertEquals '1', values.value(field1)
        assertEquals '2', values.value(field2)
        assertEquals '3', values.value(field3)
        assertNull values.value(field4)
    }

    void testSaveSubscriberDynamicFields() {
        def field1 = createDynamicField(1)
        def field2 = createDynamicField(2)

        assertTrue dynamicFieldService.addDynamicField(field1)
        assertTrue dynamicFieldService.addDynamicField(field2)

        def subscriber = createTestSubscriber()
        assertTrue subscriberService.saveSubscriber(subscriber)

        def value1 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field1, value: '1')

        assertTrue dynamicFieldService.saveDynamicFieldValue(value1)

        def values = subscriberService.getSubscriberDynamicFields(subscriber)
        assertEquals 2, values.fields.size()
        assertEquals 1, values.values.size()

        def value2 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field2, value: '2')
        values.addValue(value2)
        assertTrue subscriberService.saveSubscriberDynamicFields(values)

        values = subscriberService.getSubscriberDynamicFields(subscriber)
        assertEquals 2, values.fields.size()
        assertEquals 2, values.values.size()
    }

    void assertEquals(Subscriber subscriber, Subscriber found) {
        assertEquals subscriber.id, found.id
        assertEquals subscriber.firstName, found.firstName
        assertEquals subscriber.lastName, found.lastName
        assertEquals subscriber.email, found.email
        assertEquals subscriber.dateCreated, found.dateCreated
        assertEquals subscriber.enabled, found.enabled
        assertEquals subscriber.gender, found.gender
        assertEquals subscriber.timezone, found.timezone
        assertEquals subscriber.language, found.language
        assertEquals subscriber.member?.id, found.member?.id
        assertEquals subscriber.namePrefix?.id, found.namePrefix?.id
    }

    Subscriber createTestSubscriber() {
        Subscriber subscriber = new Subscriber()
        subscriber.firstName = 'John'
        subscriber.lastName = 'Doe'
        subscriber.email = 'john.doe@nowhere.com'
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
