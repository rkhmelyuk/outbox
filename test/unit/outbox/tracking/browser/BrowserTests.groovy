package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
class BrowserTests extends GroovyTestCase {

    void testFields() {
        def browser = new Browser()
        browser.name = 'Test Name'
        browser.version = 'Test Version'

        assertEquals 'Test Name', browser.name
        assertEquals 'Test Version', browser.version
    }

}
