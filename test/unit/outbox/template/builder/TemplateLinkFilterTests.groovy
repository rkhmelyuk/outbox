package outbox.template.builder

import grails.test.GrailsUnitTestCase
import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.subscriber.Subscriber

/**
 * @author Ruslan Khmelyuk
 */
class TemplateLinkFilterTests extends GrailsUnitTestCase {

    void testFilter() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.template = "<a href='http://google.com'>Link</a>"

        def filter = new TemplateLinkFilter()

        def trackingLinkBuilderControl = mockFor(TrackingLinkBuilder)
        trackingLinkBuilderControl.demand.trackingLink { ref -> 'http://tracking/123' }
        filter.trackingLinkBuilder = trackingLinkBuilderControl.createMock()

        filter.filter context

        trackingLinkBuilderControl.verify()
        assertEquals "<a href='http://tracking/123'>Link</a>", context.template

        assertEquals 1, context.trackingReferences.size() 
        assertEquals "http://google.com", context.trackingReferences.first().reference
    }
}
