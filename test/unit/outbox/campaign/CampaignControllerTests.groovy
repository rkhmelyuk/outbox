package outbox.campaign

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.member.MemberRole
import outbox.search.OwnedByCondition
import outbox.search.PageCondition
import outbox.security.OutboxUser
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService

/**
 * @author Ruslan Khmelyuk
 */
class CampaignControllerTests extends ControllerUnitTestCase {

    @Override protected void setUp() {
        super.setUp();
        controller.class.metaClass.createLink = { 'link' }
    }

    void testIndex() {

        def campaigns = [new Campaign(id: 1), new Campaign(id: 2)]

        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }

        mockDomain(Campaign)

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.searchCampaigns { conditions ->
            assertNotNull conditions
            assertEquals 1, conditions.get(OwnedByCondition).member.id
            assertEquals 10, conditions.get(PageCondition).max
            return campaigns
        }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.index()

        campaignServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals campaigns, result.campaigns
    }

    void testCreate() {
        def result = controller.create()
        assertNotNull result.campaign
        assertNull result.campaign.id
    }

    void testAdd_Success() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.addCampaign {
            assertEquals 'Campaign Name', it.name
            assertEquals 'Campaign Description', it.description
            return true
        }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Campaign Name'
        controller.params.description = 'Campaign Description'

        controller.add()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertEquals 'link', result.redirectTo
        assertNull result.error
    }

    void testAdd_Fail() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        mockDomain(Campaign)

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.addCampaign {
            assertEquals 'Campaign Name', it.name
            assertEquals 'Campaign Description', it.description
            return false
        }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Campaign Name'
        controller.params.description = 'Campaign Description'

        controller.add()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.redirectTo
        assertNotNull result.errors
    }

    void testShow() {
        def member = new Member(id: 1)
        def campaign = new Campaign(id: 10, name: 'Name', owner: member)

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id ->
            assertEquals 10, id
            return campaign
        }
        campaignServiceControl.demand.getTotalSubscribersNumber { 0 }
        controller.campaignService = campaignServiceControl.createMock()

        controller.params.id = '10'
        controller.params.page = 'details'
        def result = controller.show()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        assertEquals campaign, result.campaign
        assertEquals 'details', result.page
        assertTrue 'Need subscribers.', result.needSubscribers
        assertTrue 'Need template.', result.needTemplate
    }

    void testShow_WrongPage() {
        def member = new Member(id: 1)
        def campaign = new Campaign(id: 10, name: 'Name', owner: member)

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id ->
            assertEquals 10, id
            return campaign
        }
        campaignServiceControl.demand.getTotalSubscribersNumber { 0 }
        controller.campaignService = campaignServiceControl.createMock()

        controller.params.id = '10'
        controller.params.page = 'abracadabra'
        def result = controller.show()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        assertEquals campaign, result.campaign
        assertEquals 'details', result.page
        assertTrue 'Need subscribers.', result.needSubscribers
        assertTrue 'Need template.', result.needTemplate
    }

    void testShow_AbsentReports() {
        def member = new Member(id: 1)
        def campaign = new Campaign(id: 10, name: 'Name', owner: member)

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id ->
            assertEquals 10, id
            return campaign
        }
        campaignServiceControl.demand.getTotalSubscribersNumber { 10 }
        controller.campaignService = campaignServiceControl.createMock()

        controller.params.id = '10'
        controller.params.page = 'reports'
        def result = controller.show()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        assertEquals campaign, result.campaign
        assertFalse 'Need subscribers.', result.needSubscribers
        assertTrue 'Need template.', result.needTemplate
        assertEquals 'details', result.page
    }

    void testShow_NotFound() {
        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { null }
        controller.campaignService = campaignServiceControl.createMock()

        controller.params.id = '10'
        controller.show()

        assertEquals 404, mockResponse.status

        campaignServiceControl.verify()
    }

    void testShow_Denied() {
        def member = new Member(id: 1)
        def campaign = new Campaign(id: 10, name: 'Name', owner: null)

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { campaign }
        controller.campaignService = campaignServiceControl.createMock()

        controller.params.id = '10'
        controller.show()
        assertEquals 403, mockResponse.status

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()
    }

    void testEdit_Success() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id -> new Campaign(id: id, owner: member) }
        campaignServiceControl.demand.saveCampaign {
            assertEquals 1, it.id
            assertEquals 'Campaign Name', it.name
            assertEquals 'Campaign Subject', it.subject
            assertEquals 'Campaign Description', it.description
            return true
        }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '1'
        controller.params.name = 'Campaign Name'
        controller.params.description = 'Campaign Description'
        controller.params.subject = 'Campaign Subject'

        controller.update()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertEquals 'Campaign Name', result.name
        assertNull result.error
    }

    void testEdit_Fail() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }

        mockDomain(Campaign)

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id -> new Campaign(id: id, owner: member) }
        campaignServiceControl.demand.saveCampaign {
            assertEquals 1, it.id
            assertEquals 'Campaign Name', it.name
            assertEquals 'Campaign Subject', it.subject
            assertEquals 'Campaign Description', it.description
            return false
        }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '1'
        controller.params.name = 'Campaign Name'
        controller.params.description = 'Campaign Description'
        controller.params.subject = 'Campaign Subject'

        controller.update()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.name
        assertNotNull result.errors
    }

    void testAddSubscriptionList() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }
        MemberRole.class.metaClass.static.findAllByMember = { null }

        mockDomain(Campaign)

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id ->
            assertEquals 1, id
            new Campaign(id: id, owner: member, state: CampaignState.New) }
        campaignServiceControl.demand.addCampaignSubscription { true }
        campaignServiceControl.demand.getProposedSubscriptionLists { [] }
        campaignServiceControl.demand.getTotalSubscribersNumber { 0 }
        campaignServiceControl.demand.getCampaignSubscriptions { [] }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            assertEquals 2, id
            new SubscriptionList(id: id, owner: member)
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.campaignId = '1'
        controller.params.subscriptionList = '2'

        controller.class.metaClass.render = { String template, Map model ->  'model' }

        controller.addSubscriptionList()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()
        subscriptionListServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be success.', result.success
        assertNull result.error
    }

    void testAddSubscriptionList_Failed() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }
        MemberRole.class.metaClass.static.findAllByMember = { null }

        mockDomain(CampaignSubscription)

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id ->
            assertEquals 1, id
            new Campaign(id: id, owner: member, state: CampaignState.New) }
        campaignServiceControl.demand.addCampaignSubscription { false }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            assertEquals 2, id
            new SubscriptionList(id: id, owner: member)
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.campaignId = '1'
        controller.params.subscriptionList = '2'

        controller.class.metaClass.render = { String template, Map model ->  'model' }

        controller.addSubscriptionList()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()
        subscriptionListServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be success.', result.error
        assertNull result.success
    }

    void testRemoveSubscriptionList() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }
        MemberRole.class.metaClass.static.findAllByMember = { null }

        mockDomain(Campaign)

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaignSubscription { id ->
            assertEquals 2, id
            return new CampaignSubscription(
                    id: id,
                    campaign: new Campaign(id: id, owner: member, state: CampaignState.Ready),
                    subscriptionList: new SubscriptionList(id: id, owner: member)
            )
        }
        campaignServiceControl.demand.deleteCampaignSubscription { true }
        campaignServiceControl.demand.getProposedSubscriptionLists { [] }
        campaignServiceControl.demand.getTotalSubscribersNumber { 0 }
        campaignServiceControl.demand.getCampaignSubscriptions { [] }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.campaignSubscriptionId = '2'
        controller.removeSubscriptionList()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Must be success.', result.success
        assertNull result.error
    }

    void testRemoveSubscriptionList_Failed() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }
        MemberRole.class.metaClass.static.findAllByMember = { null }

        mockDomain(CampaignSubscription)

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaignSubscription { id ->
            assertEquals 2, id
            return new CampaignSubscription(
                    id: id,
                    campaign: new Campaign(id: id, owner: member, state: CampaignState.New),
                    subscriptionList: new SubscriptionList(id: id, owner: member)
            )
        }
        campaignServiceControl.demand.deleteCampaignSubscription { false }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.campaignSubscriptionId = '2'
        controller.removeSubscriptionList()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Must be success.', result.error
        assertNull result.success
    }

}
