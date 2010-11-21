package outbox.subscriber.search.query

import outbox.subscriber.search.Sort
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Join
import outbox.subscriber.search.query.elems.Order
import outbox.subscriber.search.query.elems.Table

/**
 * @author Ruslan Khmelyuk
 */
class QueryTests extends GroovyTestCase {

    Query query

    @Override protected void setUp() {
        super.setUp()

        query = new Query()
    }

    void testFields() {
        query.page = 10
        query.perPage = 15

        assertEquals 10, query.page
        assertEquals 15, query.perPage

        def column = new Column((String) null, null)
        query.addColumn(column)

        def table = new Table(null)
        query.addTable(table)

        def join = new Join(table, 'condition')
        query.addJoin(join)

        def order = new Order(column, Sort.Asc)
        query.addOrder(order)

        assertTrue query.columns.contains(column)
        assertTrue query.tables.contains(table)
        assertTrue query.joins.contains(join)
        assertTrue query.orders.contains(order)
    }

}
