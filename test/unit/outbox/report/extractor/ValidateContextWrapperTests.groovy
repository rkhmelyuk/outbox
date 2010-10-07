package outbox.report.extractor

import grails.test.GrailsUnitTestCase
import outbox.report.Period
import outbox.report.ReportResult
import outbox.report.metadata.Parameter
import outbox.report.metadata.ParameterType

/**
 * @author Ruslan Khmelyuk
 */
class ValidateContextWrapperTests extends GrailsUnitTestCase {

    void testSuccess() {
        def reportResult = new ReportResult()

        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.getParameters {
            [
                    new Parameter(name: 'integer', type: ParameterType.Integer, required: true),
                    new Parameter(name: 'string', type: ParameterType.String, required: true),
                    new Parameter(name: 'bool', type: ParameterType.Boolean, required: true),
                    new Parameter(name: 'floatNum', type: ParameterType.Float, required: true),
                    new Parameter(name: 'date', type: ParameterType.Date, required: true),
                    new Parameter(name: 'period', type: ParameterType.Period, required: true),
            ]
        }
        extractorControl.demand.extract { reportResult }
        def wrapper = new ValidateContextWrapper(extractorControl.createMock())

        assertEquals reportResult, wrapper.extract([
                integer: 1, string: 'text', bool: true, floatNum: 12.22,
                date: new Date(), period: Period.Day])
        
        extractorControl.verify()
    }

    void testRequired() {
        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.getParameters {
            [new Parameter(name: 'campaignId', type: ParameterType.Integer, required: true)]
        }
        def wrapper = new ValidateContextWrapper(extractorControl.createMock())

        try {
            wrapper.extract([:])
            fail 'Must throw WrongContextException'
        }
        catch (WrongContextException e) {

        }
        catch (Exception e) {
            fail 'Unexpected exception ' + e
        }
        finally {
            extractorControl.verify()
        }
    }

    void testNotInteger() {
        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.getParameters {
            [new Parameter(name: 'campaignId', type: ParameterType.Integer, required: true)]
        }
        def wrapper = new ValidateContextWrapper(extractorControl.createMock())

        try {
            wrapper.extract([campaignId: 12.22])
            fail 'Must throw WrongContextException'
        }
        catch (WrongContextException e) {

        }
        catch (Exception e) {
            fail 'Unexpected exception ' + e
        }
        finally {
            extractorControl.verify()
        }
    }

    void testNotFloat() {
        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.getParameters {
            [new Parameter(name: 'campaignId', type: ParameterType.Float, required: true)]
        }
        def wrapper = new ValidateContextWrapper(extractorControl.createMock())

        try {
            wrapper.extract([campaignId: 'text'])
            fail 'Must throw WrongContextException'
        }
        catch (WrongContextException e) {

        }
        catch (Exception e) {
            fail 'Unexpected exception ' + e
        }
        finally {
            extractorControl.verify()
        }
    }

    void testNotBoolean() {
        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.getParameters {
            [new Parameter(name: 'campaignId', type: ParameterType.Boolean, required: true)]
        }
        def wrapper = new ValidateContextWrapper(extractorControl.createMock())

        try {
            wrapper.extract([campaignId: 32])
            fail 'Must throw WrongContextException'
        }
        catch (WrongContextException e) {

        }
        catch (Exception e) {
            fail 'Unexpected exception ' + e
        }
        finally {
            extractorControl.verify()
        }
    }

    void testNotDate() {
        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.getParameters {
            [new Parameter(name: 'campaignId', type: ParameterType.Date, required: true)]
        }
        def wrapper = new ValidateContextWrapper(extractorControl.createMock())

        try {
            wrapper.extract([campaignId: true])
            fail 'Must throw WrongContextException'
        }
        catch (WrongContextException e) {

        }
        catch (Exception e) {
            fail 'Unexpected exception ' + e
        }
        finally {
            extractorControl.verify()
        }
    }

    void testNotPeriod() {
        def extractorControl = mockFor(ReportExtractor)
        extractorControl.demand.getParameters {
            [new Parameter(name: 'campaignId', type: ParameterType.Period, required: true)]
        }
        def wrapper = new ValidateContextWrapper(extractorControl.createMock())

        try {
            wrapper.extract([campaignId: 32])
            fail 'Must throw WrongContextException'
        }
        catch (WrongContextException e) {

        }
        catch (Exception e) {
            fail 'Unexpected exception ' + e
        }
        finally {
            extractorControl.verify()
        }
    }
}
