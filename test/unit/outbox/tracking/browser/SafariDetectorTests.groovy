package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
class SafariDetectorTests extends GroovyTestCase {

    void testDetect() {
        def detector = new SafariDetector()

        def userAgent = 'Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8'
        def browser = detector.detect(userAgent.toLowerCase())
        assertEquals 'Safari', browser.name
        assertEquals '5.0.1', browser.version

        userAgent = 'Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_3; ru-ru) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.1'
        browser = detector.detect(userAgent.toLowerCase())
        assertEquals 'Safari', browser.name
        assertEquals '5.0', browser.version

        userAgent = 'Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_7; en-us) AppleWebKit/533.4 (KHTML, like Gecko) Version/4.1 Safari/533.4'
        browser = detector.detect(userAgent.toLowerCase())
        assertEquals 'Safari', browser.name
        assertEquals '4.1', browser.version

        userAgent = 'Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/521.32.1 (KHTML, like Gecko) Safari/521.32.1'
        browser = detector.detect(userAgent.toLowerCase())
        assertEquals 'Safari', browser.name
        assertEquals 'unknown', browser.version
    }
}
