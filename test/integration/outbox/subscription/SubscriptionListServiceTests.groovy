package outbox.subscription

import outbox.member.Member
import outbox.subscriber.Subscriber

/**
 * {@link SubscriptionListService}  tests.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriptionListServiceTests extends GroovyTestCase {

    def member

    SubscriptionListService subscriptionListService

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

    void testSaveSubscriptionList() {
        def subscriptionList = createTestSubscriptionList()
        assertTrue 'Subscribers List is not saved.', subscriptionListService.saveSubscriptionList(subscriptionList)
        assertNotNull subscriptionList.id
        def found = subscriptionListService.getSubscriptionList(subscriptionList.id)
        assertNotNull found
        assertEquals subscriptionList, found
    }

    void testSaveDuplicatedSubscriptionList() {
        def subscriptionList1 = createTestSubscriptionList()
        def subscriptionList2 = createTestSubscriptionList()
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList1)
        assertFalse subscriptionListService.saveSubscriptionList(subscriptionList2)
        
        assertNull subscriptionList2.id
    }

    void testRestoreSubscriptionList() {
        def subscriptionList = createTestSubscriptionList()
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList)
        assertTrue subscriptionListService.archiveSubscriptionList(subscriptionList)
        assertTrue subscriptionListService.restoreSubscriptionList(subscriptionList)

        def found = subscriptionListService.getSubscriptionList(subscriptionList.id)
        assertFalse found.archived
    }

    void testGetSubscriptionList() {
        def subscriptionList1 = createTestSubscriptionList()
        def subscriptionList2 = createTestSubscriptionList()
        subscriptionList2.name = 'Other name'

        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList1)
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList2)

        def found1 = subscriptionListService.getSubscriptionList(subscriptionList1.id)
        def found2 = subscriptionListService.getSubscriptionList(subscriptionList2.id)

        assertEquals subscriptionList1, found1
        assertEquals subscriptionList2, found2
    }

    void testDeleteSubscriptionList_Empty() {
        def subscriptionList = createTestSubscriptionList()
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList)
        assertNotNull subscriptionList.id

        subscriptionListService.deleteSubscriptionList subscriptionList
        assertNull subscriptionListService.getSubscriptionList(subscriptionList.id)
    }

    void testDeleteSubscriptionList_NotEmpty() {
        def subscriptionList = createTestSubscriptionList()
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList)
        assertNotNull subscriptionList.id

        def status = new SubscriptionStatus(id: 1, name: 'test').save()
        def subscriber1 = createTestSubscriber(1).save()
        def subscriber2 = createTestSubscriber(2).save()
        def subscription1 = new Subscription(subscriber: subscriber1, subscriptionList: subscriptionList, status: status)
        def subscription2 = new Subscription(subscriber: subscriber2, subscriptionList: subscriptionList, status: status)

        assertTrue subscriptionListService.addSubscription(subscription1)
        assertTrue subscriptionListService.addSubscription(subscription2)

        subscriptionListService.deleteSubscriptionList subscriptionList
        assertNull subscriptionListService.getSubscriptionList(subscriptionList.id)
        assertEquals 0, subscriptionListService.getSubscriptionsForList(subscriptionList).size()
    }

    void testAddSubscription() {
        def subscriptionList = createTestSubscriptionList()
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList)

        def status = new SubscriptionStatus(id: 1, name: 'test').save()
        def subscriber = createTestSubscriber(1).save()
        def subscription = new Subscription(subscriber: subscriber, subscriptionList: subscriptionList, status: status)

        assertTrue subscriptionListService.addSubscription(subscription)

        def found = subscriptionListService.getSubscriptionsForList(subscriptionList)
        assertEquals 1, found.size()
        assertTrue found.contains(subscription)
        assertEquals 'test', found.first().status.name
        assertEquals subscriber.id, found.first().subscriber.id
        assertEquals subscriptionList.id, found.first().subscriptionList.id
    }

    void testGetSubscriptions() {
        def subscriptionList1 = createTestSubscriptionList()
        def subscriptionList2 = createTestSubscriptionList()
        subscriptionList1.name += '1'
        subscriptionList2.name += '2'
        
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList1)
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList2)

        def status = new SubscriptionStatus(id: 1, name: 'test').save()
        def subscriber1 = createTestSubscriber(1).save()
        def subscriber2 = createTestSubscriber(2).save()
        def subscriber3 = createTestSubscriber(3).save()

        def subscription1 = new Subscription(subscriber: subscriber1, subscriptionList: subscriptionList1, status: status)
        def subscription2 = new Subscription(subscriber: subscriber2, subscriptionList: subscriptionList1, status: status)
        def subscription3 = new Subscription(subscriber: subscriber3, subscriptionList: subscriptionList2, status: status)

        assertTrue subscriptionListService.addSubscription(subscription1)
        assertTrue subscriptionListService.addSubscription(subscription2)
        assertTrue subscriptionListService.addSubscription(subscription3)

        def found = subscriptionListService.getSubscriptionsForList(subscriptionList1)
        assertEquals 2, found.size()
        assertTrue found.contains(subscription1)
        assertTrue found.contains(subscription2)
        assertFalse found.contains(subscription3)

        found = subscriptionListService.getSubscriptionsForList(subscriptionList2)
        assertEquals 1, found.size()
        assertTrue found.contains(subscription3)
    }

    void testSearchSubscriptionList_All() {
        def subscriptionList1 = createTestSubscriptionList()
        def subscriptionList2 = createTestSubscriptionList()
        subscriptionList1.name += '1'
        subscriptionList1.archived = true
        subscriptionList2.name += '2'
        subscriptionList2.archived = false

        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList1)
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList2)

        def conditions = new SubscriptionListConditionsBuilder().build {
            ownedBy member
        }

        def found = subscriptionListService.search(conditions).list
        assertEquals 2, found.size()

        assertTrue found.contains(subscriptionList1)
        assertTrue found.contains(subscriptionList2)
    }

    void testSearchSubscriptionList_Archived() {
        def subscriptionList1 = createTestSubscriptionList()
        def subscriptionList2 = createTestSubscriptionList()
        subscriptionList1.name += '1'
        subscriptionList1.archived = true
        subscriptionList2.name += '2'
        subscriptionList2.archived = false

        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList1)
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList2)

        def conditions = new SubscriptionListConditionsBuilder().build {
            ownedBy member
            archived true
        }
        def found = subscriptionListService.search(conditions).list
        assertEquals 1, found.size()

        assertTrue found.contains(subscriptionList1)

        conditions = new SubscriptionListConditionsBuilder().build {
            ownedBy member
            archived false
        }
        found = subscriptionListService.search(conditions).list
        assertEquals 1, found.size()
        assertTrue found.contains(subscriptionList2)
    }

    void assertEquals(SubscriptionList subscriptionList, SubscriptionList found) {
        assertEquals subscriptionList.id, found.id
        assertEquals subscriptionList.name, found.name
        assertEquals subscriptionList.description, found.description
        assertEquals subscriptionList.dateCreated, found.dateCreated
        assertEquals subscriptionList.subscribersNumber, found.subscribersNumber
        assertEquals subscriptionList.owner?.id, found.owner?.id
    }

    SubscriptionList createTestSubscriptionList() {
        SubscriptionList subscriptionList = new SubscriptionList()
        subscriptionList.name = 'Subscribers list'
        subscriptionList.description = 'Subscribers list description'
        subscriptionList.owner = member
        subscriptionList.subscribersNumber = 100
        return subscriptionList
    }

    Subscriber createTestSubscriber(id) {
        Subscriber subscriber = new Subscriber()
        subscriber.firstName = 'John'
        subscriber.lastName = 'Doe'
        subscriber.email = "john.doe$id@nowhere.com"
        subscriber.gender = null
        subscriber.timezone = null
        subscriber.language = null
        subscriber.namePrefix = null
        subscriber.enabled = true
        subscriber.member = member
        return subscriber
    }
}
