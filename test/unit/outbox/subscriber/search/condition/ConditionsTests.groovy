package outbox.subscriber.search.condition

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.ConditionVisitor
import outbox.subscriber.search.Conditions
import outbox.subscriber.search.Sort

/**
 * @author Ruslan Khmelyuk
 * @created 2010-11-13
 */
class ConditionsTests extends GrailsUnitTestCase {

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

    void testAdd_Single() {
        def condition = new SubscriberFieldCondition(null, null)
        condition.concatenation = Concatenation.Or
        conditions.add(condition)
        assertEquals Concatenation.Or, conditions.conditions.first().concatenation
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

    void testVisit() {
        def condition1 = new SubscriberFieldCondition(null, null)
        conditions.and(condition1)
        def condition2 = new DynamicFieldCondition(null, null)
        conditions.and(condition2)
        def condition3 = new SubscriptionCondition(false, null)
        conditions.and(condition3)

        def visitorControl = mockFor(ConditionVisitor)
        visitorControl.demand.visitSubscriberFieldCondition { it ->
            assertEquals condition1, it
        }
        visitorControl.demand.visitDynamicFieldCondition { it ->
            assertEquals condition2, it
        }
        visitorControl.demand.visitSubscriptionCondition { it ->
            assertEquals condition3, it
        }

        conditions.visit visitorControl.createMock()

        visitorControl.verify()
    }

    void testOrderBy() {
        def conditions = new Conditions()

        conditions.orderBy('FirstName')

        assertFalse conditions.orders.empty
        assertEquals 'FirstName', conditions.orders.first().column
        assertEquals Sort.Asc, conditions.orders.first().sort
    }

    void testOrderBy_Desc() {
        def conditions = new Conditions()

        conditions.orderBy('FirstName', Sort.Desc)

        assertFalse conditions.orders.empty
        assertEquals 'FirstName', conditions.orders.first().column
        assertEquals Sort.Desc, conditions.orders.first().sort
    }

    void testOrderBy_Null() {
        def conditions = new Conditions()
        conditions.orderBy(null, Sort.Desc)
        assertTrue conditions.orders.empty
    }
}
