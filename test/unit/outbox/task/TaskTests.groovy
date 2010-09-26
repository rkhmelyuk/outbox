package outbox.task

import outbox.campaign.Campaign

/**
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class TaskTests extends GroovyTestCase {

    void testFields() {
        Task task = new Task()
        task.name = 'SendCampaign'
        task.version = 2
        task.params = [campaign: new Campaign(id: 3)]

        assertEquals 'SendCampaign', task.name
        assertEquals 2, task.version
        assertNotNull task.params
        assertEquals 3, task.params.campaign.id
    }
}
