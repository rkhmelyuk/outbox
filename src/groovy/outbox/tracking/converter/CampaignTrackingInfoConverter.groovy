package outbox.tracking.converter

import org.apache.log4j.Logger
import outbox.tracking.RawTrackingInfo
import outbox.tracking.TrackingInfo
import outbox.tracking.TrackingReferenceType
import outbox.tracking.UserAgentInfo
import outbox.tracking.geolocation.GeoLocationService

/**
 * The default tracking info converter.
 *
 * @author Ruslan Khmelyuk
 * @since 2009-11-11
 */
class CampaignTrackingInfoConverter implements TrackingInfoConverter {

    static final Logger log = Logger.getLogger(CampaignTrackingInfoConverter.class)

    GeoLocationService geoLocationService

    TrackingInfo convert(RawTrackingInfo rawTrackingInfo) {
        def trackingInfo = new TrackingInfo()
        trackingInfo.ipAddress = rawTrackingInfo.remoteAddress
        trackingInfo.datetime = new Date(rawTrackingInfo.timestamp)

        def reference = rawTrackingInfo.reference
        trackingInfo.campaignId = reference.campaignId
        trackingInfo.subscriberId = reference.subscriberId
        trackingInfo.trackingReferenceId = reference.id
        trackingInfo.click = reference?.type == TrackingReferenceType.Link

        fillUserAgentInformation(trackingInfo, rawTrackingInfo)
        fillLanguageInformation(trackingInfo, rawTrackingInfo)
        fillLocationInformation(trackingInfo, rawTrackingInfo)

        return trackingInfo
    }

    private void fillUserAgentInformation(TrackingInfo trackingInfo, RawTrackingInfo rawTrackingInfo) {
        if (!rawTrackingInfo.userAgentHeader) return

        try {
            def info = new UserAgentInfo(rawTrackingInfo.userAgentHeader)
            
            info.parse()

            def browser = info.browser
            trackingInfo.browserName = browser.name
            trackingInfo.browserVersion = browser.version
            trackingInfo.operatingSystem = info.operatingSystem
        }
        catch (Exception e) {
            log.error 'Can\'t get browser/OS for tracking request, cause:', e
        }
    }

    private void fillLanguageInformation(TrackingInfo trackingInfo, RawTrackingInfo rawTrackingInfo) {
        if (!rawTrackingInfo.acceptLanguageHeader) return

        try {
            String[] acceptedLocales = rawTrackingInfo.acceptLanguageHeader.split(LOCALES_SEPARATOR)
            if (acceptedLocales.length != 0) {
                String locale = acceptedLocales[0]
                int optionColonIndex = locale.indexOf(OPTIONS_SEPARATOR)
                if (optionColonIndex != -1) {
                    locale = locale.substring(0, optionColonIndex)
                }
                trackingInfo.locale = locale
            }
        }
        catch (Exception e) {
            log.error 'Can\'t get language information for tracking request, cause:', e
        }
    }

    private void fillLocationInformation(TrackingInfo trackingInfo, RawTrackingInfo rawTrackingInfo) {
        def ipAddress = rawTrackingInfo.remoteAddress
        if (!ipAddress) return

        try {
            def location = geoLocationService.lookupLocation(trackingInfo.ipAddress)

            trackingInfo.city = location.city
            trackingInfo.countryCode = location.countryCode
            trackingInfo.countryName = location.countryName
            trackingInfo.region = location.region
            trackingInfo.postalCode = location.postal
            trackingInfo.latitude = location.latitude
            trackingInfo.longitude = location.longitude

        }
        catch (Exception e) {
            log.error 'Can\'t get IP location information for tracking request, cause:', e
        }
    }

}
