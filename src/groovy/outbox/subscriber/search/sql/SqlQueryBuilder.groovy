package outbox.subscriber.search.sql

import outbox.subscriber.search.query.Query

/**
 * Builds SQL query for {@link outbox.subscriber.search.query.Query} object and return as string.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-11-21
 */
interface SqlQueryBuilder {

    String build(Query query)
}
