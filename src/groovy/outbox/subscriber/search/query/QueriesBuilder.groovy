package outbox.subscriber.search.query

import outbox.subscriber.search.criteria.CriteriaVisitor

/**
 * @author Ruslan Khmelyuk
 */
class QueriesBuilder {

    QueryBuilder dynamicFieldQueryBuilder
    QueryBuilder subscriptionQueryBuilder
    QueryBuilder subscriberFieldQueryBuilder

    Queries build(CriteriaVisitor criteria) {
        def queries = new Queries()

        queries.subscriptionQuery = subscriptionQueryBuilder.build(criteria.subscriptionTree)
        queries.dynamicFieldQuery = dynamicFieldQueryBuilder.build(criteria.dynamicFieldTree)
        queries.subscriberFieldQuery = subscriberFieldQueryBuilder.build(criteria.subscriberFieldTree)

        return queries
    }
}
