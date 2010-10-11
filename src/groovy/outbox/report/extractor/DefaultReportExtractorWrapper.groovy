package outbox.report.extractor

import outbox.report.ReportResult
import outbox.report.metadata.Parameter

/**
 * Wraps extractor with a default set of wrappers.
 *
 * @author Ruslan Khmelyuk
 */
class DefaultReportExtractorWrapper implements ReportExtractor {

    ReportExtractor extractor

    DefaultReportExtractorWrapper(ReportExtractor extractor, Map defaults = null) {
        def wrapped = new ValidateContextWrapper(extractor)
        if (defaults) {
            wrapped = new DefaultsContextWrapper(extractor, defaults)
        }
        this.extractor = wrapped
    }

    List<Parameter> getParameters() {
        extractor.parameters
    }

    ReportResult extract(Map context) {
        extractor.extract(context)
    }

}
