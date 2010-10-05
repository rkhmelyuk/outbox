package outbox.tracking

import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.campaign.CampaignService
import outbox.member.Member
import outbox.subscriber.Subscriber
import outbox.subscriber.SubscriberService

/**
 * @author Ruslan Khmelyuk
 */
class TrackingServiceTests extends GroovyTestCase {

    CampaignService campaignService
    SubscriberService subscriberService
    TrackingService trackingService

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

    void testAddTrackingReference() {
        def reference = createTestTrackingReference(1)
        assertTrue trackingService.addTrackingReference(reference)
        def found = trackingService.getTrackingReference(reference.id)

        assertEquals(reference, found)
    }

    void testAddTrackingReferences() {
        def reference1 = createTestTrackingReference(1)
        def reference2 = createTestTrackingReference(2)
        def reference3 = createTestTrackingReference(3)

        assertTrue trackingService.addTrackingReference(reference1)
        assertTrue trackingService.addTrackingReference(reference2)
        assertTrue trackingService.addTrackingReference(reference3)

        def found1 = trackingService.getTrackingReference(reference1.id)
        def found2 = trackingService.getTrackingReference(reference2.id)
        def found3 = trackingService.getTrackingReference(reference3.id)

        assertEquals reference1, found1
        assertEquals reference2, found2
        assertEquals reference3, found3
    }

    void testTrack() {
        def rawTrackingInfo = new RawTrackingInfo()
        rawTrackingInfo.userAgentHeader = 'Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8'
        rawTrackingInfo.remoteAddress = '192.12.93.122'
        rawTrackingInfo.remoteHost = 'core'
        rawTrackingInfo.remoteUser = 'guest'
        rawTrackingInfo.reference = new TrackingReference(id: '3', campaignId: 1,
                subscriberId: 'abcdef00', type: TrackingReferenceType.Link)
        rawTrackingInfo.acceptLanguageHeader = 'en-US'

        rawTrackingInfo.reference.generateId()
        trackingService.track rawTrackingInfo

        def trackingInfo = TrackingInfo.list()

        assertNotNull trackingInfo
        assertEquals 1, trackingInfo.size()
    }

    private def assertEquals(TrackingReference reference, TrackingReference found) {
        assertEquals reference.campaignId, found.campaignId
        assertEquals reference.subscriberId, found.subscriberId
        assertEquals reference.campaignMessageId, found.campaignMessageId
        assertEquals reference.reference, found.reference
        assertEquals reference.type, found.type
    }

    TrackingReference createTestTrackingReference(id) {
        def campaign = createTestCampaign()
        def subscriber = createTestSubscriber(id)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriberService.saveSubscriber(subscriber)

        def message = new CampaignMessage(campaign: campaign, subscriber: subscriber,
                sendDate: new Date(), email: 'test@mailsight.com')

        campaignService.addCampaignMessages([message])

        def reference = new TrackingReference()
        reference.campaignId = campaign.id
        reference.subscriberId = subscriber.id
        reference.campaignMessageId = message.id
        reference.reference = 'htp://google.com'
        reference.type = TrackingReferenceType.Link

        return reference
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
