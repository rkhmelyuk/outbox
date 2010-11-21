package outbox.subscriber.search.runner

import outbox.subscriber.search.query.Queries

/**
 * Used to detect the best fit query runner implementation.
 * 
 * @author Ruslan Khmelyuk
 */
class QueryRunnerDetector {

    QueryRunner singleQueryRunner

    /**
     * Detects what query runner should be used to search by specified queries.
     *
     * @param queries the queries to search by.
     * @return the best fit query runner implementation.
     */
    QueryRunner detect(Queries queries) {
        singleQueryRunner
    }
}
