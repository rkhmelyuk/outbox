package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
class iPhoneDetectorTests extends GroovyTestCase {

    void testDetect() {
        def detector = new SafariDetector()

        def userAgent = 'Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3'
        def browser = detector.detect(userAgent.toLowerCase())
        assertEquals 'Safari', browser.name
        assertEquals '3.0', browser.version
    }
}
