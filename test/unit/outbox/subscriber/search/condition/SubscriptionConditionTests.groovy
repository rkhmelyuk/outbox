package outbox.subscriber.search.condition

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.ConditionVisitor

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionConditionTests extends GrailsUnitTestCase {

    void testSubscribed() {
        def id = 2
        def condition = SubscriptionCondition.subscribed(id)
        condition.concatenation = Concatenation.And

        assertTrue condition.subscribedTo
        assertEquals id, condition.subscriptionListId
        assertEquals Concatenation.And, condition.concatenation
    }

    void testNotSubscribed() {
        def id = 2
        def condition = SubscriptionCondition.notSubscribed(id)

        assertFalse condition.subscribedTo
        assertEquals id, condition.subscriptionListId
    }

    void testVisit() {
        def condition = SubscriptionCondition.subscribed(null)

        def visitorControl = mockFor(ConditionVisitor)
        visitorControl.demand.visitSubscriptionCondition { it ->
            assertEquals condition, it
        }

        condition.visit visitorControl.createMock()

        visitorControl.verify()
    }
}
