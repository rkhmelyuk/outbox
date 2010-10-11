package outbox.report.campaign

import org.hibernate.SessionFactory
import outbox.ValueUtil
import outbox.report.ReportResult
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.Parameter
import outbox.report.metadata.ParameterType

/**
 * @author Ruslan Khmelyuk
 */
class OpenedExtractor implements ReportExtractor {

    SessionFactory sessionFactory

    List<Parameter> getParameters() {
        return [
                new Parameter(name: 'campaignId', type: ParameterType.Integer, required: true),
                new Parameter(name: 'startDate', type: ParameterType.Date, required: false),
                new Parameter(name: 'endDate', type: ParameterType.Date, required: false)
        ]
    }

    ReportResult extract(Map context) {

        def session = sessionFactory.currentSession
        def query = session.getNamedQuery('Report.opened')

        query.setParameter('campaignId', context.campaignId)
        query.setParameter('startDate', ValueUtil.beginDate(context.startDate))
        query.setParameter('endDate', ValueUtil.endDate(context.endDate))

        def queryResult = query.uniqueResult()
        def reportResult = new ReportResult()

        reportResult.addSingle('number', queryResult ?: 0)

        return reportResult
    }

}
