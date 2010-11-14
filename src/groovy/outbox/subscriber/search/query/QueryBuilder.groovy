package outbox.subscriber.search.query

import outbox.subscriber.search.criteria.CriteriaTree

/**
 * The builder for the query.
 * Query builders are responsible to fill query with all important parts,
 * like column names, table names etc.
 *
 * @author Ruslan Khmelyuk
 */
interface QueryBuilder {

    /**
     * Builds the query with specified criteria.
     * Implementation must add columns, tables and joins if need.
     *
     * @param criteria the criteria for the query.
     * @return the ready to use query.
     */
    Query build(CriteriaTree criteria)
}
