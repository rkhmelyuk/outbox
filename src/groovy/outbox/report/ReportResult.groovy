package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
class ReportResult {

    Map<String, ReportDataSet> dataSets = [:]
    Map<String, Object> singles = [:]

    void addDataSet(String name, ReportDataSet dataSet) {
        dataSets.put(name, dataSet)
    }

    void addSingle(String name, Object value) {
        singles.put(name, value)
    }

    ReportDataSet dataSet(String name) {
        dataSets.get(name)
    }

    Object single(String name) {
        singles.get(name)
    }

    Set<String> getDataSets() {
        dataSets.keySet()
    }

    Set<String> getSingles() {
        singles.keySet()
    }

}
