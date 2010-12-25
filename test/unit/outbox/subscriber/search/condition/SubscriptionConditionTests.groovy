package outbox.subscriber.search.condition

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.ConditionVisitor
import outbox.subscription.SubscriptionList

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionConditionTests extends GrailsUnitTestCase {

    void testSubscribed() {
        def list = new SubscriptionList()
        def condition = SubscriptionCondition.subscribed(list)
        condition.concatenation = Concatenation.And

        assertTrue condition.subscribedTo
        assertEquals list, condition.subscriptionList
        assertEquals Concatenation.And, condition.concatenation
    }

    void testNotSubscribed() {
        def list = new SubscriptionList()
        def condition = SubscriptionCondition.notSubscribed(list)

        assertFalse condition.subscribedTo
        assertEquals list, condition.subscriptionList
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
