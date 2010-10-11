package outbox.report.campaign

import grails.test.GrailsUnitTestCase
import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.campaign.CampaignService
import outbox.member.Member
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.ParameterType
import outbox.subscriber.Subscriber
import outbox.tracking.TrackingService

/**
 * @author Ruslan Khmelyuk
 */
class TotalSubscribersExtractorTests extends GrailsUnitTestCase {

    def member
    def sessionFactory

    ReportExtractor extractor
    CampaignService campaignService
    TrackingService trackingService

    @Override protected void setUp() {
        super.setUp()

        extractor = new TotalSubscribersExtractor()
        extractor.sessionFactory = sessionFactory

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

    void testParameters() {
        def params = extractor.parameters

        assertEquals 1, params.size()

        def campaign = params.find { it.name == 'campaignId' }
        assertNotNull campaign
        assertEquals ParameterType.Integer, campaign.type
        assertTrue campaign.required
    }

    void testWithData() {
        def campaign1 = createTestCampaign()
        def campaign2 = createTestCampaign()

        assertTrue campaignService.addCampaign(campaign1)
        assertTrue campaignService.addCampaign(campaign2)

        createCampaignMessage 1, campaign1
        createCampaignMessage 2, campaign1
        createCampaignMessage 3, campaign1
        createCampaignMessage 4, campaign2

        def result = extractor.extract([campaignId: campaign1.id])
        assertEquals 3, result.single('number')

        result = extractor.extract([campaignId: campaign2.id])
        assertEquals 1, result.single('number')
    }

    void testWithoutData() {
        def result = extractor.extract([campaignId: 1])
        assertEquals 0, result.single('number')
    }

    void createCampaignMessage(int id, Campaign campaign) {
        def campaignMessage = new CampaignMessage()
        campaignMessage.campaign = campaign
        campaignMessage.subscriber = createTestSubscriber(id).save()
        campaignMessage.email = 'test@mailsight.com'
        campaignMessage.sendDate = new Date()

        assertTrue campaignService.addCampaignMessages([campaignMessage])
    }

    Subscriber createTestSubscriber(id) {
        Subscriber subscriber = new Subscriber()
        subscriber.firstName = 'John'
        subscriber.lastName = 'Doe'
        subscriber.email = 'john.doe' + id + '@nowhere.com'
        subscriber.gender = null
        subscriber.timezone = null
        subscriber.language = null
        subscriber.namePrefix = null
        subscriber.enabled = true
        subscriber.member = member
        return subscriber
    }

    Campaign createTestCampaign() {
        Campaign campaign = new Campaign()
        campaign.name = 'Test Name'
        campaign.description = 'Test Description'
        campaign.owner = member
        return campaign
    }
}
