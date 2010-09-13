package outbox.subscription

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionStatusTests extends GrailsUnitTestCase {

    void testFields() {
        SubscriptionStatus status = new SubscriptionStatus()
        status.id = 1
        status.name = 'Test Status'

        assertEquals 1, status.id
        assertEquals 'Test Status', status.name
    }

    void testStatic() {
        SubscriptionStatus.class.metaClass.static.get = { id ->
            return new SubscriptionStatus(id: id)
        }

        assertEquals 1, SubscriptionStatus.subscribed().id
        assertEquals 2, SubscriptionStatus.unsubscribed().id
        assertEquals 3, SubscriptionStatus.unsubscribedByRecipient().id
    }
}
