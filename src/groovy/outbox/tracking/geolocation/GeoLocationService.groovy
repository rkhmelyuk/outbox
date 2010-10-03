package outbox.tracking.geolocation

import outbox.tracking.Location

/**
 * @author Ruslan Khmelyuk
 */
interface GeoLocationService {

    /**
     * Looks up the location for specified ip address.
     *
     * @param ipAddress the ip address; can't be null or empty.
     * @return the found location.
     * @throws Exception error to find the location.
     */
    Location lookupLocation(String ipAddress)
}
