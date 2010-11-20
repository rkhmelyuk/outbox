package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class InSubqueryCriterionTests extends GroovyTestCase {

    void testFields() {
        def subquery = 'subquery'

        def criterion = new InSubqueryCriterion()
        criterion.left = 'Language'
        criterion.subquery = subquery
        criterion.not = true

        assertEquals 'Language', criterion.left
        assertEquals  subquery, criterion.subquery
        assertTrue criterion.not
    }
}
