package outbox.subscriber.search.sql

import outbox.subscriber.search.Columns
import outbox.subscriber.search.query.Query

/**
 * Builds select count queries.
 *
 * @author Ruslan Khmelyuk
 */
class CountSqlQueryBuilder extends BaseSqlQueryBuilder {

    String build(Query query) {
        def builder = new StringBuilder()
        builder << 'select '

        if (query.distinct) {
            builder << 'distinct '
        }

        builder << 'count(*) as ' + Columns.RowCount

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
            builder << join
        }

        def criteria = query.criteria
        if (criteria && criteria.root) {
            // include criteria
            builder << ' where '
            buildCriteria(builder, criteria.root)
        }

        return builder.toString()
    }

}
