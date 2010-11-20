package outbox.subscriber.search.sql

import outbox.subscriber.search.query.Query

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
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')

        assertEquals 'select count(*) as RowCount from Person as p', builder.build(query)
    }

    void testDistinctCount() {
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        query.distinct = true

        assertEquals 'select distinct count(*) as RowCount from Person as p', builder.build(query)
    }
    
}
