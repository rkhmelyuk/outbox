package outbox.subscriber.search.query

/**
 * @author Ruslan Khmelyuk
 */
class QueriesTests extends GroovyTestCase {

    void testFields() {
        def dynamicFieldQuery = new Query()
        def subscriptionQuery = new Query()
        def subscriberFieldQuery = new Query()

        def queries = new Queries()
        queries.subscriptionQuery = subscriptionQuery
        queries.dynamicFieldQuery = dynamicFieldQuery
        queries.subscriberFieldQuery = subscriberFieldQuery

        assertEquals subscriptionQuery, queries.subscriptionQuery
        assertEquals dynamicFieldQuery, queries.dynamicFieldQuery
        assertEquals subscriberFieldQuery, queries.subscriberFieldQuery
    }

}
