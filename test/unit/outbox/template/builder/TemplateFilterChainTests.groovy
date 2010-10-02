package outbox.template.builder

import grails.test.GrailsUnitTestCase
import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.subscriber.Subscriber

/**
 * @author Ruslan Khmelyuk
 * @since  2010-10-02
 */
class TemplateFilterChainTests extends GrailsUnitTestCase {

    void testFilter() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))
        
        def filter1Control = mockFor(TemplateFilter)
        def filter2Control = mockFor(TemplateFilter)
        filter1Control.demand.filter { _context -> assertEquals context, _context }
        filter2Control.demand.filter { _context -> assertEquals context, _context }

        def chain = new TemplateFilterChain()
        chain.add filter1Control.createMock()
        chain.add filter2Control.createMock()

        chain.filter context

        filter1Control.verify()
        filter2Control.verify()
    }

    void testFilter_NoFilters() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        try {
            def chain = new TemplateFilterChain()
            chain.filter context
        }
        catch (Exception e) {
            fail 'failed if no filter is set'
        }
    }
}
