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
        campaign column: 'CampaignId', lazy: false
        subscriptionList column: 'SubscriptionListId', lazy: false
        version false
    }

    static constraints = {
        campaign nullable: false
        subscriptionList nullable: false, validator: { val, obj ->
            if (duplicateSubscription(obj)) {
                return 'campaignSubscription.subscriptionList.unique'
            }
        }
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

    static boolean duplicateSubscription(CampaignSubscription subscription) {
        def found = CampaignSubscription.findAllByCampaignAndSubscriptionList(
                subscription.campaign, subscription.subscriptionList)

        if (!found) {
            return false
        }

        for (each in found) {
            if (each.id != subscription.id) {
                return true
            }
        }
        return false
    }

}
