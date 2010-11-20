package outbox.subscriber.search

/**
 * @author Ruslan Khmelyuk
 */
class Column implements Serializable {

    final String table
    final String name

    Column(String table, String name) {
        this.table = table
        this.name = name
    }

    String toSQL() {
        table + '.' + name
    }
}
