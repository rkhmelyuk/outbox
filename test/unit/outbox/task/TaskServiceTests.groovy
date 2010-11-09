package outbox.task

import grails.test.GrailsUnitTestCase
import grails.test.MockUtils
import outbox.campaign.Campaign
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldItem

/**
 * @author Ruslan Khmelyuk
 */
class TaskServiceTests extends GrailsUnitTestCase {

    TaskService taskService

    @Override protected void setUp() {
        super.setUp();
        taskService = new TaskService()
        MockUtils.mockLogging(outbox.task.TaskService) 
    }

    void testEnqueue_Null() {
        assertFalse taskService.enqueueTask(null)
    }

    void testEnqueue_SendCampaign() {
        Task task = TaskFactory.createSendCampaignTask(new Campaign(id: 1))

        def sendCampaignTaskProcessorControl = mockFor(TaskProcessor)
        sendCampaignTaskProcessorControl.demand.process { aTask ->
            assertEquals 'SendCampaign', aTask.name
            assertEquals 1, aTask.params.campaignId
        }
        taskService.sendCampaignTaskProcessor = sendCampaignTaskProcessorControl.createMock()

        assertTrue taskService.enqueueTask(task)

        Thread.sleep(1500)
        
        sendCampaignTaskProcessorControl.verify()
    }

    void testEnqueue_RemoveDynamicField() {
        Task task = TaskFactory.createRemoveDynamicFieldTask(new DynamicField(id: 1))

        def removeDynamicFieldTaskProcessorControl = mockFor(TaskProcessor)
        removeDynamicFieldTaskProcessorControl.demand.process { aTask ->
            assertEquals 'RemoveDynamicField', aTask.name
            assertEquals 1, aTask.params.dynamicFieldId
        }
        taskService.removeDynamicFieldTaskProcessor = removeDynamicFieldTaskProcessorControl.createMock()

        assertTrue taskService.enqueueTask(task)

        Thread.sleep(1500)

        removeDynamicFieldTaskProcessorControl.verify()
    }


    void testEnqueue_RemoveDynamicFieldItem() {
        Task task = TaskFactory.createRemoveDynamicFieldItemTask(new DynamicFieldItem(id: 1))

        def removeDynamicFieldItemTaskProcessorControl = mockFor(TaskProcessor)
        removeDynamicFieldItemTaskProcessorControl.demand.process { aTask ->
            assertEquals 'RemoveDynamicFieldItem', aTask.name
            assertEquals 1, aTask.params.dynamicFieldItemId
        }
        taskService.removeDynamicFieldItemTaskProcessor = removeDynamicFieldItemTaskProcessorControl.createMock()

        assertTrue taskService.enqueueTask(task)

        Thread.sleep(1500)

        removeDynamicFieldItemTaskProcessorControl.verify()
    }

    void testEnqueue_Unexpected() {
        Task task = new Task(name: 'some unexpected name')
        assertTrue taskService.enqueueTask(task)
    }
}
