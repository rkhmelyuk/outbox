package outbox.subscriber.search.query.elems

/**
 * Represents simple join of specified table by specified condition.
 *
 * @author Ruslan Khmelyuk
 */
// TODO - condition to be CriteriaTree
class Join implements Serializable {

    /**
     * Table, required.
     */
    final Table table

    /**
     * Join condition, required.
     */
    final String condition

    Join(Table table, String condition) {
        this.table = table
        this.condition = condition
    }

    String toSQL() {
        " join ${table.toSQL()} on ($condition)"
    }
}
