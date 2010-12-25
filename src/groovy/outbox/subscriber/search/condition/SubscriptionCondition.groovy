package outbox.subscriber.search.condition

import outbox.subscriber.search.ConditionVisitor
import outbox.subscription.SubscriptionList

/**
 * Subscription related condition, like within specified SL or not.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriptionCondition extends Condition {

    static SubscriptionCondition subscribed(SubscriptionList subscriptionList) {
        new SubscriptionCondition(true, subscriptionList)
    }

    static SubscriptionCondition notSubscribed(SubscriptionList subscriptionList) {
        new SubscriptionCondition(false, subscriptionList)
    }

    // -----------------------------------------------------

    final boolean subscribedTo
    final SubscriptionList subscriptionList

    SubscriptionCondition(boolean subscribed, SubscriptionList subscriptionList) {
        this.subscribedTo = subscribed
        this.subscriptionList = subscriptionList
    }

    void visit(ConditionVisitor visitor) {
        visitor.visitSubscriptionCondition this
    }

}
