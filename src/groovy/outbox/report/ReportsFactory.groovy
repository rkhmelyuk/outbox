package outbox.report

import org.hibernate.SessionFactory
import outbox.report.campaign.ClicksByDateExtractor
import outbox.report.campaign.OpensByDateExtractor
import outbox.report.campaign.TotalClicksExtractor
import outbox.report.campaign.TotalOpensExtractor

/**
 * @author Ruslan Khmelyuk
 */
class ReportsFactory {

    SessionFactory sessionFactory

    Report totalClicksReport() {
        def extractor = new TotalClicksExtractor(sessionFactory: sessionFactory)
        new Report(name: 'totalClicks', extractor: extractor)
    }

    Report totalOpensReport() {
        def extractor = new TotalOpensExtractor(sessionFactory: sessionFactory)
        new Report(name: 'totalOpens', extractor: extractor)
    }

    Report clicksByDateReport() {
        def extractor = new ClicksByDateExtractor(sessionFactory: sessionFactory)
        new Report(name: 'clicksByDate', extractor: extractor)
    }

    Report opensByDateReport() {
        def extractor = new OpensByDateExtractor(sessionFactory: sessionFactory)
        new Report(name: 'opensByDate', extractor: extractor)
    }
}
