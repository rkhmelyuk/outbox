package outbox.report.campaign

import java.text.MessageFormat
import org.hibernate.SessionFactory
import outbox.ValueUtil
import outbox.report.Period
import outbox.report.ReportDataSet
import outbox.report.ReportResult
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.Parameter
import outbox.report.metadata.ParameterType

/**
 * @author Ruslan Khmelyuk
 */
class ClicksByDateExtractor implements ReportExtractor {

    SessionFactory sessionFactory

    List<Parameter> getParameters() {
        return [
                new Parameter(name: 'campaignId', type: ParameterType.Integer, required: true),
                new Parameter(name: 'startDate', type: ParameterType.Date, required: false),
                new Parameter(name: 'endDate', type: ParameterType.Date, required: false),
                new Parameter(name: 'period', type: ParameterType.Period, required: false),
        ]
    }

    ReportResult extract(Map context) {

        def period = context.period ?: Period.Day

        def session = sessionFactory.currentSession
        def query = session.getNamedQuery('Report.clicksByDate')
        def queryString = MessageFormat.format(query.queryString, period.sqlPeriod)
        query = session.createSQLQuery(queryString)

        query.setParameter('campaignId', context.campaignId)
        query.setParameter('startDate', ValueUtil.beginDate(context.startDate))
        query.setParameter('endDate', ValueUtil.endDate(context.endDate))

        def queryResult = query.list()

        def results = queryResult.collect { [date: it[0], clicks: it[1]]}

        def reportResult = new ReportResult()
        reportResult.addDataSet('clicks', new ReportDataSet(results))

        return reportResult
    }

}
