package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
class ReportUtilTests extends GroovyTestCase {

    void testBestPeriod() {
        def date = new Date()

        assertEquals Period.Hour, ReportUtil.bestPeriod(null, null)
        assertEquals Period.Hour, ReportUtil.bestPeriod(date - 1, date)
        assertEquals Period.Hour, ReportUtil.bestPeriod(date - 1, null)
        assertEquals Period.Day, ReportUtil.bestPeriod(date - 5, date)
        assertEquals Period.Day, ReportUtil.bestPeriod(date - 21, date)
        assertEquals Period.Week, ReportUtil.bestPeriod(date - 50, date)
        assertEquals Period.Month, ReportUtil.bestPeriod(date - 60, date)
        assertEquals Period.Month, ReportUtil.bestPeriod(date - 140, date)
        assertEquals Period.Month, ReportUtil.bestPeriod(date - 440, date)
        assertEquals Period.Year, ReportUtil.bestPeriod(date - 1440, date)
    }
}
