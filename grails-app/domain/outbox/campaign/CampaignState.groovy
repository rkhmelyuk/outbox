package outbox.campaign

/**
 * The state of campaign. States: new, ready, in progress and done. 
 *
 * @author Ruslan Khmelyuk
 * @since  2010-09-19
 */
public enum CampaignState {

    New (1, 'campaignState.new'),

    Ready(2, 'campaignState.ready'),

    InProgress(3, 'campaignState.inProgress'),

    Finished(4, 'campaignState.finished');

    final Integer id
    final String messageCode

    def CampaignState(id, messageCode) {
        this.id = id;
        this.messageCode = messageCode;
    }
    
}
