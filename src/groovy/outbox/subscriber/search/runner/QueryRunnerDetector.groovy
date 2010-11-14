package outbox.subscriber.search.runner

import outbox.subscriber.search.query.Queries

/**
 * Used to detect the best fit query runner implementation.
 * 
 * @author Ruslan Khmelyuk
 */
class QueryRunnerDetector {

    QueryRunner singleQueryRunner

    QueryRunner detect(Queries queries) {
        singleQueryRunner
    }
}
