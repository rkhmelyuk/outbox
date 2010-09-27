package outbox.mail

/**
 * @author Ruslan Khmelyuk
 * @since  2010-09-27
 */
class EmailUtilTests extends GroovyTestCase {

    void testEmailAddress() {
        assertEquals '"John" <john@mailsight.com>', EmailUtil.emailAddress('John', 'john@mailsight.com')
        assertEquals '"John Smith" <john@mailsight.com>', EmailUtil.emailAddress('John Smith', 'john@mailsight.com')
        assertEquals '"john@mailsight.com" <john@mailsight.com>', EmailUtil.emailAddress('', 'john@mailsight.com')
        assertEquals '"john@mailsight.com" <john@mailsight.com>', EmailUtil.emailAddress(null, 'john@mailsight.com')
        assertNull EmailUtil.emailAddress(null, null)
        assertNull EmailUtil.emailAddress('John Smith', null)
    }
}
