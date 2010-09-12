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

    void testFullName() {
        assertEquals 'Test User', new Member(firstName: 'Test', lastName: 'User').fullName
        assertEquals '', new Member(firstName: null, lastName: null).fullName
        assertEquals 'User', new Member(firstName: null, lastName: 'User').fullName
        assertEquals 'Test', new Member(firstName: 'Test', lastName: null).fullName
    }
}
