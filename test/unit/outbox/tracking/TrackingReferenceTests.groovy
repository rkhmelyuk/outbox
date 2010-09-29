package outbox.tracking

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class TrackingReferenceTests extends GrailsUnitTestCase {

    void testFields() {
        TrackingReference reference = new TrackingReference()
        reference.id = 'abcdef0123456789'
        reference.campaignId = 2
        reference.campaignMessageId = '0123456789'
        reference.subscriberId = '0123456789abcdef'
        reference.reference = 'http://google.com'
        reference.type = TrackingReferenceType.Link

        assertEquals 'abcdef0123456789', reference.id
        assertEquals 2, reference.campaignId
        assertEquals '0123456789', reference.campaignMessageId
        assertEquals 'http://google.com', reference.reference
        assertEquals TrackingReferenceType.Link, reference.type
    }
}
