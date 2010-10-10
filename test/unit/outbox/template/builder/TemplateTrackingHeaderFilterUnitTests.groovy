package outbox.template.builder

import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.subscriber.Subscriber

/**
 * @author Ruslan Khmelyuk
 */
class TemplateTrackingHeaderFilterUnitTests extends GroovyTestCase {

    void testFilter() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.template = 'test'

        def filter = new TemplateTrackingHeaderFilter()
        filter.trackingHeader = '[HEADER]'
        filter.filter context

        assertEquals '[HEADER]test', context.template
    }

    void testFilter_Body() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.template = '<body>test</body>'

        def filter = new TemplateTrackingHeaderFilter()
        filter.trackingHeader = '[HEADER]'
        filter.filter context

        assertEquals '<body>test[HEADER]</body>', context.template
    }

    void testFilter_Html() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.template = '<html>test</html>'

        def filter = new TemplateTrackingHeaderFilter()
        filter.trackingHeader = '[HEADER]'
        filter.filter context

        assertEquals '<html>test[HEADER]</html>', context.template
    }

}
