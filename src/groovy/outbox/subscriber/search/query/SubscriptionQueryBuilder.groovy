package outbox.subscriber.search.query

import outbox.subscriber.search.Names
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.query.elems.NullColumn
import outbox.subscriber.search.query.elems.Table

/**
 * The builder for subscription query used as subquery.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriptionQueryBuilder implements QueryBuilder {

    Query build(CriteriaTree criteria) {
        if (criteria.empty) {
            return null
        }

        def query = new Query()

        query.addColumn new NullColumn()
        query.addTable new Table(Names.SubscriptionTable, Names.SubscriptionAlias)

        query.criteria = criteria

        return query
    }

}
