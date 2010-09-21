package outbox.campaign

import outbox.subscription.SubscriptionList

/**
 * @author Ruslan Khmelyuk
 */
class CampaignSubscription implements Comparable<CampaignSubscription> {

    Long id
    Campaign campaign
    SubscriptionList subscriptionList

    static mapping = {
        table 'CampaignSubscription'
        id column: 'CampaignSubscriptionId'
        campaign column: 'CampaignId'
        subscriptionList column: 'SubscriptionListId'
        version false
    }

    static constraints = {
        campaign nullable: false
        subscriptionList nullable: false
    }

    int compareTo(CampaignSubscription other) {
        if (other == null) {
            return 1
        }
        if (id && other.id) {
            return -id.compareTo(other.id)
        }
        else if (id) {
            return 1
        }
        else if (other.id) {
            return -1
        }
        
        return 0
    }

}
