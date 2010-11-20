package outbox.subscriber.search.query

import outbox.subscriber.search.criteria.CriteriaTree

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionQueryBuilder implements QueryBuilder {

    Query build(CriteriaTree criteria) {
        if (criteria.empty) {
            return null
        }

        def query = new Query()

        query.addColumn 'null'
        query.addTable 'Subscription', 'SS'

        query.criteria = criteria

        return query
    }

}
