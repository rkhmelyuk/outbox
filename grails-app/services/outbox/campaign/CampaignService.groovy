package outbox.campaign

import org.hibernate.Session
import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil
import outbox.search.SearchConditions
import outbox.subscriber.Subscriber
import outbox.subscription.SubscriptionList
import outbox.task.TaskFactory
import outbox.task.TaskService
import outbox.template.Template
import outbox.template.TemplateService

/**
 * @author Ruslan Khmelyuk
 */
class CampaignService {

    static transactional = true

    TaskService taskService
    TemplateService templateService

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
    boolean saveCampaign(Campaign campaign, boolean updateState = true) {
        if (!campaign.subject) {
            campaign.subject = campaign.name
        }
        if (updateState) {
            campaign.state = getCampaignState(campaign)
        }
        ServiceUtil.saveOrRollback campaign
    }

    /**
     * Delete campaign, if campaign is not started yet.
     * Otherwise ignores request and returns false.
     *
     * @param campaign the campaign to delete.
     * @return true if was deleted, otherwise false.
     */
    @Transactional
    boolean deleteCampaign(Campaign campaign) {
        if (campaign && campaign.notStarted) {
            // 1. delete campaign subscriptions
            deleteCampaignSubscriptions(campaign)
            // 2. delete campaign
            campaign.delete(flush: true)
            return true
        }
        return false
    }

    /**
     * Start sending campaign. Before moving on, checks whether campaign is in Ready state.
     * If not, returns false and don't send nothing.
     * This method changes campaign state to Queued and enqueue the Send Campaign task, and
     * setup campaign Start Date.
     *
     * @param campaign the campaign to send, if null than false is returned.
     * @return true if started to send successfully, otherwise false.
     */
    @Transactional
    boolean sendCampaign(Campaign campaign) {
        log.info "Starting Sending Campaign $campaign"
        if (campaign) {
            campaign = Campaign.findById(campaign.id)
            if (campaign?.state == CampaignState.Ready) {
                campaign.state = CampaignState.Queued
                campaign.startDate = new Date()
                if (saveCampaign(campaign, false)) {
                    def task = TaskFactory.createSendCampaignTask(campaign)
                    if (taskService.enqueueTask(task)) {
                        log.info "Successfully enqueued task to Send Campaign $campaign"
                        return true
                    }
                    ServiceUtil.setRollbackTransaction()
                }
            }
        }
        log.info "Error to enqueued task to Send Campaign $campaign"

        return false
    }

    /**
     * Deletes subscriptions for specified campaign.
     * @param campaign the campaign to remove subscriptions for
     */
    private void deleteCampaignSubscriptions(Campaign campaign) {
        CampaignSubscription.executeUpdate(
                'delete from CampaignSubscription where campaign = :campaign', [campaign: campaign])
    }

