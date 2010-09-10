package outbox.subscriber

import org.hibernate.Session
import outbox.member.Member

/**
 * {@link SubscriberService} tests.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriberServiceTests extends GroovyTestCase {

    SubscriberService subscriberService

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
}
