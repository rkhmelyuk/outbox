package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
static class UnknownDetector implements Detector {

    Browser detect(String userAgentHeader) {
        def result = new Browser()
        result.name = UNKNOWN
        result.version = UNKNOWN
        return result
    }
}
