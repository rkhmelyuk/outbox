package outbox.report.extractor

import outbox.AppException
import outbox.report.ReportResult
import outbox.report.metadata.Parameter

/**
 * Extracts report data and returns them as data set.
 *
 * @author Ruslan Khmelyuk
 */
public interface ReportExtractor {

    /**
     * Gets a set of parameters.
     * @return a set of parameters.
     */
    List<Parameter> getParameters()

    /**
     * Returns report results.
     * @param context the report extractor context.
     * @return never null.
     */
    ReportResult extract(Map context) throws AppException
}
