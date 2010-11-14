package outbox.subscriber.search.query

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.criteria.CriteriaVisitor

/**
 * @author Ruslan Khmelyuk
 */
class QueriesBuilderTests extends GrailsUnitTestCase {

    void testBuild() {
        def builder = new QueriesBuilder()

        def subscriptionTree = new CriteriaTree()
        def dynamicFieldTree = new CriteriaTree()
        def subscriberFieldTree = new CriteriaTree()
        def visitor = new CriteriaVisitor()

        visitor.subscriptionTree = subscriptionTree
        visitor.dynamicFieldTree = dynamicFieldTree
        visitor.subscriberFieldTree = subscriberFieldTree

        def subscriptionQueryBuilderControl = mockFor(QueryBuilder)
        def dynamicFieldQueryBuilderControl = mockFor(QueryBuilder)
        def subscriberFieldQueryBuilderControl = mockFor(QueryBuilder)
        subscriptionQueryBuilderControl.demand.build { it ->
            assertEquals subscriptionTree, it
            return new Query()
        }
        dynamicFieldQueryBuilderControl.demand.build { it ->
            assertEquals dynamicFieldTree, it
            return new Query()
        }
        subscriberFieldQueryBuilderControl.demand.build { it ->
            assertEquals subscriberFieldTree, it
            return new Query()
        }

        builder.subscriptionQueryBuilder = subscriptionQueryBuilderControl.createMock()
        builder.dynamicFieldQueryBuilder = dynamicFieldQueryBuilderControl.createMock()
        builder.subscriberFieldQueryBuilder = subscriberFieldQueryBuilderControl.createMock()

        def queries = builder.build(visitor)

        assertNotNull queries.subscriptionQuery
        assertNotNull queries.dynamicFieldQuery
        assertNotNull queries.subscriberFieldQuery

        subscriptionQueryBuilderControl.verify()
        dynamicFieldQueryBuilderControl.verify()
        subscriberFieldQueryBuilderControl.verify()

    }
}
