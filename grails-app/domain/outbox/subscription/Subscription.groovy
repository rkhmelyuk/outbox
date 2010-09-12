package outbox.subscription

import outbox.subscriber.Subscriber

/**
 * The relationship between Subscriber and SubscriptionList.
 * 
 * @author Ruslan Khmelyuk
 */
class Subscription {

    Long id
    Subscriber subscriber
    SubscriptionList subscriptionList
    SubscriptionStatus status

    Date dateCreated

    static mapping = {
        table 'Subscription'
        id column: 'SubscriptionId'
        columns {
            subscriber column: 'SubscriberId'
            subscriptionList column: 'SubscriptionListId'
            status column: 'SubscriptionStatusId'
            dateCreated column: 'CreateDate'
        }
        version false
        cache false
        sort 'dateCreated'

        status lazy: false
        subscriber lazy: false
        subscriptionList lazy: false
    }

    static constraints = {
        subscriber nullable: false
        subscriptionList nullable: false
        status nullable: false
        dateCreated nullable: true
    }
}
