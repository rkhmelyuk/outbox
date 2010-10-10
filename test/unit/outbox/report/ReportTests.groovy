package outbox.report

import grails.test.GrailsUnitTestCase
import outbox.report.extractor.ReportExtractor

/**
 * @author Ruslan Khmelyuk
 */
class ReportTests extends GrailsUnitTestCase {

    void testFields() {
        def report = new Report()
        report.name = 'Test Name'
        report.extractor = null

        assertEquals 'Test Name', report.name
        assertNull report.extractor
    }

    void testExtract() {
        def reportResult = new ReportResult()
        def reportExtractorControl = mockFor(ReportExtractor)
        reportExtractorControl.demand.extract { context ->
            assertEquals 'test1', context.param1
            assertEquals 'test2', context.param2
            return reportResult
        }

        def report = new Report()
        report.name = 'clicksByDay'
        report.extractor = reportExtractorControl.createMock()

        assertEquals reportResult, report.extract([param1: 'test1', param2: 'test2'])

        reportExtractorControl.verify()
    }
}
