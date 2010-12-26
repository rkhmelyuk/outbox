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
            builder << column.toSQL()
        }

        if (query.tables) {
            builder << ' from '
            query.tables.eachWithIndex { table, index ->
                if (index != 0) {
                    builder << ', '
                }
                builder << table.toSQL()
            }
            query.joins.eachWithIndex { join, index ->
                if (index != 0) {
                    builder << ', '
                }
                builder << join.toSQL()
            }
        }

        def criteria = query.criteria
        if (criteria && criteria.root) {
            // include criteria
            builder << ' where 1=1 '
            buildCriteria(builder, criteria.root)
        }

        if (query.orders) {
            builder << ' order by '
            query.orders.eachWithIndex { order, index ->
                if (index != 0) {
                    builder << ', '
                }
                builder << order.toSQL()
            }
        }

        return builder.toString()
    }

}
