package outbox.subscriber

import outbox.member.Member

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

    void testSaveDuplicatedSubscribersList() {
        def subscriptionList1 = createTestSubscriptionList()
        def subscriptionList2 = createTestSubscriptionList()
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList1)
        assertFalse subscriptionListService.saveSubscriptionList(subscriptionList2)
        
        assertNull subscriptionList2.id
    }

    void testGetSubscribersList() {
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

    void testDeleteSubscribersList() {
        def subscriptionList = createTestSubscriptionList()
        assertTrue subscriptionListService.saveSubscriptionList(subscriptionList)
        assertNotNull subscriptionList.id

        subscriptionListService.deleteSubscriptionList subscriptionList
        assertNull subscriptionListService.getSubscriptionList(subscriptionList.id)
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
}
