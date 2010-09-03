package outbox.subscriber

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
                email: 'test.member@nowhere.com',
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
        assertTrue 'Subscriber is not added.', subscriberService.addSubscriber(subscriber)
        assertNotNull 'Subscriber id is not generated.', subscriber.id
    }

    void testSaveSubscriber() {
        Subscriber subscriber = createTestSubscriber()
        assertTrue 'Subscriber is not added.', subscriberService.addSubscriber(subscriber)
        assertNotNull 'Subscriber id is not generated.', subscriber.id
    }

    void testEnableSubscriber() {

    }

    void testDisableSubscriber() {

    }

    Subscriber createTestSubscriber() {
        Subscriber subscriber = new Subscriber()
        subscriber.firstName = 'John'
        subscriber.lastName = 'Doe'
        subscriber.email = 'john.doe@nowhere.com'
        subscriber.gender = null
        subscriber.timezone = null
        subscriber.language = null
        subscriber.enabled = true
        subscriber.member = member
        subscriber.createDate = new Date()
        return subscriber
    }
}
