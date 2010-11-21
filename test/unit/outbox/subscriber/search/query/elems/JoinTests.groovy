package outbox.subscriber.search.query.elems

/**
 * @author Ruslan Khmelyuk
 */
class JoinTests extends GroovyTestCase {

    void testFields() {
        def table = new Table('Table', 'Alias')
        def join = new Join(table, 'condition')
        assertEquals table, join.table
        assertEquals 'condition', join.condition
    }

    void testToSQL() {
        def table = new Table('Table', 'Alias')
        def join = new Join(table, 'condition')
        assertEquals ' join Table as Alias on (condition)', join.toSQL()
    }

}
