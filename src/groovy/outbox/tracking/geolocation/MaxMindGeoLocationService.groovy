package outbox.tracking.geolocation

import org.apache.log4j.Logger
import outbox.tracking.Location

/**
 * @author Ruslan Khmelyuk
 */
class MaxMindGeoLocationService implements GeoLocationService {

    private static final Logger log = Logger.getLogger(MaxMindGeoLocationService.class)

    MaxMindLookupService lookupService

    Location lookupLocation(String ipAddress) {

        def location = lookupService.getLocation(ipAddress)

        if (!location) {
            // location is not found for specified address
            return null
        }

        def result = new Location()

        result.city  = location.city
        result.countryCode = location.countryCode
        result.setCountryName(location.countryName)
        result.setLatitude(location.latitude)
        result.setLongitude(location.longitude)
        result.setPostal(location.postalCode)
        result.setRegion(location.region)
        
        return result
    }
}
