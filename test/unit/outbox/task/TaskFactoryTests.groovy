package outbox.task

import outbox.campaign.Campaign
import outbox.subscriber.field.DynamicField

/**
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class TaskFactoryTests extends GroovyTestCase {

    void testCreateSendCampaignTask() {
        assertNull 'Should not create task for null campaign.', TaskFactory.createSendCampaignTask(null)

        def task = TaskFactory.createSendCampaignTask(new Campaign(id: 3))

        assertNotNull task
        assertEquals 'SendCampaign', task.name
        assertEquals 1, task.version
        assertEquals 3, task.params.campaignId
    }

    void testRemoveDynamicFieldTask() {
        assertNull 'Should not create task for null dynamic field.', TaskFactory.createSendCampaignTask(null)

        def task = TaskFactory.createRemoveDynamicFieldTask(new DynamicField(id: 4))

        assertNotNull task
        assertEquals 'RemoveDynamicField', task.name
        assertEquals 1, task.version
        assertEquals 4, task.params.dynamicFieldId
    }
}
