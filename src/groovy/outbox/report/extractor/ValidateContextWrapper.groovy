package outbox.report.extractor

import outbox.report.Period
import outbox.report.ReportResult
import outbox.report.metadata.Parameter
import outbox.report.metadata.ParameterType

/**
 * Use this wrapper to validate context and it's parameters.
 * @author Ruslan Khmelyuk
 */
class ValidateContextWrapper implements ReportExtractor {

    ReportExtractor extractor

    def ValidateContextWrapper(extractor) {
        this.extractor = extractor
    }

    List<Parameter> getParameters() {
        extractor.parameters
    }

    ReportResult extract(Map context) {
        validateInput(context)
        extractor.extract(context)
    }

    def validateInput(Map context) {
        def parameters = getParameters()
        parameters?.each { param ->
            def value = context[param.name]
            if (param.required && !value) {
                throw new WrongContextException("Parameter ${param.name} is required.")
            }
            else if (value) {
                switch (param.type) {
                    case ParameterType.Integer:
                        if (!(value instanceof Integer) && !(value instanceof Long) &&
                                !(value instanceof Short) && !(value instanceof BigInteger)) {
                            throw new WrongContextException("Parameter ${param.name} must be an integer number.")
                        }
                        break
                    case ParameterType.Float:
                        if (!(value instanceof Float) && !(value instanceof Double) &&
                                !(value instanceof BigDecimal)) {
                            throw new WrongContextException("Parameter ${param.name} must be a float number.")
                        }
                        break
                    case ParameterType.Period:
                        if (!(value instanceof Period)) {
                            throw new WrongContextException("Parameter ${param.name} must be a Period.")
                        }
                        break
                    case ParameterType.Boolean:
                        if (!(value instanceof Boolean)) {
                            throw new WrongContextException("Parameter ${param.name} must be a boolean value.")
                        }
                        break
                    case ParameterType.Date:
                        if (!(value instanceof Date)) {
                            throw new WrongContextException("Parameter ${param.name} must be a Date.")
                        }
                        break
                }
            }
        }
    }

}
