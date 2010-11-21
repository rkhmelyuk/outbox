package outbox.subscriber.search.query

import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.query.elems.NullColumn
import outbox.subscriber.search.query.elems.Table

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionQueryBuilder implements QueryBuilder {

    Query build(CriteriaTree criteria) {
        if (criteria.empty) {
            return null
        }

        def query = new Query()

        query.addColumn new NullColumn()
        query.addTable new Table('Subscription', 'SS')

        query.criteria = criteria

        return query
    }

}
