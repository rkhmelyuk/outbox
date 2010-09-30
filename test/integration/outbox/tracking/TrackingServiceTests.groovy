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
        def reference = createTestTrackingReference()
        assertTrue trackingService.addTrackingReference(reference)
        def found = trackingService.getTrackingReference(reference.id)

        assertEquals reference.campaignId, found.campaignId
        assertEquals reference.subscriberId, found.subscriberId
        assertEquals reference.campaignMessageId, found.campaignMessageId
        assertEquals reference.reference, found.reference
        assertEquals reference.type, found.type
    }

    TrackingReference createTestTrackingReference() {
        def campaign = createTestCampaign()
        def subscriber = createTestSubscriber(1)

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
