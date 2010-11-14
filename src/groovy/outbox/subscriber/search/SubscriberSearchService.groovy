package outbox.subscriber.search

import outbox.subscriber.search.condition.Conditions
import outbox.subscriber.search.criteria.CriteriaVisitor
import outbox.subscriber.search.query.QueriesBuilder
import outbox.subscriber.search.runner.QueryRunnerDetector

/**
 * @author Ruslan Khmelyuk
 * @created 2010-11-14
 */
class SubscriberSearchService {

    QueryRunnerDetector queryRunnerDetector
    QueriesBuilder queriesBuilder = new QueriesBuilder()

    Subscribers search(Conditions conditions) {

        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queries = queriesBuilder.build(visitor)
        def runner = queryRunnerDetector.detect(queries)

        runner.run(queries)
    }
}
