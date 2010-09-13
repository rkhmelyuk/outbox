package outbox.subscription

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionStatus {

    Integer id
    String name

    static mapping = {
        table 'SubscriptionStatus'
        id column: 'SubscriptionStatusId'
        name column: 'Name'
        version false
        cache usage: 'read-only', include: 'non-lazy'
    }

    static constraints = {
        name nullable: true, blank: true, maxSize: 200
    }

    /**
     * @return the Subscribed subscription status.
     */
    static SubscriptionStatus subscribed() {
        SubscriptionStatus.get 1
    }

    /**
     * @return the Unsubscribed subscription status.
     */
    static SubscriptionStatus unsubscribed() {
        SubscriptionStatus.get 2
    }

    /**
     * @return the Unsubscribed by recipient subscription status.
     */
    static SubscriptionStatus unsubscribedByRecipient() {
        SubscriptionStatus.get 3
    }
}
