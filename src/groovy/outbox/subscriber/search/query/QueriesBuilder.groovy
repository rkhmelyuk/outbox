package outbox.subscriber.search.query

import outbox.subscriber.search.CriteriaVisitor
import outbox.subscriber.search.condition.Conditions

/**
 * @author Ruslan Khmelyuk
 */
class QueriesBuilder {

    def subscriptionQueryBuilder = new SubscriptionQueryBuilder()
    def dynamicFieldQueryBuilder = new DynamicFieldQueryBuilder()
    def subscriberFieldQueryBuilder = new SubscriberQueryBuilder()

    Queries build(Conditions conditions, CriteriaVisitor criteria) {
        def queries = new Queries()

        queries.subscriptionQuery = subscriptionQueryBuilder.build(criteria.subscriptionTree)
        queries.dynamicFieldQuery = dynamicFieldQueryBuilder.build(criteria.dynamicFieldTree)
        queries.subscriberFieldQuery = subscriberFieldQueryBuilder.build(criteria.subscriberFieldTree)
        queries.subscriberFieldQuery.page = conditions.page
        queries.subscriberFieldQuery.perPage = conditions.perPage

        return queries
    }
}
