package outbox.task

import grails.test.GrailsUnitTestCase
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicFieldItem

/**
 * @author Ruslan Khmelyuk
 * @created 2010-11-09
 */
class RemoveDynamicFieldItemTaskProcessorTests extends GrailsUnitTestCase {

    RemoveDynamicFieldItemTaskProcessor processor

    @Override protected void setUp() {
        super.setUp()

        processor = new RemoveDynamicFieldItemTaskProcessor()
    }

    void testProcess_NoDynamicFieldItem() {
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicFieldItem { id ->
            assertEquals 11, id
            return null
        }
        processor.dynamicFieldService = dynamicFieldServiceControl.createMock()

        processor.process new Task(params: [dynamicFieldItemId: 11])

        dynamicFieldServiceControl.verify()
    }

    void testProcess() {
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicFieldItem { id ->
            assertEquals 11, id
            return new DynamicFieldItem(id: id, removed: true)
        }
        dynamicFieldServiceControl.demand.deleteRemovedDynamicFieldItem { dynamicFieldItem ->
            assertEquals 11, dynamicFieldItem.id
        }
        processor.dynamicFieldService = dynamicFieldServiceControl.createMock()

        DynamicFieldItem.metaClass.static.withSession = { closure -> closure() }
        processor.process new Task(params: [dynamicFieldItemId: 11])

        dynamicFieldServiceControl.verify()
    }

    void testProcess_Active() {
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicFieldItem { id ->
            assertEquals 11, id
            return new DynamicFieldItem(id: id, removed: false)
        }
        processor.dynamicFieldService = dynamicFieldServiceControl.createMock()

        processor.process new Task(params: [dynamicFieldItemId: 11])

        dynamicFieldServiceControl.verify()
    }

}
