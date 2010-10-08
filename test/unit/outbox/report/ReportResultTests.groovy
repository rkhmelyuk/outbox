package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
class ReportResultTests extends GroovyTestCase {

    void testSingles() {
        def result = new ReportResult()

        result.addSingle('test1', 10)
        result.addSingle('test2', false)

        assertEquals 10, result.single('test1')
        assertEquals false, result.single('test2')
    }

    void testDataSets() {
        def result = new ReportResult()

        def dataSet1 = new ReportDataSet()
        def dataSet2 = new ReportDataSet()

        result.addDataSet('test1', dataSet1)
        result.addDataSet('test2', dataSet2)

        assertEquals dataSet1, result.dataSet('test1')
        assertEquals dataSet2, result.dataSet('test2')
    }
}
