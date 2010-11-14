package outbox.subscriber.search

import outbox.member.Member
import outbox.subscriber.Subscriber
import outbox.subscriber.SubscriberService

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberSearchServiceTests extends GroovyTestCase {

    SubscriberService subscriberService
    def subscriberSearchService

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
        conditions.and(new SubscriberFieldCondition('FirstName', ValueCondition.equals('John')))

        def subscribers = subscriberSearchService.search(conditions)

        assertNotNull subscribers
        assertEquals 2, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertTrue subscribers.list.contains(subscriber2)
        assertFalse subscribers.list.contains(subscriber3)
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
}
