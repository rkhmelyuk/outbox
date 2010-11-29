package outbox.subscriber.search.query.elems

/**
 * @author Ruslan Khmelyuk
 */
class ColumnTests extends GroovyTestCase {

    void testFields() {
        def column = new Column('Table', 'Column', 'Alias', ColumnType.Number)
        assertEquals 'Table', column.table
        assertEquals 'Column', column.name
        assertEquals 'Alias', column.alias
        assertEquals ColumnType.Number, column.type

        column = new Column('Table', 'Column')
        assertEquals 'Table', column.table
        assertEquals 'Column', column.name
        assertNull column.alias
        assertNull column.type
    }

    void testToSQL() {
        def column = new Column('Person', 'FirstName', 'Alias')
        assertEquals 'Person.FirstName as Alias', column.toSQL()
    }
}
