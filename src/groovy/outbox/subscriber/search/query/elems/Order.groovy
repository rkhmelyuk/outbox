package outbox.subscriber.search.query.elems

import outbox.subscriber.search.Sort

/**
 * The information about ordering that can be translated easily into sql.
 * 
 * @author Ruslan Khmelyuk
 */
class Order {

    final Column column
    final Sort sort

    Order(Column column, Sort sort) {
        this.column = column
        this.sort = sort
    }

    /**
     * SQL of order statement.
     * @return the sql of order statement as string.
     */
    String toSQL() {
        "${column.toSQL()} $sort.keyword"
    }
}
