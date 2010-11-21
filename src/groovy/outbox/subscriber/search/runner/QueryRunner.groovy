package outbox.subscriber.search.runner

import outbox.subscriber.search.Subscribers
import outbox.subscriber.search.query.Queries

/**
 * The query runner interface.
 * There will be used one of few query runners in depend on conditions.
 *
 * @author Ruslan Khmelyuk
 */
interface QueryRunner {

    /**
     * Search for subscribers using specified queries.
     *
     * @param queries the queries used to search subscribers.
     * @return the result of subscribers search.
     */
    Subscribers run(Queries queries)

}
