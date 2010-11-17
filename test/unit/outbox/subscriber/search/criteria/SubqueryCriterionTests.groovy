package outbox.subscriber.search.criteria

import outbox.subscriber.search.query.Query

/**
 * @author Ruslan Khmelyuk
 */
class SubqueryCriterionTests extends GroovyTestCase {

    void testFields() {
        def criterion = new SubqueryCriterion()

        def query = new Query()
        criterion.subquery = query
        criterion.condition = SubqueryConditionType.Exists

        assertEquals query, criterion.subquery
        assertEquals SubqueryConditionType.Exists, criterion.condition
    }

}
