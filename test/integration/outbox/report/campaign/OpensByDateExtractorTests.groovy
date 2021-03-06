package outbox.report.campaign

import grails.test.GrailsUnitTestCase
import org.hibernate.SessionFactory
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.ParameterType
import outbox.tracking.TrackingInfo

/**
 * @author Ruslan Khmelyuk
 */
class OpensByDateExtractorTests extends GrailsUnitTestCase {

    ReportExtractor extractor
    SessionFactory sessionFactory

    @Override protected void setUp() {
        super.setUp()

        extractor = new OpensByDateExtractor()
        extractor.sessionFactory = sessionFactory
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

    void testWithData() {
        createTrackingInfo(new Date() - 1, false, true)
        createTrackingInfo(new Date() - 2, false, true)
        createTrackingInfo(new Date() - 1, false, true)
        createTrackingInfo(new Date() - 1, false, true)
        createTrackingInfo(new Date() - 2, false, true)
        createTrackingInfo(new Date() - 2, false, false)

        assertEquals 6, TrackingInfo.count()

        def result = extractor.extract([campaignId: 1])
        def set = result.dataSet('opens')
        assertEquals 2, set.data.size()

        def date = new Date()
        date.seconds = 0
        date.minutes = 0
        date.hours = 0

        set.list().each {
            if (it.date == date - 1) {
                assertEquals 3, it.opens
            }
            else if (it.date == date - 2) {
                assertEquals 2, it.opens
            }
        }
    }

    void testWithDataAndDateCondition() {
        def date = new Date()

        createTrackingInfo(date - 6, false, true)
        createTrackingInfo(date - 5, false, true)
        createTrackingInfo(date - 4, false, true)
        createTrackingInfo(date - 3, false, true)
        createTrackingInfo(date - 2, false, true)
        createTrackingInfo(date - 1, false, false)

        assertEquals 6, TrackingInfo.count()

        def result = extractor.extract([campaignId: 1, startDate: date - 5, endDate: date - 2])
        def set = result.dataSet('opens')
        assertEquals 4, set.data.size()
    }

    void testWithoutData() {
        def result = extractor.extract([campaignId: 1])
        def set = result.dataSet('opens')
        assertEquals 0, set.size()
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
