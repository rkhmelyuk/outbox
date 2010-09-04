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

    void testGetSubscriber() {
        Subscriber subscriber = createTestSubscriber()

        assertTrue 'Subscriber is not added.', subscriberService.addSubscriber(subscriber)
        assertNotNull 'Subscriber is not found.', subscriberService.getSubscriber(subscriber.id)
    }

    void testEnableSubscriber() {
        Subscriber subscriber = createTestSubscriber()
        subscriber.enabled = false
        
        assertTrue subscriberService.addSubscriber(subscriber)
        assertTrue subscriberService.enableSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull found
        assertTrue subscriber.enabled
    }

    void testDisableSubscriber() {
        Subscriber subscriber = createTestSubscriber()

        assertTrue subscriberService.addSubscriber(subscriber)
        assertTrue subscriberService.disableSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull found
        assertFalse subscriber.enabled
    }

    void testEnableEnabledSubscriber() {
        Subscriber subscriber = createTestSubscriber()

        assertTrue subscriberService.addSubscriber(subscriber)
        assertTrue subscriberService.enableSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull found
        assertTrue subscriber.enabled
    }

    void testDisableDisabledSubscriber() {
        Subscriber subscriber = createTestSubscriber()
        subscriber.enabled = false
        assertTrue subscriberService.addSubscriber(subscriber)
        assertTrue subscriberService.disableSubscriber(subscriber)

        Subscriber found = subscriberService.getSubscriber(subscriber.id)
        assertNotNull found
        assertFalse subscriber.enabled
    }

    void testSaveDuplicateSubscriber() {
        Subscriber subscriber1 = createTestSubscriber()
        Subscriber subscriber2 = createTestSubscriber()

        assertTrue subscriberService.addSubscriber(subscriber1)
        try {
            subscriberService.addSubscriber(subscriber2)
            fail 'Added subscriber with duplicated id - that\'s wrong'
        }
        catch (Exception e) {
            // that's ok
        }
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
