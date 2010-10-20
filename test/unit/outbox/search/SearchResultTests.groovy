package outbox.search

/**
 * @author Ruslan Khmelyuk
 */
class SearchResultTests extends GroovyTestCase {

    void testFields() {
        def result = new SearchResult()
        result.list = [1]
        result.total = 101

        assertEquals 1, result.list.size()
        assertTrue result.list.contains(1)
        assertEquals 101, result.total
    }

}
