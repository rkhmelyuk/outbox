package outbox.tracking

import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil

/**
 * @author Ruslan Khmelyuk
 */
class TrackingService {

    static transactional = true

    /**
     * Saves tracking reference.
     * @param reference the tracking reference to save.
     * @return true if tracking reference is saved, otherwise false.
     */
    @Transactional
    boolean addTrackingReference(TrackingReference reference) {
        ServiceUtil.saveOrRollback reference
    }

    /**
     * Gets tracking reference by id.
     * @param id the tracking reference id.
     * @return the found tracking reference or null.
     */
    @Transactional(readOnly=true)
    TrackingReference getTrackingReference(String id) {
        TrackingReference.findById id
    }
    
}
