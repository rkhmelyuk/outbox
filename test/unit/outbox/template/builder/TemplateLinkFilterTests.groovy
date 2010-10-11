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

    void testFilter_OnlyLinks() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.template = "<a href='http://google.com'>http://google.com</a><a href='http://doodle.com'>http://doodle.com</a>"

        def filter = new TemplateLinkFilter()

        def trackingLinkBuilderControl = mockFor(TrackingLinkBuilder)
        trackingLinkBuilderControl.demand.trackingLink(2..2) { ref -> 'http://tracking/123' }
        filter.trackingLinkBuilder = trackingLinkBuilderControl.createMock()

        filter.filter context

        trackingLinkBuilderControl.verify()
        assertEquals "<a href='http://tracking/123'>http://google.com</a><a href='http://tracking/123'>http://doodle.com</a>",
                context.template

        assertEquals 2, context.trackingReferences.size()
        assertEquals 'http://google.com', context.trackingReferences[0].reference
        assertEquals 'http://doodle.com', context.trackingReferences[1].reference
    }
}
