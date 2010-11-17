package outbox.subscriber.search.condition

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.ConditionVisitor

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionConditionTests extends GrailsUnitTestCase {

    void testSubscribed() {
        def ids = [1, 2, 3]
        def condition = SubscriptionCondition.subscribed(ids)
        condition.concatenation = Concatenation.And

        assertTrue condition.subscribedTo
        assertEquals ids, condition.subscriptionListIds
        assertEquals Concatenation.And, condition.concatenation
    }

    void testNotSubscribed() {
        def ids = [1, 2, 3]
        def condition = SubscriptionCondition.notSubscribed(ids)

        assertFalse condition.subscribedTo
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
