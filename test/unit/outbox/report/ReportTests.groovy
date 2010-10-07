package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
class ReportTests extends GroovyTestCase {

    void testFields() {
        def report = new Report()
        report.name = 'Test Name'
        report.extractor = null

        assertEquals 'Test Name', report.name
        assertNull report.extractor
    }
}
