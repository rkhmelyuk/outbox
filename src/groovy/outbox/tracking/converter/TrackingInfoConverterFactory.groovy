package outbox.tracking.converter

import outbox.tracking.RawTrackingInfo

/**
 * The tracking info converters factory implementation.
 *
 * @author Ruslan Khmelyuk
 */
public class TrackingInfoConverterFactory {

    def campaignTrackingInfoConverter

    TrackingInfoConverter createConverter(RawTrackingInfo rawTrackingInfo) {
        campaignTrackingInfoConverter
    }
}
