package outbox.subscriber.search.sql

import outbox.subscriber.search.query.Query

/**
 * Select data query builder
 *
 * @author Ruslan Khmelyuk
 */
class SelectSqlQueryBuilder extends BaseSqlQueryBuilder {

    String build(Query query) {
        def builder = new StringBuilder()
        builder << 'select '

        if (query.distinct) {
            builder << 'distinct '
        }

        query.columns.eachWithIndex { column, index ->
            if (index != 0) {
                builder << ', '
            }
            builder << column
        }

        builder << ' from '
        query.tables.eachWithIndex { table, index ->
            if (index != 0) {
                builder << ', '
            }
            builder << table
        }

        query.joins.eachWithIndex { join, index ->
            if (index != 0) {
                builder << ', '
            }
            builder << join
        }

        def criteria = query.criteria
        if (criteria && criteria.root) {
            // include criteria
            builder << ' where '
            buildCriteria(builder, criteria.root)
        }

        query.orders.eachWithIndex { order, index ->
            if (index != 0) {
                builder << ', '
            }
            else {
                builder << ' order by '
            }
            builder << "$order.column $order.sort.keyword"
        }

        return builder.toString()
    }

}
