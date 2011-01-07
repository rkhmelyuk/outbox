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

    /**
     * The column type.
     */
    final ColumnType type

    Column(String table, String name, String alias = null, ColumnType type = null) {
        this.table = table
        this.name = name
        this.alias = alias
        this.type = type
    }

    Column(Table table, String name, String alias = null, ColumnType type = null) {
        this.name = name
        this.alias = alias
        this.type = type

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

    String toString() {
        name
    }
}
