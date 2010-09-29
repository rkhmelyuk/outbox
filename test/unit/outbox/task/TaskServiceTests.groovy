package outbox.task

import grails.test.GrailsUnitTestCase
import grails.test.MockUtils
import outbox.campaign.Campaign

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

    void testEnqueue_Unexpected() {
        Task task = new Task(name: 'some unexpected name')
        assertTrue taskService.enqueueTask(task)
    }
}