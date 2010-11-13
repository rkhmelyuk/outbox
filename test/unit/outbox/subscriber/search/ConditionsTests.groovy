package outbox.subscriber.search

/**
 * @author Ruslan Khmelyuk
 * @created 2010-11-13
 */
class ConditionsTests extends GroovyTestCase {

    def conditions

    @Override protected void setUp() {
        super.setUp()

        conditions = new Conditions()
    }

    void testAdd() {
        conditions.add(Concatenation.Or, null)
        assertTrue conditions.conditions.empty

        def condition = new SubscriberFieldCondition(null, null)
        conditions.add(null, condition)
        assertTrue conditions.conditions.empty

        conditions.add(Concatenation.And, condition)
        assertEquals 1, conditions.conditions.size()
        assertEquals Concatenation.And, condition.concatenation
    }

    void testAnd() {
        def condition = new SubscriberFieldCondition(null, null)
        conditions.and(condition)
        assertEquals 1, conditions.conditions.size()
        assertEquals Concatenation.And, condition.concatenation
    }

    void testOr() {
        def condition = new SubscriberFieldCondition(null, null)
        conditions.or(condition)
        assertEquals 1, conditions.conditions.size()
        assertEquals Concatenation.Or, condition.concatenation
    }

    void testAndNot() {
        def condition = new SubscriberFieldCondition(null, null)
        conditions.andNot(condition)
        assertEquals 1, conditions.conditions.size()
        assertEquals Concatenation.AndNot, condition.concatenation
    }

    void testOrNot() {
        def condition = new SubscriberFieldCondition(null, null)
        conditions.orNot(condition)
        assertEquals 1, conditions.conditions.size()
        assertEquals Concatenation.OrNot, condition.concatenation
    }

    void testEmpty() {
        assertTrue conditions.empty

        conditions.and(new SubscriberFieldCondition(null, null))
        assertFalse conditions.empty
    }
}
