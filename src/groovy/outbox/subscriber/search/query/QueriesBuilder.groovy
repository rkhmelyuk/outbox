package outbox.subscriber.search.query

import outbox.subscriber.search.Conditions
import outbox.subscriber.search.CriteriaVisitor
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Order

/**
 * Builds the {@link Queries} object. Use a set of {@link QueryBuilder} implementations
 * to build query for each {@link outbox.subscriber.search.criteria.CriteriaTree} returned
 * by {@link CriteriaVisitor}.
 *
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
        queries.subscriberFieldQuery.orders = conditions.orders.collect { fieldOrder ->
            def columnName = fieldOrder.column
            def column = queries.subscriberFieldQuery.columns.find { it.name == columnName }
            if (!column) { column = new Column((String) null, columnName) }

            return new Order(column, fieldOrder.sort)
        }

        return queries
    }
}
