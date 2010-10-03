package outbox.tracking.converter

import outbox.tracking.RawTrackingInfo
import outbox.tracking.TrackingInfo

/**
 * The interface for tracking info converter.
 * Use for converting raw tracking information to the tracking info that could be stored.
 *
 * @author Ruslan Khmelyuk
 */
interface TrackingInfoConverter {

    String LOCALES_SEPARATOR = ','
    String OPTIONS_SEPARATOR = ';'

    TrackingInfo convert(RawTrackingInfo rawTrackingInfo)
}
