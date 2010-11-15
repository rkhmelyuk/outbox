package outbox.subscriber.search.condition

import outbox.subscriber.search.ConditionVisitor

/**
 * Subscription related condition, like within specified SL or not.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriptionCondition extends Condition {

    static SubscriptionCondition subscribed(List<Long> listIds) {
        new SubscriptionCondition(true, listIds)
    }

    static SubscriptionCondition notSubscribed(List<Long> listIds) {
        new SubscriptionCondition(false, listIds)
    }

    // -----------------------------------------------------

    final boolean subscribed
    final List<Long> subscriptionListIds

    SubscriptionCondition(boolean subscribed, List<Long> subscriptionListIds) {
        this.subscribed = subscribed
        this.subscriptionListIds = subscriptionListIds
    }

    void visit(ConditionVisitor visitor) {
        visitor.visitSubscriptionCondition this
    }

}
