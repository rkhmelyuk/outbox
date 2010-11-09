package outbox.task

import outbox.campaign.Campaign
import outbox.subscriber.field.DynamicField

/**
 * The factory for main tasks.
 * Using need method with specified parameters will help to construct correct task.
 *  
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class TaskFactory {

    /**
     * Creates the task to send campaign.
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

    /**
     * Creates the task to remove dynamic field with all related data.
     * @param dynamicField the dynamic field to remove.
     * @return the new task to remove dynamic field or null if dynamic field is not specified.
     */
    public static Task createRemoveDynamicFieldTask(DynamicField dynamicField) {
        if (dynamicField == null) {
            return null
        }

        return new Task(name: 'RemoveDynamicField', version: 1, params: [dynamicFieldId: dynamicField.id])
    }
}
