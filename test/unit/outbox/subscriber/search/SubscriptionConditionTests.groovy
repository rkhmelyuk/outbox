package outbox.subscriber.search

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionConditionTests extends GrailsUnitTestCase {

    void testSubscribed() {
        def ids = [1, 2, 3]
        def condition = SubscriptionCondition.subscribed(ids)
        condition.concatenation = Concatenation.And

        assertTrue condition.subscribed
        assertEquals ids, condition.subscriptionListIds
        assertEquals Concatenation.And, condition.concatenation
    }

    void testNotSubscribed() {
        def ids = [1, 2, 3]
        def condition = SubscriptionCondition.notSubscribed(ids)

        assertFalse condition.subscribed
        assertEquals ids, condition.subscriptionListIds
    }

    void testVisit() {
        def condition = SubscriptionCondition.subscribed([])

        def visitorControl = mockFor(ConditionVisitor)
        visitorControl.demand.visitSubscriptionCondition { it ->
            assertEquals condition, it
        }

        condition.visit visitorControl.createMock()

        visitorControl.verify()
    }
}
