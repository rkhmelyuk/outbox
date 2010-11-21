package outbox.subscriber.search.query.elems

/**
 * Represents table name.
 *
 * @author Ruslan Khmelyuk
 */
class Table implements Serializable {

    final String name
    final String alias

    Table(String name, String alias = null) {
        this.name = name
        this.alias = alias
    }

    /**
     * SQL representation of table name.
     * @return the sql table name as string.
     */
    String toSQL() {
        def out = new StringBuilder()
        out << name
        if (alias) {
            out << ' as ' + alias
        }
        return out.toString()
    }
}
