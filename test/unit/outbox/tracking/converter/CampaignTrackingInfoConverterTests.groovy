package outbox.tracking.converter

import grails.test.GrailsUnitTestCase
import outbox.AppConstant
import outbox.tracking.Location
import outbox.tracking.RawTrackingInfo
import outbox.tracking.TrackingReference
import outbox.tracking.TrackingReferenceType
import outbox.tracking.geolocation.GeoLocationService

/**
 * @author Ruslan Khmelyuk
 */
class CampaignTrackingInfoConverterTests extends GrailsUnitTestCase {

    void testConvert() {
        def rawTrackingInfo = new RawTrackingInfo()
        rawTrackingInfo.userAgentHeader = 'Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8'
        rawTrackingInfo.remoteAddress = '192.12.93.122'
        rawTrackingInfo.remoteHost = 'core'
        rawTrackingInfo.remoteUser = 'guest'
        rawTrackingInfo.reference = new TrackingReference(id: '3', campaignId: 1,
                subscriberId: 'abcdef00', type: TrackingReferenceType.Link)
        rawTrackingInfo.acceptLanguageHeader = 'en-US'

        def geoLocationServiceControl = mockFor(GeoLocationService)
        geoLocationServiceControl.demand.lookupLocation { ipAddress ->
            assertEquals rawTrackingInfo.remoteAddress, ipAddress
            return new Location(
                    city: 'New York',
                    region: 'Mann',
                    postal: '9090',
                    countryCode: 'US',
                    countryName: 'USA',
                    longitude: 0.22,
                    latitude: 0.12
            )
        }

        def converter = new CampaignTrackingInfoConverter()
        converter.geoLocationService = geoLocationServiceControl.createMock()

        def trackingInfo = converter.convert(rawTrackingInfo)
        geoLocationServiceControl.verify()

        assertEquals 'Safari', trackingInfo.browserName
        assertEquals '5.0.1', trackingInfo.browserVersion
        assertEquals 1, trackingInfo.campaignId
        assertEquals 'New York', trackingInfo.city
        assertEquals '3', trackingInfo.trackingReferenceId
        assertEquals 'abcdef00', trackingInfo.subscriberId
        assertEquals 'Mann', trackingInfo.region
        assertEquals '9090', trackingInfo.postalCode
        assertEquals 'US', trackingInfo.countryCode
        assertEquals 'USA', trackingInfo.countryName
        assertEquals '192.12.93.122', trackingInfo.ipAddress
        assertEquals 0.12, trackingInfo.latitude
        assertEquals 0.22, trackingInfo.longitude
        assertEquals 'Windows Vista', trackingInfo.operatingSystem
        assertEquals 'en-US', trackingInfo.locale
        assertTrue trackingInfo.click
    }

    void testConvert_Ping() {
        def rawTrackingInfo = new RawTrackingInfo()
        rawTrackingInfo.userAgentHeader = 'Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8'
        rawTrackingInfo.remoteAddress = '192.12.93.122'
        rawTrackingInfo.remoteHost = 'core'
        rawTrackingInfo.remoteUser = 'guest'
        rawTrackingInfo.reference = new TrackingReference(id: '3', campaignId: 1,
                subscriberId: 'abcdef00', type: TrackingReferenceType.Resource,
                reference: AppConstant.OPEN_PING_RESOURCE)

        def geoLocationServiceControl = mockFor(GeoLocationService)
        geoLocationServiceControl.demand.lookupLocation { ipAddress ->
            assertEquals rawTrackingInfo.remoteAddress, ipAddress
            return null
        }

        def converter = new CampaignTrackingInfoConverter()
        converter.geoLocationService = geoLocationServiceControl.createMock()

        def trackingInfo = converter.convert(rawTrackingInfo)
        geoLocationServiceControl.verify()

        assertFalse trackingInfo.click
        assertTrue trackingInfo.open
    }

    void testConvert_Resource() {
        def rawTrackingInfo = new RawTrackingInfo()
        rawTrackingInfo.userAgentHeader = 'Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8'
        rawTrackingInfo.remoteAddress = '192.12.93.122'
        rawTrackingInfo.remoteHost = 'core'
        rawTrackingInfo.remoteUser = 'guest'
        rawTrackingInfo.reference = new TrackingReference(id: '3', campaignId: 1,
                subscriberId: 'abcdef00', type: TrackingReferenceType.Resource,
                reference: 'image.jpg')

        def geoLocationServiceControl = mockFor(GeoLocationService)
        geoLocationServiceControl.demand.lookupLocation { ipAddress ->
            assertEquals rawTrackingInfo.remoteAddress, ipAddress
            return null
        }

        def converter = new CampaignTrackingInfoConverter()
        converter.geoLocationService = geoLocationServiceControl.createMock()

        def trackingInfo = converter.convert(rawTrackingInfo)
        geoLocationServiceControl.verify()

        assertFalse trackingInfo.click
        assertFalse trackingInfo.open
    }
}
