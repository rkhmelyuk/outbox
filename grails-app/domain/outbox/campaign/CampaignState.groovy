package outbox.campaign

/**
 * The state of campaign. States: new, ready, queued, sending, in progress and done. 
 *
 * @author Ruslan Khmelyuk
 * @since  2010-09-19
 */
public enum CampaignState {

    /**
     * Campaign is not ready for sending.
     */
    New (1, 'campaignState.new'),

    /**
     * Campaign is ready for sending.
     */
    Ready(2, 'campaignState.ready'),

    /**
     * Campaign is queued for sending.
     */
    Queued(3, 'campaignState.queued'),

    /**
     * Emails are sending to subscribers.
     */
    Sending(4, 'campaignState.sending'),

    /**
     * Campaign is in progress. Gathering results.
     */
    InProgress(5, 'campaignState.inProgress'),

    /**
     * Campaign is finished.
     */
    Finished(6, 'campaignState.finished');

    final Integer id
    final String messageCode

    def CampaignState(id, messageCode) {
        this.id = id;
        this.messageCode = messageCode;
    }
    
}
