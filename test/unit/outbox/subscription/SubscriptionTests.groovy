package outbox.subscription

import grails.test.GrailsUnitTestCase
import outbox.subscriber.Subscriber

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionTests extends GrailsUnitTestCase {

    void testFields() {
        def date = new Date()

        Subscription subscription = new Subscription()
        subscription.id = 1
        subscription.subscriber = new Subscriber(id: '0123456789abcdef')
        subscription.status = new SubscriptionStatus(id: 2)
        subscription.subscriptionList = new SubscriptionList(id: 3)
        subscription.dateCreated = date

        assertEquals 1, subscription.id
        assertEquals '0123456789abcdef', subscription.subscriber.id
        assertEquals 2, subscription.status.id
        assertEquals 3, subscription.subscriptionList.id
        assertEquals date, subscription.dateCreated
    }

}
