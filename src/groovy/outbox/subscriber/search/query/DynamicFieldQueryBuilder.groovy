package outbox.subscriber.search.query

import outbox.subscriber.search.Columns
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Join
import outbox.subscriber.search.query.elems.Table

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

        def dynamicFieldValueTable = new Table('DynamicFieldValue', 'DFV')
        query.addColumn new Column(dynamicFieldValueTable, Columns.SubscriberId)
        query.addTable dynamicFieldValueTable
        query.addJoin new Join(new Table('DynamicField', 'DF'), 'DF.DynamicFieldId = DFV.DynamicFieldId')

        query.criteria = criteria

        return query
    }

}
