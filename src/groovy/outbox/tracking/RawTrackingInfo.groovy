package outbox.tracking

/**
 * @author Ruslan Khmelyuk
 */
class RawTrackingInfo {

    TrackingReference reference
    long timestamp

    String remoteAddress
    String remoteHost
    String remoteUser

    String userAgentHeader
    String acceptLanguageHeader
    
}
