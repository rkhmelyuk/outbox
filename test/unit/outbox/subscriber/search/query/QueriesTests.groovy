package outbox.subscriber.search.query

/**
 * @author Ruslan Khmelyuk
 */
class QueriesTests extends GroovyTestCase {

    void testFields() {
        def dynamicFieldQueries = []
        def subscriptionQueries = []
        def subscriberFieldQuery = new Query()

        def queries = new Queries()
        queries.subscriptionQueries = subscriptionQueries
        queries.dynamicFieldQueries = dynamicFieldQueries
        queries.subscriberFieldQuery = subscriberFieldQuery

        assertEquals subscriptionQueries, queries.subscriptionQueries
        assertEquals dynamicFieldQueries, queries.dynamicFieldQueries
        assertEquals subscriberFieldQuery, queries.subscriberFieldQuery
    }

}
