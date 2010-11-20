package outbox.subscriber.search

/**
 * @author Ruslan Khmelyuk
 */
class ColumnTests extends GroovyTestCase {

    void testFields() {
        def column = new Column('Person', 'FirstName')
        assertEquals 'Person', column.table
        assertEquals 'FirstName', column.name
    }

    void testToSQL() {
        def column = new Column('Person', 'FirstName')
        assertEquals 'Person.FirstName', column.toSQL()
    }
}
