package outbox.subscriber.search.query

import outbox.subscriber.search.Conditions
import outbox.subscriber.search.CriteriaVisitor

/**
 * @author Ruslan Khmelyuk
 */
class QueriesBuilder {

    def subscriptionQueryBuilder = new SubscriptionQueryBuilder()
    def dynamicFieldQueryBuilder = new DynamicFieldQueryBuilder()
    def subscriberFieldQueryBuilder = new SubscriberQueryBuilder()

    Queries build(Conditions conditions, CriteriaVisitor criteria) {
        def queries = new Queries()

        queries.subscriptionQueries = criteria.subscriptionTrees.collect { subscriptionQueryBuilder.build(it)}
        queries.dynamicFieldQueries = criteria.dynamicFieldTrees.collect { dynamicFieldQueryBuilder.build(it)}
        queries.subscriberFieldQuery = subscriberFieldQueryBuilder.build(criteria.subscriberFieldTree)
        queries.subscriberFieldQuery.page = conditions.page
        queries.subscriberFieldQuery.perPage = conditions.perPage
        queries.subscriberFieldQuery.orders = conditions.orders

        return queries
    }
}
