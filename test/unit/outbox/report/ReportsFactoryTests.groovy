package outbox.report

import outbox.report.extractor.DefaultReportExtractorWrapper

/**
 * @author Ruslan Khmelyuk
 */
class ReportsFactoryTests extends GroovyTestCase {

    ReportsFactory factory

    @Override protected void setUp() {
        super.setUp()
        factory = new ReportsFactory()
    }

    void testTotalClicksReport() {
        def report = factory.totalClicksReport()
        assertEquals 'totalClicks', report.name
        assertTrue report.extractor instanceof DefaultReportExtractorWrapper
    }

    void testTotalOpensReport() {
        def report = factory.totalOpensReport()
        assertEquals 'totalOpens', report.name
        assertTrue report.extractor instanceof DefaultReportExtractorWrapper
    }

    void testClicksByDateReport() {
        def report = factory.clicksByDateReport()
        assertEquals 'clicksByDate', report.name
        assertTrue report.extractor instanceof DefaultReportExtractorWrapper
    }

    void testOpensByDateReport() {
        def report = factory.opensByDateReport()
        assertEquals 'opensByDate', report.name
        assertTrue report.extractor instanceof DefaultReportExtractorWrapper
    }
}
