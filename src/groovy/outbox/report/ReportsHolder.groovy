package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
class ReportsHolder {

    ReportsFactory reportsFactory
    Map<String, Report> reports = [:]

    void init() {
        register(reportsFactory.opensByDateReport())
        register(reportsFactory.clicksByDateReport())
        register(reportsFactory.totalOpensReport())
        register(reportsFactory.totalClicksReport())
    }

    synchronized void register(Report report) {
        if (report) {
            reports.put(report.name, report)
        }
    }

    Report getReport(String name) {
        reports.get(name)
    }
}
