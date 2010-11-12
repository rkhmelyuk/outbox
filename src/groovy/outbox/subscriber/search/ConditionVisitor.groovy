package outbox.subscriber.search

/**
 * The interface for the visitor.
 * 
 * @author Ruslan Khmelyuk
 */
interface ConditionVisitor {

    /**
     * Handle {@link SubscriberFieldCondition} conditions.
     * @param condition the condition to visit.
     */
    void visitSubscriberFieldCondition(SubscriberFieldCondition condition)

    /**
     * Handle {@link DynamicFieldCondition} conditions.
     * @param condition the condition to visit.
     */
    void visitDynamicFieldCondition(DynamicFieldCondition condition)

    /**
     * Handle {@link SubscriptionCondition} conditions.
     * @param condition the condition to visit.
     */
    void visitSubscriptionCondition(SubscriptionCondition condition)

}
