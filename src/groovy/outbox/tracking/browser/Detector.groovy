package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
static interface Detector {

    String UNKNOWN = 'unknown'
    
    Browser detect(String userAgentHeader)

}
