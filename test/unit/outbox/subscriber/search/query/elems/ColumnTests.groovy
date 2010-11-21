package outbox.subscriber.search.query.elems

/**
 * @author Ruslan Khmelyuk
 */
class ColumnTests extends GroovyTestCase {

    void testFields() {
        def column = new Column('Table', 'Column', 'Alias')
        assertEquals 'Table', column.table
        assertEquals 'Column', column.name
        assertEquals 'Alias', column.alias

        column = new Column('Table', 'Column')
        assertEquals 'Table', column.table
        assertEquals 'Column', column.name
        assertNull column.alias
    }

    void testToSQL() {
        def column = new Column('Person', 'FirstName', 'Alias')
        assertEquals 'Person.FirstName as Alias', column.toSQL()
    }
}
