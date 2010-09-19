package outbox.campaign

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.search.OwnedByCondition
import outbox.search.PageCondition
import outbox.security.OutboxUser

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
        controller.campaignService = campaignServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.show()

        springSecurityServiceControl.verify()
        campaignServiceControl.verify()

        assertEquals campaign, result.campaign
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

}
