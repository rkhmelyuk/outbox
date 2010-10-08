package outbox.report.extractor

import grails.test.GrailsUnitTestCase
import outbox.report.ReportResult

/**
 * @author Ruslan Khmelyuk
 */
class DefaultsContextWrapperTests extends GrailsUnitTestCase {

    void testDefaults() {
        def reportResult = new ReportResult()

        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.extract { context ->
            assertEquals 'default1', context.param1
            assertEquals 'test', context.param2
            assertEquals '', context.param3
            return reportResult 
        }
         
        def wrapper = new DefaultsContextWrapper(extractorControl.createMock(),
                [param1: 'default1', param2: 'default2', param3: 'default3'])

        assertEquals reportResult, wrapper.extract([param2: 'test', param3: ''])

        extractorControl.verify()
    }

    void testDefaults_AllSet() {
        def reportResult = new ReportResult()

        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.extract { context ->
            assertEquals 'set', context.param1
            assertEquals 'test', context.param2
            assertEquals '', context.param3
            return reportResult
        }

        def wrapper = new DefaultsContextWrapper(extractorControl.createMock(),
                [param1: 'default1', param2: 'default2', param3: 'default3'])

        assertEquals reportResult, wrapper.extract([param1: 'set', param2: 'test', param3: ''])

        extractorControl.verify()
    }

    void testDefaults_Null() {
        def reportResult = new ReportResult()

        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.extract { context ->
            assertEquals 'default1', context.param1
            assertEquals 'test', context.param2
            assertEquals '', context.param3
            return reportResult
        }

        def wrapper = new DefaultsContextWrapper(extractorControl.createMock(),
                [param1: 'default1', param2: 'default2', param3: 'default3'])

        assertEquals reportResult, wrapper.extract([param1: null, param2: 'test', param3: ''])

        extractorControl.verify()
    }
}
