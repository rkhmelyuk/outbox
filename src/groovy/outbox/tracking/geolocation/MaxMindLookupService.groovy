package outbox.tracking.geolocation

import com.maxmind.geoip.Location
import com.maxmind.geoip.LookupService

/**
 * @author Ruslan Khmelyuk
 */
class MaxMindLookupService {

    LookupService lookupService

    Location getLocation(String ipAddress) {
        lookupService.getLocation(ipAddress)
    }
}
