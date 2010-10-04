package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
class UnknownDetectorTests extends GroovyTestCase {

    void testDetect() {
        def detector = new UnknownDetector()

        def browser = detector.detect(null)
        assertEquals Detector.UNKNOWN, browser.name
        assertEquals Detector.UNKNOWN, browser.version
    }
}
