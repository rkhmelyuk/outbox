package outbox.subscriber.search.query.elems

/**
 * @author Ruslan Khmelyuk
 */
class Column implements Serializable {

    final String table
    final String name
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