    /**
     * Gets campaign by it's id.
     * @param id the campaign id.
     * @return the found campaign.
     */
    @Transactional(readOnly = true)
    Campaign getCampaign(Long id, boolean lazy = true) {
        def result = Campaign.get(id)

        if (!lazy) {
            result.template?.name
        }

        return result
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

    /**
     * Adds campaign subscription. Subscription must contains both
     * campaign and subscription list.
     *
     * @param subscription the subscription to add.
     * @return true if added, otherwise false.
     */
    @Transactional
    boolean addCampaignSubscription(CampaignSubscription subscription) {
        if (subscription && subscription.campaign.notStarted) {
            boolean result = ServiceUtil.saveOrRollback(subscription)
            return result ? updateCampaignState(subscription.campaign) : false
        }
        return false
    }

    /**
     * Delete campaign subscription, if campaign is not started yet.
     * Otherwise ignores request and returns false.
     * @param subscription the subscription to delete.
     * @return true if was deleted, otherwise false.
     */
    @Transactional
    boolean deleteCampaignSubscription(CampaignSubscription subscription) {
        if (subscription && subscription.campaign.notStarted) {
            subscription.delete(flush: true)
            return updateCampaignState(subscription.campaign)
        }
        return false
    }

    /**
     * Updates campaign state.
     * @param campaign the campaign state.
     * @return the campaign state.
     */
    private boolean updateCampaignState(Campaign campaign) {
        campaign.state = getCampaignState(campaign)
        return saveCampaign(campaign, false)
    }

    /**
     * Gets campaign state.
     * @param campaign the campaign state.
     * @return the campaign state.
     */
    private CampaignState getCampaignState(Campaign campaign) {
        if (campaign.notStarted) {
            def needSubscribers = !getTotalSubscribersNumber(campaign)
            def needTemplate = (campaign.template == null)

            if (!needTemplate && !needSubscribers) {
                return CampaignState.Ready
            }
            else if (needTemplate || needSubscribers) {
                return CampaignState.New
            }
        }

        return campaign.state
    }

    /**
     * Gets campaign subscription by id.
     *
     * @param id the id to get campaign subscription.
     * @return the found campaign subscription, otherwise null
     */
    @Transactional(readOnly = true)
    CampaignSubscription getCampaignSubscription(Long id) {
        CampaignSubscription.get id
    }

    /**
     * Gets the list of campaign subscriptions.
     * @param campaign the campaign to get subscriptions for.
     * @return the list of found campaign subscriptions.
     */
    @Transactional(readOnly = true)
    List<CampaignSubscription> getCampaignSubscriptions(Campaign campaign) {
        CampaignSubscription.findAllByCampaign campaign
    }

    /**
     * Gets the list of campaign subscriptions by subscription list.
     * @param subscription the subscription to get campaign subscriptions for.
     * @return the list of found campaign subscriptions.
     */
    @Transactional(readOnly = true)
    List<CampaignSubscription> getSubscriptions(SubscriptionList subscriptionList) {
        CampaignSubscription.findAllBySubscriptionList subscriptionList
    }

    /**
     * Gets the list of subscriptions list that are proposed to be added to the
     * campaign. Currently this is a list of all subscription lists.
     *
     * @param campaign the campaign to get proposed subscription lists for.
     * @return the list of proposed subscription lists.
     */
    @Transactional(readOnly = true)
    List<SubscriptionList> getProposedSubscriptionLists(Campaign campaign) {
        def result = null
        SubscriptionList.withSession { Session session ->
            result = session.getNamedQuery('Campaign.findProposedSubscriptionLists')
                    .setLong('campaignId', campaign.id)
                    .setLong('memberId', campaign.owner?.id).list()
        }

        result != null ? result : []
    }

    /**
     * Gets the list of proposed templates for campaign.
     *
     * @param campaign the campaign to get proposed templates for.
     * @return the list of proposed templates.
     */
    List<Template> getProposedTemplates(Campaign campaign) {
        List<Template> templates = templateService.getMemberTemplates(campaign.owner)
        if (campaign.template) {
            for (template in templates) {
                if (template.id == campaign.template.id) {
                    templates.remove(template)
                    break
                }
            }
        }
        return templates
    }

    /**
     * Gets the number of real unique subscribers for specified campaign.
     *
     * @param campaign the campaign to get total number of unique subscribers for.
     * @return the number of unique subscribers for specified campaign.
     */
    @Transactional(readOnly = true)
    int getTotalSubscribersNumber(Campaign campaign) {
        int result = 0
        if (campaign != null) {
            CampaignSubscription.withSession { Session session ->
                result = (Integer) session.getNamedQuery('CampaignSubscription.totalSubscribersNumber')
                        .setLong('campaignId', campaign.id).uniqueResult()
            }
        }
        return result
    }

    /**
     * Gets the subscribers ready to sent emails too (e.g. only enabled and not unsubscribed).
     *
     * @param campaign the campaign to get subscribers for.
     * @return the list of subscribers.
     */
    // TODO - remove this method from this service and move to Mailer
    // TODO - we will need to split fetching subscribers into groups with 2000 subscribers in each

    @Transactional(readOnly = true)
    List<Subscriber> getCampaignSubscribers(Campaign campaign) {
        def result = []
        Subscriber.withSession { Session session ->
            result = session.getNamedQuery('CampaignSubscription.campaignSubscribers')
                    .setLong('campaignId', campaign.id).list()
        }
        return result
    }

    /**
     * Add campaign messages to the storage.
     * @param messages the list of messages to add.
     * @return true if saved messages, otherwise false.
     */
    @Transactional
    boolean addCampaignMessages(List<CampaignMessage> messages) {
        for (CampaignMessage message in messages) {
            if (message) {
                if (!ServiceUtil.saveOrRollback(message)) {
                    return false
                }
            }
        }
        return true
    }

}
