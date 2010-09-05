package outbox.member

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 * @since  2010-08-29
 */
class MemberTests extends GrailsUnitTestCase {

    void testFields() {


        def member = new Member()
        member.firstName = 'First'
        member.lastName = 'Last'
        member.username = 'username'
        member.password = '123'

        assertEquals 'First', member.firstName
        assertEquals 'Last', member.lastName
        assertEquals 'username', member.username
        assertEquals '123', member.password
    }
}
