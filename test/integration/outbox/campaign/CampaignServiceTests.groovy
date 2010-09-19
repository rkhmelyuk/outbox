package outbox.campaign

import grails.test.GrailsUnitTestCase
import outbox.member.Member
import outbox.template.Template

/**
 * @author Ruslan Khmelyuk
 */
class CampaignServiceTests extends GrailsUnitTestCase {

    CampaignService campaignService

    def member
    def template

    protected void setUp() {
        super.setUp();

        member = new Member(
                firstName: 'Test',
                lastName: 'Member',
                email: 'test+member@mailsight.com',
                username: 'username',
                password: 'password')

        member.save()

        template = new Template(
                name: 'Test Template',
                templateBody: 'Test Body',
                owner: member)

        template.save()
    }

    protected void tearDown() {
        template.delete()
        member.delete()

        super.tearDown();
    }

    void testAddCampaign() {
        def campaign = createTestCampaign()

        assertTrue campaignService.addCampaign(campaign)
        def found = campaignService.getCampaign(campaign.id)

        assertEquals 'Test Name', found.subject
        assertEquals campaign, found
    }

    void testSaveCampaign() {
        def campaign = createTestCampaign()

        assertTrue campaignService.addCampaign(campaign)
        def found = campaignService.getCampaign(campaign.id)

        found.name = 'Test Name 2'
        found.subject = 'Test Subject 2'
        found.description = 'Test Description 2'

        assertTrue campaignService.saveCampaign(found)

        def found2 = campaignService.getCampaign(campaign.id)

        assertEquals found, found2
    }

    void testGetMemberCampaigns() {
        def campaign1 = createTestCampaign()
        def campaign2 = createTestCampaign()
        def campaign3 = createTestCampaign()

        assertTrue campaignService.addCampaign(campaign1)
        assertTrue campaignService.addCampaign(campaign2)
        assertTrue campaignService.addCampaign(campaign3)

        def found = campaignService.getMemberCampaigns(member)

        assertNotNull found
        assertEquals 3, found.size()
        assertTrue found.contains(campaign1)
        assertTrue found.contains(campaign2)
        assertTrue found.contains(campaign3)
    }

    void testCampaignsSorting() {
        def campaign1 = createTestCampaign()
        def campaign2 = createTestCampaign()
        def campaign3 = createTestCampaign()

        campaign1.dateCreated = new Date() - 10
        campaign2.dateCreated = new Date()
        campaign3.dateCreated = new Date() - 5

        assertTrue campaignService.addCampaign(campaign1)
        assertTrue campaignService.addCampaign(campaign2)
        assertTrue campaignService.addCampaign(campaign3)

        def found = campaignService.getMemberCampaigns(member)

        assertEquals campaign2, found[0]
        assertEquals campaign3, found[1]
        assertEquals campaign1, found[2]
    }

    void assertEquals(Campaign campaign, Campaign found) {
        assertNotNull found.dateCreated
        assertEquals CampaignState.New, found.state
        assertEquals campaign.subject, found.subject
        assertEquals campaign.name, found.name
        assertEquals campaign.description, found.description
        assertEquals campaign.owner.id, found.owner.id
        assertEquals campaign.template.id, found.template.id
    }

    Campaign createTestCampaign() {
        Campaign campaign = new Campaign()
        campaign.name = 'Test Name'
        campaign.description = 'Test Description'
        campaign.owner = member
        campaign.template = template
        return campaign
    }
}
