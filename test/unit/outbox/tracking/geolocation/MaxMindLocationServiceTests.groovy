package outbox.tracking.geolocation

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class MaxMindLocationServiceTests extends GrailsUnitTestCase {

    MaxMindGeoLocationService locationService

    @Override protected void setUp() {
        super.setUp();
        locationService = new MaxMindGeoLocationService()
    }

    void testLookupLocation() {
        def location = new com.maxmind.geoip.Location()
        location.city = 'Test City'
        location.region = 'Test Region'
        location.postalCode = 'Test Postal'
        location.countryCode = 'Test Country Code'
        location.countryName = 'Test Country Name'
        location.latitude = 0.12
        location.longitude = 1.12

        def lookupServiceControl = mockFor(MaxMindLookupService)
        lookupServiceControl.demand.getLocation {ip ->
            assertEquals '127.0.0.2', ip
            return location
        }
        locationService.lookupService = lookupServiceControl.createMock()

        def found = locationService.lookupLocation('127.0.0.2')
        lookupServiceControl.verify()

        assertEquals location.city, found.city
        assertEquals location.region, found.region
        assertEquals location.postalCode, found.postal
        assertEquals location.countryName, found.countryName
        assertEquals location.countryCode, found.countryCode
        assertEquals location.latitude, found.latitude
        assertEquals location.longitude, found.longitude
    }

    void testLookupLocation_NotFound() {
        def lookupServiceControl = mockFor(MaxMindLookupService)
        lookupServiceControl.demand.getLocation {ip ->
            assertEquals '127.0.0.2', ip
            return null
        }
        locationService.lookupService = lookupServiceControl.createMock()
        
        assertNull locationService.lookupLocation('127.0.0.2')
        lookupServiceControl.verify()
    }

}
