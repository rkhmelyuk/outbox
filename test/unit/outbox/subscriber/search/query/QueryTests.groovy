package outbox.subscriber.search.query

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
    }

}
