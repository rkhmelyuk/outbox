package outbox.report

import outbox.report.metadata.Parameter
import outbox.report.metadata.ParameterType

/**
 * @author Ruslan Khmelyuk
 */
class ParameterTests extends GroovyTestCase {

    void testParameter() {
        def parameter = new Parameter()
        parameter.name = 'test'
        parameter.type= ParameterType.Date
        parameter.required = true

        assertEquals 'test', parameter.name
        assertEquals ParameterType.Date, parameter.type
        assertTrue parameter.required
    }
}
