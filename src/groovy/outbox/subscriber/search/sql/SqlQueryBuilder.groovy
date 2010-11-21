package outbox.subscriber.search.sql

import outbox.subscriber.search.query.Query

/**
 * Builds SQL query for {@link outbox.subscriber.search.query.Query} object and return as string.
 *
 * @author Ruslan Khmelyuk
 */
interface SqlQueryBuilder {

    /**
     * Builds SQL query.
     * @param query the query to build SQL for.
     * @return the SQL query as string.
     */
    String build(Query query)

}
