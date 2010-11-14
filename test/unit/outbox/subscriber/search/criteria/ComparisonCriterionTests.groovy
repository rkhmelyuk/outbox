package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class ComparisonCriterionTests extends GroovyTestCase {

    void testFields() {
        def criterion = new ComparisonCriterion()
        criterion.left = 'FirstName'
        criterion.right = 'John'
        criterion.comparisonOp = '='

        assertEquals 'FirstName', criterion.left
        assertEquals 'John', criterion.right
        assertEquals '=', criterion.comparisonOp
    }

}
