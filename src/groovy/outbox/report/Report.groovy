package outbox.report

import outbox.report.extractor.ReportExtractor

/**
 * @author Ruslan Khmelyuk
 */
class Report {

    String name
    ReportExtractor extractor

    ReportResult extract(Map context) {
        extractor.extract context
    }

    public String toString ( ) {
        "Report[name=$name]"
    }
}
