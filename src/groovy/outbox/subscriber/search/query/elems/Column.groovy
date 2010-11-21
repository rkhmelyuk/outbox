package outbox.subscriber.search.query.elems

/**
 * Represents table column or select result column name.
 *
 * @author Ruslan Khmelyuk
 */
class Column implements Serializable {

    /**
     * Table, not required.
     */
    final String table

    /**
     * Column name, required.
     */
    final String name

    /**
     * Column alias, not required.
     */
    final String alias

    Column(String table, String name, String alias = null) {
        this.table = table
        this.name = name
        this.alias = alias
    }

    Column(Table table, String name, String alias = null) {
        this.name = name
        this.alias = alias

        if (table) {
            if (table.alias) {
                this.table = table.alias
            }
            else {
                this.table = table.name
            }
        }
    }

    /**
     * Returns SQL column name.
     * @return the sql column name as string.
     */
    String toSQL() {
        def out = new StringBuilder()
        if (table) {
            out << table + '.'
        }
        out << name
        if (alias) {
            out << ' as ' + alias
        }
        return out.toString()
    }
}
