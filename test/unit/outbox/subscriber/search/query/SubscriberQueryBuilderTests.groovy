package outbox.subscriber.search.query

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.criteria.CriterionNode

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberQueryBuilderTests extends GrailsUnitTestCase {

    void testBuild() {
        def criteriaTree = new CriteriaTree()
        criteriaTree.addNode(new CriterionNode())

        def queryBuilder = new SubscriberQueryBuilder()
        def query = queryBuilder.build(criteriaTree)

        assertEquals criteriaTree, query.criteria
        assertFalse query.columns.empty
        assertFalse query.tables.empty
        assertTrue query.joins.empty
        assertTrue query.distinct
    }

    void testBuild_Empty() {
        def queryBuilder = new SubscriberQueryBuilder()
        assertNull queryBuilder.build(new CriteriaTree())
    }

}
