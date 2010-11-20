package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class InListCriterionTests extends GroovyTestCase {

    void testFields() {
        def values = [10, 20, 30]

        def criterion = new InListCriterion()
        criterion.left = 'Language'
        criterion.values = values
        criterion.not = true

        assertEquals 'Language', criterion.left
        assertEquals  values, criterion.values
        assertTrue criterion.not
    }
}
