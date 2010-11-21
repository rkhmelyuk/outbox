package outbox.subscriber.search.query.elems

import outbox.subscriber.search.Sort

/**
 * @author Ruslan Khmelyuk
 */
class OrderTests extends GroovyTestCase {

    void testFields() {
        def column = new Column((String) null, null)
        def order = new Order(column, Sort.Asc)
        assertEquals column, order.column
        assertEquals Sort.Asc, order.sort
    }

    void testToSQL() {
        def column = new Column('Table', 'Column')
        def order = new Order(column, Sort.Asc)
        assertEquals 'Table.Column asc', order.toSQL()
    }
}
