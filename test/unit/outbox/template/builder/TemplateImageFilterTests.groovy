package outbox.template.builder

import grails.test.GrailsUnitTestCase
import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.subscriber.Subscriber

/**
 * @author Ruslan Khmelyuk
 */
class TemplateImageFilterTests extends GrailsUnitTestCase {

    void testFilter() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.template = "<img alt='test' src='logo.png'>MailSight</a>"

        def filter = new TemplateImageFilter()

        def trackingLinkBuilderControl = mockFor(TrackingLinkBuilder)
        trackingLinkBuilderControl.demand.trackingLink { ref -> "'http://tracking/123'" }
        filter.trackingLinkBuilder = trackingLinkBuilderControl.createMock()

        filter.filter context

        trackingLinkBuilderControl.verify()
        assertEquals "<img alt='test' src='http://tracking/123'>MailSight</a>", context.template

        assertEquals 1, context.trackingReferences.size()
        println context.trackingReferences.first()
        assertEquals "'logo.png'", context.trackingReferences.first().reference
    }
}
