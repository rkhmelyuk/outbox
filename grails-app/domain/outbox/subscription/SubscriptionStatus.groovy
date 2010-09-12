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
}
