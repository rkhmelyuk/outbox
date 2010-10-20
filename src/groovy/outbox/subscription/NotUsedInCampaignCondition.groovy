package outbox.subscription

import grails.orm.HibernateCriteriaBuilder
import outbox.campaign.Campaign
import outbox.search.Condition

/**
 * To search subscription lists not used in specified campaign.
 *
 * @author Ruslan Khmelyuk
 */
class NotUsedInCampaignCondition implements Condition {

    Campaign campaign

    boolean isConditionFilter() {
        true
    }

    void build(HibernateCriteriaBuilder builder) {
        builder.sqlRestriction """SubscriptionListId not in
                (select CS.SubscriptionListId from CampaignSubscription CS
                where CS.CampaignId = ${campaign.id})"""
    }

}
