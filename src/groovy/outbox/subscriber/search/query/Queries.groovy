package outbox.subscriber.search.query

/**
 * Holds queries used to search subscribers.
 * We use single query to search for subscribers and their fields,
 * and each query for search by dynamic field or subscription.
 *
 * @author Ruslan Khmelyuk
 */
class Queries {

    Query subscriberFieldQuery

    List<Query> dynamicFieldQueries

    List<Query> subscriptionQueries

}
