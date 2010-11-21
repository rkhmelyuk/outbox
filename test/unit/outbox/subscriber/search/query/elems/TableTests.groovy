package outbox.subscriber.search.query.elems

/**
 * @author Ruslan Khmelyuk
 */
class TableTests extends GroovyTestCase {

    void testFields() {
        def table = new Table('Table', 'Alias')
        assertEquals 'Table', table.name
        assertEquals 'Alias', table.alias

        table = new Table('Table')
        assertEquals 'Table', table.name
        assertNull table.alias
    }

    void testToSQL() {
        def table = new Table('Table', 'Alias')
        assertEquals 'Table as Alias', table.toSQL()
    }
}
