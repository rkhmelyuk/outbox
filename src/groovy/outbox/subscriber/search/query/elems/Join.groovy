package outbox.subscriber.search.query.elems

/**
 * @author Ruslan Khmelyuk
 */
class Join implements Serializable {

    final Table table
    final String condition

    Join(Table table, String condition) {
        this.table = table
        this.condition = condition
    }

    String toSQL() {
        " join ${table.toSQL()} on ($condition)"
    }
}
