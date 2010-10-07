package outbox.report

import outbox.report.extractor.ReportExtractor

/**
 * @author Ruslan Khmelyuk
 */
class Report {

    String name
    ReportExtractor extractor

    public String toString ( ) {
        "Report[name=$name]"
    }
}
