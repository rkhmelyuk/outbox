package outbox.report.campaign

import org.hibernate.SessionFactory
import outbox.report.ReportResult
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.Parameter
import outbox.report.metadata.ParameterType

/**
 * @author Ruslan Khmelyuk
 */
class TotalSubscribersExtractor implements ReportExtractor {

    SessionFactory sessionFactory

    List<Parameter> getParameters() {
        [new Parameter(name: 'campaignId', type: ParameterType.Integer, required: true)]
    }

    ReportResult extract(Map context) {

        def session = sessionFactory.currentSession
        def query = session.getNamedQuery('Report.totalSubscribers')

        query.setParameter('campaignId', context.campaignId)

        def queryResult = query.uniqueResult()
        def reportResult = new ReportResult()

        reportResult.addSingle('number', queryResult ?: 0)

        return reportResult
    }

}
