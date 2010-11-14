package outbox.subscriber.search.condition

/**
 * The interface for the visitor.
 * 
 * @author Ruslan Khmelyuk
 */
interface ConditionVisitor {

    /**
     * Handle {@link outbox.subscriber.search.condition.SubscriberFieldCondition} conditions.
     * @param condition the condition to visit.
     */
    void visitSubscriberFieldCondition(SubscriberFieldCondition condition)

    /**
     * Handle {@link outbox.subscriber.search.condition.DynamicFieldCondition} conditions.
     * @param condition the condition to visit.
     */
    void visitDynamicFieldCondition(DynamicFieldCondition condition)

    /**
     * Handle {@link outbox.subscriber.search.condition.SubscriptionCondition} conditions.
     * @param condition the condition to visit.
     */
    void visitSubscriptionCondition(SubscriptionCondition condition)

}
