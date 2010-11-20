package outbox.subscriber.search.query

import outbox.subscriber.search.Columns
import outbox.subscriber.search.criteria.CriteriaTree

/**
 * The builder for query to search over dynamic field values.
 * @author Ruslan Khmelyuk
 */
class DynamicFieldQueryBuilder implements QueryBuilder {

    Query build(CriteriaTree criteria) {
        if (criteria.empty) {
            return null
        }

        def query = new Query()

        query.distinct = true

        query.addTableColumn 'DFV', Columns.SubscriberId
        query.addTable 'DynamicFieldValue', 'DFV'
        query.addJoin 'DynamicField', 'DF', 'DF.DynamicFieldId = DFV.DynamicFieldId'

        query.criteria = criteria

        return query
    }

}
