package outbox.tracking

/**
 * @author Ruslan Khmelyuk
 */
class RawTrackingInfoTests extends GroovyTestCase {

    void testFields() {
        def trackingInfo = new RawTrackingInfo()
        trackingInfo.acceptLanguageHeader = 'Test Accept Language Header'
        trackingInfo.reference = new TrackingReference(id: 'abcdef01')
        trackingInfo.userAgentHeader = 'Test User Agent Header'
        trackingInfo.remoteAddress = '192.134.13.11'
        trackingInfo.remoteHost = 'tank'
        trackingInfo.remoteUser = 'user'
        trackingInfo.timestamp = 123

        assertEquals 'Test Accept Language Header', trackingInfo.acceptLanguageHeader
        assertEquals 'abcdef01', trackingInfo.reference.id
        assertEquals 'Test User Agent Header', trackingInfo.userAgentHeader
        assertEquals '192.134.13.11', trackingInfo.remoteAddress
        assertEquals 'tank', trackingInfo.remoteHost
        assertEquals 'user', trackingInfo.remoteUser
        assertEquals 123, trackingInfo.timestamp
    }
}
