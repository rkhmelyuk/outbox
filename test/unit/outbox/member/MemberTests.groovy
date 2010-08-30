package outbox.member

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 * @since  2010-08-29
 */
class MemberTests extends GrailsUnitTestCase {

    void testValidateBlankFields() {
        mockForConstraintsTests(Member)

        def member = new Member()
        member.firstName = ''
        member.lastName = ''
        member.username = ''

        assertFalse member.validate()
        
        assertEquals 'blank', member.errors['firstName']
        assertEquals 'blank', member.errors['lastName']
        assertEquals 'blank', member.errors['username']
    }
}
