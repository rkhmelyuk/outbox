package outbox.subscriber

import outbox.member.Member

/**
 * {@link SubscribersListService}  tests.
 *
 * @author Ruslan Khmelyuk
 */
class SubscribersListServiceTests extends GroovyTestCase {

    def member

    SubscribersListService subscribersListService

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

    void testSaveSubscribersList() {
        def subscribersList = createTestSubscribersList()
        assertTrue 'Subscribers List is not saved.', subscribersListService.saveSubscribersList(subscribersList)
        assertNotNull subscribersList.id
        def found = subscribersListService.getSubscribersList(subscribersList.id)
        assertNotNull found
        assertEquals subscribersList, found
    }

    void testGetSubscribersList() {
        def subscribersList1 = createTestSubscribersList()
        def subscribersList2 = createTestSubscribersList()

        assertTrue subscribersListService.saveSubscribersList(subscribersList1)
        assertTrue subscribersListService.saveSubscribersList(subscribersList2)

        def found1 = subscribersListService.getSubscribersList(subscribersList1.id)
        def found2 = subscribersListService.getSubscribersList(subscribersList2.id)

        assertEquals subscribersList1, found1
        assertEquals subscribersList2, found2
    }

    void testDeleteSubscribersList() {
        def subscribersList = createTestSubscribersList()
        assertTrue subscribersListService.saveSubscribersList(subscribersList)
        assertNotNull subscribersList.id

        subscribersListService.deleteSubscribersList subscribersList
        assertNull subscribersListService.getSubscribersList(subscribersList.id)
    }

    void assertEquals(SubscribersList subscribersList, SubscribersList found) {
        assertEquals subscribersList.id, found.id
        assertEquals subscribersList.name, found.name
        assertEquals subscribersList.description, found.description
        assertEquals subscribersList.dateCreated, found.dateCreated
        assertEquals subscribersList.subscribersNumber, found.subscribersNumber
        assertEquals subscribersList.owner?.id, found.owner?.id
    }

    SubscribersList createTestSubscribersList() {
        SubscribersList subscribersList = new SubscribersList()
        subscribersList.name = 'Subscribers list'
        subscribersList.description = 'Subscribers list description'
        subscribersList.owner = member
        subscribersList.subscribersNumber = 100
        return subscribersList
    }
}
