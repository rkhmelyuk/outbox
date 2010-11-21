package outbox.subscriber.search.query

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.Conditions
import outbox.subscriber.search.CriteriaVisitor
import outbox.subscriber.search.criteria.CriteriaTree

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

        visitor.subscriptionTrees = [subscriptionTree, subscriptionTree]
        visitor.dynamicFieldTrees = [dynamicFieldTree, dynamicFieldTree, dynamicFieldTree]
        visitor.subscriberFieldTree = subscriberFieldTree

        def subscriptionQueryBuilderControl = mockFor(QueryBuilder)
        def dynamicFieldQueryBuilderControl = mockFor(QueryBuilder)
        def subscriberFieldQueryBuilderControl = mockFor(QueryBuilder)
        subscriptionQueryBuilderControl.demand.build(2) { it ->
            assertEquals subscriptionTree, it
            return new Query()
        }
        dynamicFieldQueryBuilderControl.demand.build(3) { it ->
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

        def conditions = new Conditions(page: 1, perPage: 10)
        conditions.orderBy 'FirstName'
        def queries = builder.build(conditions, visitor)

        assertNotNull queries.subscriptionQueries
        assertNotNull queries.dynamicFieldQueries
        assertNotNull queries.subscriberFieldQuery
        assertEquals 1, queries.subscriberFieldQuery.page
        assertEquals 10, queries.subscriberFieldQuery.perPage

        def orders = queries.subscriberFieldQuery.orders
        assertEquals 1, orders.size()
        assertEquals 'FirstName', orders.first().column.name

        subscriptionQueryBuilderControl.verify()
        dynamicFieldQueryBuilderControl.verify()
        subscriberFieldQueryBuilderControl.verify()
    }
}
