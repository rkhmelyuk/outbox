package outbox.task

import grails.test.GrailsUnitTestCase
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldStatus

/**
 * @author Ruslan Khmelyuk
 * @created 2010-11-09
 */
class RemoveDynamicFieldTaskProcessorTests extends GrailsUnitTestCase {

    RemoveDynamicFieldTaskProcessor processor

    @Override protected void setUp() {
        super.setUp()

        processor = new RemoveDynamicFieldTaskProcessor()
    }

    void testProcess_NoDynamicField() {
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 11, id
            return null
        }
        processor.dynamicFieldService = dynamicFieldServiceControl.createMock()

        processor.process new Task(params: [dynamicFieldId: 11])

        dynamicFieldServiceControl.verify()
    }

    void testProcess() {
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 11, id
            return new DynamicField(id: id, status: DynamicFieldStatus.Removed)
        }
        dynamicFieldServiceControl.demand.deleteDynamicFieldValues { dynamicField ->
            assertEquals 11, dynamicField.id
        }
        dynamicFieldServiceControl.demand.deleteDynamicFieldItems { dynamicField ->
            assertEquals 11, dynamicField.id
        }
        dynamicFieldServiceControl.demand.deleteRemovedDynamicField { dynamicField ->
            assertEquals 11, dynamicField.id
        }
        processor.dynamicFieldService = dynamicFieldServiceControl.createMock()

        DynamicField.metaClass.static.withSession = { closure -> closure() }
        DynamicField.metaClass.static.withTransaction = { closure -> closure() }
        processor.process new Task(params: [dynamicFieldId: 11])

        dynamicFieldServiceControl.verify()
    }

    void testProcess_Active() {
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 11, id
            return new DynamicField(id: id, status: DynamicFieldStatus.Active)
        }
        processor.dynamicFieldService = dynamicFieldServiceControl.createMock()

        processor.process new Task(params: [dynamicFieldId: 11])

        dynamicFieldServiceControl.verify()
    }

}
