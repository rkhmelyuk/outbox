package outbox.report

import org.hibernate.SessionFactory
import outbox.report.extractor.DefaultReportExtractorWrapper
import outbox.report.campaign.*

/**
 * @author Ruslan Khmelyuk
 */
class ReportsFactory {

    SessionFactory sessionFactory

    Report totalClicksReport() {
        def extractor = new TotalClicksExtractor(sessionFactory: sessionFactory)
        extractor = new DefaultReportExtractorWrapper(extractor)
        new Report(name: 'totalClicks', extractor: extractor)
    }

    Report totalOpensReport() {
        def extractor = new TotalOpensExtractor(sessionFactory: sessionFactory)
        extractor = new DefaultReportExtractorWrapper(extractor)
        new Report(name: 'totalOpens', extractor: extractor)
    }

    Report clicksByDateReport() {
        def extractor = new ClicksByDateExtractor(sessionFactory: sessionFactory)
        extractor = new DefaultReportExtractorWrapper(extractor, [period: Period.Day])
        new Report(name: 'clicksByDate', extractor: extractor)
    }

    Report opensByDateReport() {
        def extractor = new OpensByDateExtractor(sessionFactory: sessionFactory)
        extractor = new DefaultReportExtractorWrapper(extractor, [period: Period.Day])
        new Report(name: 'opensByDate', extractor: extractor)
    }

    Report openedReport() {
        def extractor = new OpenedExtractor(sessionFactory: sessionFactory)
        extractor = new DefaultReportExtractorWrapper(extractor)
        new Report(name: 'opened', extractor: extractor)
    }

    Report totalSubscribersReport() {
        def extractor = new TotalSubscribersExtractor(sessionFactory: sessionFactory)
        extractor = new DefaultReportExtractorWrapper(extractor)
        new Report(name: 'totalSubscribers', extractor: extractor)
    }

}
