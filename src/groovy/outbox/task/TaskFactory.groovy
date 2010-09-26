package outbox.task

import outbox.campaign.Campaign

/**
 * The factory for main tasks.
 * Using need method with specified parameters will help to construct correct task.
 *  
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class TaskFactory {

    /**
     * Creates the task for sending campaign task.
     * 
     * @param campaign the campaign to be sent, if null task is not created.
     * @return the new task to send campaign or null if campaign is not specified.
     */
    public static Task createSendCampaignTask(Campaign campaign) {
        if (campaign == null) {
            return null
        }
        return new Task(name: 'SendCampaign', version: 1, params: [campaignId: campaign.id])
    }
}
