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
import outbox.template.Template
import outbox.template.TemplateService
import outbox.report.*

/**
 * @author Ruslan Khmelyuk
 */
class CampaignControllerTests extends ControllerUnitTestCase {

    @Override protected void setUp() {
        super.setUp();
        controller.class.metaClass.createLink = { 'link' }
        controller.class.metaClass.message = { 'message' }
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
        def campaign = new Campaign(id: 10, name: 'Name', owner: member, state: CampaignState.Ready)

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
        def campaign = new Campaign(id: 10, name: 'Name', owner: member, state: CampaignState.Ready)

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
        def campaign = new Campaign(id: 10, name: 'Name', owner: member, state: CampaignState.Ready)

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
        def campaign = new Campaign(id: 10, name: 'Name', owner: null, state: CampaignState.Ready)

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
        campaignServiceControl.demand.getCampaign { id -> new Campaign(id: id, owner: member, state: CampaignState.New) }
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

    void testEdit_Started() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }

        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id -> new Campaign(id: id, owner: member,
                subject: 'Subject', state: CampaignState.InProgress) }
        campaignServiceControl.demand.saveCampaign {
            assertEquals 1, it.id
            assertEquals 'Campaign Name', it.name
            assertEquals 'Subject', it.subject
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
        campaignServiceControl.demand.getCampaign { id -> new Campaign(id: id, owner: member, state: CampaignState.New) }
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
        assertNotNull result.content
        assertNotNull result.notifications
        assertNotNull result.actions
        assertEquals 'message', result.stateName
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
        assertNotNull result.content
        assertNotNull result.notifications
        assertNotNull result.actions
        assertEquals 'message', result.stateName
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

    void testShow_Template() {
        def member = new Member(id: 1)
        def campaign = new Campaign(id: 10, name: 'Name', owner: member, state: CampaignState.Ready)

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
        campaignServiceControl.demand.getProposedTemplates { camp -> [new Template(id: 1)] }
        campaignServiceControl.demand.getTotalSubscribersNumber { 0 }
        controller.campaignService = campaignServiceControl.createMock()

        controller.params.id = '10'
        controller.params.page = 'template'
        
        def result = controller.show()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        assertEquals campaign, result.campaign
        assertEquals 'template', result.page
        assertNotNull result.proposedTemplates
    }

    void testSelectTemplate() {
        def member = new Member(id: 1)
        def campaign = new Campaign(id: 10, name: 'Name', owner: member, state: CampaignState.New)

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { id ->
            assertEquals 15, id    
            return new Template(id: id, owner: member)
        }
        controller.templateService = templateServiceControl.createMock()

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
        campaignServiceControl.demand.saveCampaign { true }
        campaignServiceControl.demand.getProposedTemplates { camp -> [new Template(id: 1)] }
        campaignServiceControl.demand.getTotalSubscribersNumber { 0 }
        controller.campaignService = campaignServiceControl.createMock()

        controller.params.campaignId = '10'
        controller.params.template = '15'

        controller.selectTemplate()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()
        templateServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Must be success.', result.success
        assertNull result.error
        assertNotNull result.content
        assertNotNull result.notifications
        assertNotNull result.actions
        assertEquals 'message', result.stateName
    }

    void testSend_Success() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }

        def campaignServiceControl = mockFor(CampaignService, true)
        campaignServiceControl.demand.getCampaign { id -> new Campaign(id: id, owner: member, state: CampaignState.Ready) }
        campaignServiceControl.demand.sendCampaign {
            assertEquals 1, it.id
            return true
        }
        campaignServiceControl.demand.getTotalSubscribersNumber { 0 }
        controller.campaignService = campaignServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '1'
        controller.params.page = 'details'

        controller.send()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertNull result.error
        assertNotNull result.notifications
        assertNotNull result.actions
        assertEquals 'message', result.stateName
    }

    void testShow_Reports1() {
        def member = new Member(id: 1)
        def campaign = new Campaign(id: 10, name: 'Name', owner: member,
                state: CampaignState.InProgress, startDate: new Date())

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
        controller.campaignService = campaignServiceControl.createMock()

        def reportControl = mockFor(Report)
        reportControl.demand.extract(6..6) { context ->
            assertEquals campaign.id, context.campaignId
            def result = new ReportResult()
            result.addSingle 'clicks', 10
            result.addSingle 'opens', 20
            result.addSingle 'number', 10
            return result
        }
        def report = reportControl.createMock()
        def reportsHolderControl = mockFor(ReportsHolder)
        reportsHolderControl.demand.getReport(6..6) { name ->
            if (name == 'totalClicks' || name == 'totalOpens' ||
                    name == 'clicksByDate' || name == 'opensByDate' ||
                    name == 'totalSubscribers' || name == 'opened') {
                return report
            }
            fail "Unexpected report name: $name"
        }
        controller.reportsHolder = reportsHolderControl.createMock()

        controller.params.id = '10'
        controller.params.page = 'reports'

        def result = controller.show()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()
        reportsHolderControl.verify()
        reportControl.verify()

        assertEquals campaign, result.campaign
        assertEquals 'reports', result.page
        assertEquals 10, result.totalClicks
        assertEquals 20, result.totalOpens
        assertEquals 10, result.opened
        assertEquals 10, result.totalSubscribers
        assertEquals 0, result.notOpened
    }

    void testShow_Reports2() {
        def member = new Member(id: 1)
        def campaign = new Campaign(id: 10, name: 'Name', owner: member,
                state: CampaignState.InProgress, startDate: new Date())

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
        controller.campaignService = campaignServiceControl.createMock()

        def reportControl = mockFor(Report)
        int index = 0
        reportControl.demand.extract(6..6) { context ->
            assertEquals campaign.id, context.campaignId
            if (index > 3) {
                assertEquals Period.Hour, context.period
            }
            index++

            def result = new ReportResult()
            result.addDataSet 'clicks', new ReportDataSet([1, 2])
            result.addDataSet 'opens', new ReportDataSet([1, 2, 3])
            return result
        }
        def report = reportControl.createMock()
        def reportsHolderControl = mockFor(ReportsHolder)
        reportsHolderControl.demand.getReport(6..6) { name ->
            if (name == 'totalClicks' || name == 'totalOpens' ||
                    name == 'clicksByDate' || name == 'opensByDate' ||
                    name == 'totalSubscribers' || name == 'opened') {
                return report
            }
            fail "Unexpected report name: $name"
        }
        controller.reportsHolder = reportsHolderControl.createMock()

        controller.params.id = '10'
        controller.params.page = 'reports'

        def result = controller.show()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()
        reportsHolderControl.verify()
        reportControl.verify()

        assertEquals campaign, result.campaign
        assertEquals 'reports', result.page
        assertEquals 2, result.clicksByDate.size()
        assertEquals 3, result.opensByDate.size()
    }

}
