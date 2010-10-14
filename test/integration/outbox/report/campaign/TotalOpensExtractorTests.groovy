package outbox.report.campaign

import grails.test.GrailsUnitTestCase
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.ParameterType
import outbox.tracking.TrackingInfo

/**
 * @author Ruslan Khmelyuk
 */
class TotalOpensExtractorTests extends GrailsUnitTestCase {

    def sessionFactory
    ReportExtractor extractor

    @Override protected void setUp() {
        super.setUp()

        extractor = new TotalOpensExtractor()
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
        createTrackingInfo(new Date() - 1, false, true)
        createTrackingInfo(new Date() - 2, false, true)
        createTrackingInfo(new Date() - 1, false, true)
        createTrackingInfo(new Date() - 1, false, true)
        createTrackingInfo(new Date() - 1, false, false)

        assertEquals 5, TrackingInfo.count()

        def result = extractor.extract([campaignId: 1])
        assertEquals 4, result.single('opens')
    }

    void testWithDataAndDateRange() {
        def date = new Date()
        createTrackingInfo(date - 5, false, true)
        createTrackingInfo(date - 4, false, true)
        createTrackingInfo(date - 3, false, true)
        createTrackingInfo(date - 2, false, true)
        createTrackingInfo(date - 1, false, false)

        assertEquals 5, TrackingInfo.count()

        def result = extractor.extract([campaignId: 1, startDate: date - 5, endDate: date - 3])
        assertEquals 3, result.single('opens')
    }

    void testWithoutData() {
        def result = extractor.extract([campaignId: 1])
        assertEquals 0, result.single('opens')
    }

    void createTrackingInfo(Date date, boolean click, boolean open) {
        def trackingInfo = new TrackingInfo()
        trackingInfo.campaignId = 1
        trackingInfo.subscriberId = 'abcdef0192'
        trackingInfo.datetime = date
        trackingInfo.trackingReferenceId = 'bcasd123123'
        trackingInfo.click = click
        trackingInfo.open = open

        assertNotNull trackingInfo.save(flush: true)
    }
}
