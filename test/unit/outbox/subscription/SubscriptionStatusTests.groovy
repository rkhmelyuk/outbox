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
}
