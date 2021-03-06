package outbox.report.campaign

import grails.test.GrailsUnitTestCase
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.ParameterType
import outbox.tracking.TrackingInfo

/**
 * @author Ruslan Khmelyuk
 */
class OpenedExtractorTest extends GrailsUnitTestCase {

    def sessionFactory
    ReportExtractor extractor

    @Override protected void setUp() {
        super.setUp()

        extractor = new OpenedExtractor()
        extractor.sessionFactory = sessionFactory
    }

    void testParameters() {
        def params = extractor.parameters

        assertEquals 3, params.size()

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
    }

    void testWithData() {
        createTrackingInfo(new Date() - 1, false, true, 1)
        createTrackingInfo(new Date() - 2, false, true, 2)
        createTrackingInfo(new Date() - 1, false, true, 1)
        createTrackingInfo(new Date() - 1, false, true, 3)
        createTrackingInfo(new Date() - 1, false, false, 3)

        assertEquals 5, TrackingInfo.count()

        def result = extractor.extract([campaignId: 1])
        assertEquals 3, result.single('number')
    }

    void testWithoutData() {
        def result = extractor.extract([campaignId: 1])
        assertEquals 0, result.single('number')
    }

    void createTrackingInfo(Date date, boolean click, boolean open, id) {
        def trackingInfo = new TrackingInfo()
        trackingInfo.campaignId = 1
        trackingInfo.subscriberId = 'abcdef0192' + id
        trackingInfo.datetime = date
        trackingInfo.trackingReferenceId = 'bcasd123123'
        trackingInfo.click = click
        trackingInfo.open = open

        assertNotNull trackingInfo.save(flush: true)
    }
}
