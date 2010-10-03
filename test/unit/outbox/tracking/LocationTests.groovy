package outbox.tracking

/**
 * @author Ruslan Khmelyuk
 */
class LocationTests extends GroovyTestCase {

    void testFields() {
        def location = new Location()
        location.city = 'Test City'
        location.region = 'Test Region'
        location.countryCode = 'Test Country Code'
        location.countryName = 'Test Country Name'
        location.postal = '00000'
        location.latitude = 0.123
        location.longitude = 0.345

        assertEquals 'Test City', location.city
        assertEquals 'Test Region', location.region
        assertEquals 'Test Country Code', location.countryCode
        assertEquals 'Test Country Name', location.countryName
        assertEquals '00000', location.postal
        assertEquals 0.123, location.latitude
        assertEquals 0.345, location.longitude
    }
}
