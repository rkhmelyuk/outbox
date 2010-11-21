package outbox.subscriber.search.sql

import outbox.subscriber.search.query.Query
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Table

/**
 * @author Ruslan Khmelyuk
 */
class CountSqlQueryBuilderTests extends GroovyTestCase {
    
    Query query
    CountSqlQueryBuilder builder
    
    @Override protected void setUp() {
        super.setUp()
        
        query = new Query()
        builder = new CountSqlQueryBuilder()
    }

    void testCount() {
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))

        assertEquals 'select count(*) as RowCount from Person as p', builder.build(query)
    }

    void testDistinctCount() {
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        query.distinct = true

        assertEquals 'select distinct count(*) as RowCount from Person as p', builder.build(query)
    }
    
}
