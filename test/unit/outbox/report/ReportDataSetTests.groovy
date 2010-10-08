package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
class ReportDataSetTests extends GroovyTestCase {

    void testConstructors() {
        def dataSet = new ReportDataSet()
        assertNull dataSet.data

        dataSet = new ReportDataSet([10, 20, 30])
        assertEquals 3, dataSet.size()

        dataSet = new ReportDataSet([10, 20, 30] as Set)
        assertEquals 3, dataSet.size()

        dataSet = new ReportDataSet([10, 20, 30] as Object[])
        assertEquals 3, dataSet.size()
    }

    void testAdd() {
        def dataSet = new ReportDataSet()

        dataSet.add 10
        dataSet.add 20
        dataSet.add 30

        assertEquals 3, dataSet.size()
    }

    void testSize() {
        def dataSet = new ReportDataSet([])
        assertEquals 0, dataSet.size()

        dataSet = new ReportDataSet([1])
        assertEquals 1, dataSet.size()

        dataSet = new ReportDataSet()
        assertEquals 0, dataSet.size()
    }
}
