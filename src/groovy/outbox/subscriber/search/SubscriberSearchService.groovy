package outbox.subscriber.search

import outbox.subscriber.DynamicFieldService
import outbox.subscriber.search.query.QueriesBuilder
import outbox.subscriber.search.runner.QueryRunnerDetector

/**
 * Search used to search for subscribers.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriberSearchService {

    DynamicFieldService dynamicFieldService
    QueryRunnerDetector queryRunnerDetector
    QueriesBuilder queriesBuilder = new QueriesBuilder()

    Subscribers search(Conditions conditions) {

        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queries = queriesBuilder.build(conditions, visitor)
        def runner = queryRunnerDetector.detect(queries)

        runner.run(queries)
    }

    String describe(Conditions conditions) {
        def visitor = new ReadableConditionVisitor()
        conditions.visit(visitor)
        visitor.readableString
    }
}
