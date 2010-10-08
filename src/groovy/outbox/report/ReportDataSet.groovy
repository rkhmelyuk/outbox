package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
class ReportDataSet {

    List data

    ReportDataSet() {
    }

    ReportDataSet(List data) {
        this.data = data
    }

    ReportDataSet(Set data) {
        this.data = data as List
    }

    ReportDataSet(Object[] data) {
        this.data = data as List
    }

    void add(row) {
        if (!data) {
            data = []
        }
        data << row
    }

    int size() {
        data ? data.size() : 0
    }

}
