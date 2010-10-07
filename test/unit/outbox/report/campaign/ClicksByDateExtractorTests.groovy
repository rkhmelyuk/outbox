package outbox.report.campaign

import grails.test.GrailsUnitTestCase
import outbox.report.campaign.clicksbydate.ClicksByDateExtractor
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.ParameterType

/**
 * @author Ruslan Khmelyuk
 */
class ClicksByDateExtractorTests extends GrailsUnitTestCase {

    ReportExtractor extractor

    @Override protected void setUp() {
        super.setUp()

        extractor = new ClicksByDateExtractor()
    }

    void testParameters() {
        def params = extractor.parameters

        assertEquals 4, params.size()

        def campaign = params.find { it.name == 'campaignId' }
        assertNotNull campaign
        assertEquals ParameterType.Integer, campaign.type
        assertTrue campaign.required

        def startDate = params.find { it.name == 'startDate'}
        assertNotNull startDate
        assertEquals ParameterType.Date, startDate.type
        assertFalse startDate.required

        def endDate = params.find { it.name == 'endDate'}
        assertNotNull endDate
        assertEquals ParameterType.Date, endDate.type
        assertFalse endDate.required
        
        def period = params.find { it.name == 'period'}
        assertNotNull period
        assertEquals ParameterType.Period, period.type
        assertFalse period.required
    }
}
