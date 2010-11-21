package outbox.subscriber.search.query.elems

/**
 * @author Ruslan Khmelyuk
 */
class Table implements Serializable {

    final String name
    final String alias

    Table(String name, String alias = null) {
        this.name = name
        this.alias = alias
    }

    String toSQL() {
        def out = new StringBuilder()
        out << name
        if (alias) {
            out << ' as ' + alias
        }
        return out.toString()
    }
}
