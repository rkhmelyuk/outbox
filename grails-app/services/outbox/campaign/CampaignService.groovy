package outbox.campaign

import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil
import outbox.search.SearchConditions

/**
 * @author Ruslan Khmelyuk
 */
class CampaignService {

    static transactional = true

    /**
     * Adds new campaign. Default campaign state is new. 
     * If subject is empty, than it will be the same as campaign name.
     * 
     * @param campaign the campaign to be added.
     * @return true if added, otherwise false.
     */
    @Transactional
    boolean addCampaign(Campaign campaign) {
        if (!campaign.state) {
            campaign.state = CampaignState.New 
        }
        if (!campaign.subject) {
            campaign.subject = campaign.name
        }
        
        ServiceUtil.saveOrRollback campaign
    }

    /**
     * Saves campaign. 
     * 
     * @param campaign the campaign to be saved.
     * @return true if saved, otherwise false.
     */
    @Transactional
    boolean saveCampaign(Campaign campaign) {
        if (!campaign.subject) {
            campaign.subject = campaign.name
        }

        ServiceUtil.saveOrRollback campaign
    }

    /**
     * Gets campaign by it's id.
     * @param id the campaign id.
     * @return the found campaign.
     */
    @Transactional(readOnly = true)
    Campaign getCampaign(Long id) {
        Campaign.get id
    }

    /**
     * Gets the list of member campaigns.
     * @param member the member to get campaigns for.
     * @return the list with found member campaigns.
     */
    @Transactional(readOnly = true)
    List<Campaign> searchCampaigns(SearchConditions conditions) {
        conditions.search(Campaign.createCriteria())
    }

}
