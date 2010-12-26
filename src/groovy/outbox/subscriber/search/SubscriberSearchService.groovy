package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.SubscriberService
import outbox.subscriber.search.query.QueriesBuilder
import outbox.subscriber.search.runner.QueryRunnerDetector

/**
 * Search used to search for subscribers.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriberSearchService {

    SubscriberService subscriberService
    DynamicFieldService dynamicFieldService
    QueryRunnerDetector queryRunnerDetector
    SpringSecurityService springSecurityService
    QueriesBuilder queriesBuilder = new QueriesBuilder()

    Subscribers search(Conditions conditions) {

        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queries = queriesBuilder.build(conditions, visitor)
        def runner = queryRunnerDetector.detect(queries)

        runner.run(queries)
    }

    String describe(Conditions conditions) {
        def visitor = new ReadableConditionVisitor(subscriberService, springSecurityService)
        conditions.visit(visitor)
        return visitor.readableString
    }
}
