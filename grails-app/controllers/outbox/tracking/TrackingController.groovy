package outbox.tracking

/**
 * @author Ruslan Khmelyuk
 */
class TrackingController {

    def trackingService
    def grailsApplication

    def track = {
        def trackingReference = trackingService.getTrackingReference(params.id)

        if (!trackingReference) {
            // if reference is not found
            redirect url: grailsApplication.config.outbox.application.url
            return
        }

        def rawTrackingInfo = new RawTrackingInfo()
        rawTrackingInfo.reference = trackingReference
        rawTrackingInfo.timestamp = System.currentTimeMillis()
        rawTrackingInfo.acceptLanguageHeader = request.getHeader('Accept-Language')
        rawTrackingInfo.userAgentHeader = request.getHeader('User-Agent')
        rawTrackingInfo.remoteUser = request.remoteUser
        rawTrackingInfo.remoteAddress = request.remoteAddr
        rawTrackingInfo.remoteHost = request.remoteHost

        trackingService.track rawTrackingInfo

        redirect url: trackingReference.reference
    }
}
