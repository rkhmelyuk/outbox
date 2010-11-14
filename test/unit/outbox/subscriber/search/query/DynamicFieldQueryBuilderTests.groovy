package outbox.subscriber.search.query

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.criteria.CriterionNode

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldQueryBuilderTests extends GrailsUnitTestCase {

    void testBuild() {
        def criteriaTree = new CriteriaTree()
        criteriaTree.addNode(new CriterionNode())

        def queryBuilder = new DynamicFieldQueryBuilder()
        def query = queryBuilder.build(criteriaTree)

        assertEquals criteriaTree, query.criteria
        assertFalse query.columns.empty
        assertFalse query.tables.empty
        assertFalse query.joins.empty
        assertTrue query.distinct
    }

    void testBuild_Empty() {
        def queryBuilder = new DynamicFieldQueryBuilder()
        assertNull queryBuilder.build(new CriteriaTree())
    }

}
