package outbox.tracking

import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil
import outbox.tracking.converter.TrackingInfoConverterFactory

/**
 * @author Ruslan Khmelyuk
 */
class TrackingService {

    static transactional = true

    TrackingInfoConverterFactory trackingInfoConverterFactory

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
     * Adds tracking references.
     * @param references the collection of tracking references to save.
     * @return true if tracking references are saved, otherwise false.
     */
    @Transactional
    boolean addTrackingReferences(Collection<TrackingReference> references) {
        for (TrackingReference reference in references) {
            if (reference) {
                if (!ServiceUtil.saveOrRollback(reference)) {
                    return false
                }
            }
        }
        return true
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

    /**
     * Track request information.
     * @param rawTrackingInfo the raw tracking information.
     */
    void track(RawTrackingInfo rawTrackingInfo) {
        if (!rawTrackingInfo) return

        def converter = trackingInfoConverterFactory.createConverter(rawTrackingInfo)
        if (converter) {
            def trackingInfo = converter.convert(rawTrackingInfo)
            trackingInfo.save(flush: true)
        }
    }
}
