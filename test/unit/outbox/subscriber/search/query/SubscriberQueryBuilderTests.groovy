package outbox.subscriber.search.query

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.criteria.CriteriaTree

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberQueryBuilderTests extends GrailsUnitTestCase {

    void testBuild() {
        def criteriaTree = new CriteriaTree()

        def queryBuilder = new SubscriberQueryBuilder()
        def query = queryBuilder.build(criteriaTree)

        assertEquals criteriaTree, query.criteria
        assertFalse query.columns.empty
        assertFalse query.tables.empty
        assertTrue query.distinct
    }

}
