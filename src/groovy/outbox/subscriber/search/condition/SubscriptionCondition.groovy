package outbox.subscriber.search.condition

import outbox.subscriber.search.ConditionVisitor

/**
 * Subscription related condition, like within specified SL or not.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriptionCondition extends Condition {

    static SubscriptionCondition subscribed(Long subscriptionListId) {
        new SubscriptionCondition(true, subscriptionListId)
    }

    static SubscriptionCondition notSubscribed(Long subscriptionListId) {
        new SubscriptionCondition(false, subscriptionListId)
    }

    // -----------------------------------------------------

    final boolean subscribedTo
    final Long subscriptionListId

    SubscriptionCondition(boolean subscribed, Long subscriptionListId) {
        this.subscribedTo = subscribed
        this.subscriptionListId = subscriptionListId
    }

    void visit(ConditionVisitor visitor) {
        visitor.visitSubscriptionCondition this
    }

}
