package outbox.subscriber

import grails.test.GrailsUnitTestCase
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberTypeTests extends GrailsUnitTestCase {

    void testFields() {
        SubscriberType type = new SubscriberType()
        type.id = 1
        type.name = 'Test Name'
        type.member = new Member(id: 1)

        assertEquals 1, type.id
        assertEquals 'Test Name', type.name
        assertEquals 1, type.member.id
    }
    
}
