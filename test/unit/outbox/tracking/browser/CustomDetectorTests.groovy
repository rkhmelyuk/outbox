package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
class CustomDetectorTests extends GroovyTestCase {
    
    void testDetect() {
        def detector = new CustomDetector('Firefox', 'firefox/')

        def userAgent = 'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.6) Gecko/20050614 Firefox/0.8'
        def browser = detector.detect(userAgent.toLowerCase())

        assertEquals 'Firefox', browser.name
        assertEquals '0.8', browser.version
    }

    void testDetect_Null() {
        def detector = new CustomDetector('Firefox', 'firefox/')
        assertNull detector.detect(null)
    }

    void testDetect_Unknown() {
        def detector = new CustomDetector('Firefox', 'firefox/')
        assertNull detector.detect('uknown')
    }
}
