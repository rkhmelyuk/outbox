package outbox.report.campaign

import grails.test.GrailsUnitTestCase
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.ParameterType
import outbox.tracking.TrackingInfo

/**
 * @author Ruslan Khmelyuk
 */
class TotalClicksExtractorTests extends GrailsUnitTestCase {

    def sessionFactory
    ReportExtractor extractor

    @Override protected void setUp() {
        super.setUp()

        extractor = new TotalClicksExtractor()
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
        createTrackingInfo(new Date() - 1, true)
        createTrackingInfo(new Date() - 2, true)
        createTrackingInfo(new Date() - 1, true)
        createTrackingInfo(new Date() - 1, true)
        createTrackingInfo(new Date(), false)

        assertEquals 5, TrackingInfo.count()

        def result = extractor.extract([campaignId: 1])
        assertEquals 4, result.single('clicks')
    }

    void testWithDataAndDateRange() {
        def date = new Date()
        createTrackingInfo(date - 5, true)
        createTrackingInfo(date - 4, true)
        createTrackingInfo(date - 3, true)
        createTrackingInfo(date - 2, true)
        createTrackingInfo(date - 1, false)

        assertEquals 5, TrackingInfo.count()

        def result = extractor.extract([campaignId: 1, startDate: date - 3, endDate: null])
        assertEquals 2, result.single('clicks')
    }

    void testWithoutData() {
        def result = extractor.extract([campaignId: 1])
        assertEquals 0, result.single('clicks')
    }

    void createTrackingInfo(Date date, boolean click) {
        def trackingInfo = new TrackingInfo()
        trackingInfo.campaignId = 1
        trackingInfo.subscriberId = 'abcdef0192'
        trackingInfo.datetime = date
        trackingInfo.trackingReferenceId = 'bcasd123123'
        trackingInfo.click = click

        assertNotNull trackingInfo.save(flush: true)
    }
}
