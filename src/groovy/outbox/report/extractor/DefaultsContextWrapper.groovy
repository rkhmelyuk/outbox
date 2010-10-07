package outbox.report.extractor

import outbox.report.ReportResult
import outbox.report.metadata.Parameter

/**
 * Setup default values if context is empty.
 *
 * @author Ruslan Khmelyuk
 */
class DefaultsContextWrapper implements ReportExtractor {

    ReportExtractor extractor
    Map defaults

    def DefaultsContextWrapper(ReportExtractor extractor, Map defaults) {
        this.extractor = extractor
        this.defaults = defaults
    }

    List<Parameter> getParameters() {
        extractor.parameters
    }

    ReportResult extract(Map context) {
        def myContext = new HashMap(context)
        defaults.each { key, value ->
            if (myContext[key] == null) {
                myContext[key] = value
            }
        }
        extractor.extract(myContext)
    }
}
