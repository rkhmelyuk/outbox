package outbox.report.campaign.clicksbydate

import outbox.report.ReportResult
import outbox.report.extractor.ReportExtractor
import outbox.report.metadata.Parameter
import outbox.report.metadata.ParameterType

/**
 * @author Ruslan Khmelyuk
 */
class ClicksByDateExtractor implements ReportExtractor {

    List<Parameter> getParameters() {
        return [
                new Parameter(name: 'campaignId', type: ParameterType.Integer, required: true),
                new Parameter(name: 'startDate', type: ParameterType.Date, required: false),
                new Parameter(name: 'endDate', type: ParameterType.Date, required: false),
                new Parameter(name: 'period', type: ParameterType.Period, required: false),
        ]
    }

    ReportResult extract(Map context) {

        def campaignId = context.campaignId
        def startDate = context.startDate
        def endDate = context.endDate
        def period = context.period

        def reportResult = new ReportResult()

        return null
    }

}
