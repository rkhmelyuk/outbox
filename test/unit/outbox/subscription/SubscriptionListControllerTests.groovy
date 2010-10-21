package outbox.subscription

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.campaign.CampaignService
import outbox.campaign.CampaignSubscription
import outbox.member.Member
import outbox.search.ArchivedCondition
import outbox.search.OwnedByCondition
import outbox.search.PageCondition
import outbox.search.SearchResult
import outbox.security.OutboxUser
import outbox.subscriber.Subscriber
import outbox.subscriber.SubscriberService

/**
 * {@link SubscriptionListController} tests.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriptionListControllerTests extends ControllerUnitTestCase {

    protected void setUp() {
        super.setUp();
        controller.class.metaClass.createLink = { null }
    }

    void testList() {
        def member = new Member(id: 10)
        def subscriptionLists = null

        Member.class.metaClass.static.load = { id -> member }

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscribersWithoutSubscriptionCount {
            assertEquals member.id, it.id
            return 5
        }
        controller.subscriberService = subscriberServiceControl.createMock()

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.search { conditions ->
            assertEquals member.id, conditions.get(OwnedByCondition).member.id
            assertFalse conditions.get(ArchivedCondition).archived
            assertTrue conditions.includeCount

            def pageConditions = conditions.get(PageCondition)
            assertNotNull pageConditions
            assertEquals 10, pageConditions.max
            assertEquals 1, pageConditions.page

            return new SearchResult(list: subscriptionLists, total: 10)
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def conditions = new SubscriptionListController.SubscriptionListConditions()
        def result = controller.list(conditions)

        assertNotNull result
        assertEquals subscriptionLists, result.subscriptionLists
        assertEquals 10, result.conditions.itemsPerPage
        assertEquals 1, result.conditions.page
        assertEquals 5, result.freeSubscribersCount
        assertEquals 10, result.total
    }

    void testArchived() {
        def member = new Member(id: 10)
        def subscriptionLists = null

        Member.class.metaClass.static.load = { id -> member }

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.search { conditions ->
            assertEquals member.id, conditions.get(OwnedByCondition).member.id
            assertTrue conditions.get(ArchivedCondition).archived
            return new SearchResult(list: subscriptionLists, total: 10)
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.archived()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNotNull result
        assertEquals subscriptionLists, result.subscriptionLists
        assertEquals 10, result.total
    }

    void testFreeSubscribers() {
        def member = new Member(id: 10)
        def subscribersList = [ new Subscriber(id: "00000"), new Subscriber(id: "11111")]

        Member.class.metaClass.static.load = { id -> member }

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscribersWithoutSubscription {
            assertEquals member.id, it.id
            subscribersList
        }
        controller.subscriberService = subscriberServiceControl.createMock()


        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.freeSubscribers()

        assertNotNull result
        assertEquals subscribersList, result.subscribers
    }

    void testCreate() {
        def result = controller.create()
        assertNotNull result
        assertNotNull result.subscriptionList
        assertNull result.subscriptionList.id
    }

    void testAdd_Success() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return true
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.add()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertNull result.error
    }

    void testAdd_Fail() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        mockDomain(SubscriptionList)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return false
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.add()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNotNull result.errors
    }

    void testEdit() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()

        assertNotNull result.subscriptionList
        assertEquals 10, result.subscriptionList.id
    }

    void testEdit_Denied() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            new SubscriptionList(id: id, owner: new Member(id: 1))
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 2))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()

        assertNull result
        assertEquals 403, mockResponse.status
    }

    void testEdit_NotFound() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> null }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()

        assertNull result
        assertEquals 404, mockResponse.status
    }

    void testUpdate_Success() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member)}
        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 10, it.id
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return true
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertNull result.error
    }

    void testUpdate_Fail() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscriptionList)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member)}
        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 10, it.id
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return false
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNotNull result.errors
    }

    void testUpdate_NotFound() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscriptionList)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> null}
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.errors
    }

    void testUpdate_Denied() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscriptionList)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            new SubscriptionList(id: id, owner: new Member(id: 2))}

        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 10, it.id
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return false
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.errors
    }

    void testShow() {
        def member = new Member(id: 1)
        def subscriptions = [new Subscription(id: 1), new Subscription(id: 2)]

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        subscriptionListServiceControl.demand.getSubscriptionsForList { list -> return subscriptions }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getSubscriptions { list -> [new CampaignSubscription(subscriptionList: list)] }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.show()

        campaignServiceControl.verify()
        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNotNull result.subscriptionList
        assertEquals 10, result.subscriptionList.id
        assertNotNull result.subscriptions
        assertEquals 2, result.subscriptions.size()
        assertEquals 1, result.campaignSubscriptions.size()
        assertFalse result.canDelete
    }

    void testShow_Denied() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            new SubscriptionList(id: id, owner: new Member(id: 1))
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 2))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.show()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNull result
        assertEquals 403, mockResponse.status
    }

    void testShow_NotFound() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> null }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.show()

        assertNull result
        assertEquals 404, mockResponse.status
    }

    void testDelete_Success() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        subscriptionListServiceControl.demand.deleteSubscriptionList { return true}
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.delete()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals '', controller.redirectArgs.action
    }

    void testDelete_Failed() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        subscriptionListServiceControl.demand.deleteSubscriptionList { return false }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.delete()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals 'show', controller.redirectArgs.action
        assertEquals 1, controller.redirectArgs.id
    }

    void testDelete_Denied() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            return new SubscriptionList(id: id, owner: new Member(id: 2))
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [],  new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.delete()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals '', controller.redirectArgs.action
    }

    void testDelete_NotFound() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> return null }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = 1
        controller.delete()

        subscriptionListServiceControl.verify()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals '', controller.redirectArgs.action
    }

    void testArchive_Success() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            assertEquals 1, id
            return new SubscriptionList(id: id, owner: member) }
        subscriptionListServiceControl.demand.archiveSubscriptionList { it ->
            assertEquals 1, it.id
            return true }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.archive()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'show', controller.redirectArgs.action
        assertEquals 1, controller.redirectArgs.id
    }

    void testArchive_Failed() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        subscriptionListServiceControl.demand.archiveSubscriptionList { it -> return false }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.archive()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'show', controller.redirectArgs.action
        assertEquals 1, controller.redirectArgs.id
    }

    void testArchive_Denied() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            return new SubscriptionList(id: id, owner: new Member(id: 2))
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [],  new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.archive()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals '', controller.redirectArgs.action
    }

    void testArchive_NotFound() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> return null }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = 1
        controller.archive()

        subscriptionListServiceControl.verify()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals '', controller.redirectArgs.action
    }


    // -------------------------------------

    void testRestore_Success() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            assertEquals 1, id
            return new SubscriptionList(id: id, owner: member) }
        subscriptionListServiceControl.demand.restoreSubscriptionList { it ->
            assertEquals 1, it.id
            return true }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.restore()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'show', controller.redirectArgs.action
        assertEquals 1, controller.redirectArgs.id
    }

    void testRestore_Failed() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        subscriptionListServiceControl.demand.restoreSubscriptionList { it -> return false }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.restore()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'show', controller.redirectArgs.action
        assertEquals 1, controller.redirectArgs.id
    }

    void testRestore_Denied() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            return new SubscriptionList(id: id, owner: new Member(id: 2))
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [],  new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.restore()

        subscriptionListServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals '', controller.redirectArgs.action
    }

    void testRestore_NotFound() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> return null }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = 1
        controller.restore()

        subscriptionListServiceControl.verify()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals '', controller.redirectArgs.action
    }

}
