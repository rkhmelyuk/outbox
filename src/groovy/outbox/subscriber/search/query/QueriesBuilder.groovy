package outbox.subscriber.search.query

import outbox.subscriber.search.criteria.CriteriaVisitor

/**
 * @author Ruslan Khmelyuk
 */
class QueriesBuilder {

    def subscriptionQueryBuilder = new SubscriptionQueryBuilder()
    def dynamicFieldQueryBuilder = new DynamicFieldQueryBuilder()
    def subscriberFieldQueryBuilder = new SubscriberQueryBuilder()

    Queries build(CriteriaVisitor criteria) {
        def queries = new Queries()

        queries.subscriptionQuery = subscriptionQueryBuilder.build(criteria.subscriptionTree)
        queries.dynamicFieldQuery = dynamicFieldQueryBuilder.build(criteria.dynamicFieldTree)
        queries.subscriberFieldQuery = subscriberFieldQueryBuilder.build(criteria.subscriberFieldTree)

        return queries
    }
}
