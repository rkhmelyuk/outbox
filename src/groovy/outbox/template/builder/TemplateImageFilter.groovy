package outbox.template.builder

import outbox.tracking.TrackingReference
import outbox.tracking.TrackingReferenceType

/**
 * Used to replace image src with tracking reference.
 * 
 * @author Ruslan Khmelyuk
 * @since  2010-10-01
 */
class TemplateImageFilter implements TemplateFilter {

    TrackingLinkBuilder trackingLinkBuilder

    void filter(TemplateFilterContext context) {
        def body = context.template

        def reference = ~/<img[^>]*src=(["']([^"']*)["'])/
        def matcher = (body =~ reference)

        def trackingReferences = [:]
        while (matcher.find()) {
            def resource = matcher.group(2)
            if (resource) {
                def trackingRef = trackingReferences[resource]
                if (!trackingRef) {
                    trackingRef = new TrackingReference(
                            campaignId: context.campaign.id,
                            subscriberId: context.subscriber.id,
                            campaignMessageId: context.message.id,
                            reference: resource,
                            type: TrackingReferenceType.Resource)

                    trackingRef.generateId()
                    trackingReferences[resource] = trackingRef
                }
            }
        }

        trackingReferences.each { String key, TrackingReference value ->
            body = body.replace(key, trackingLinkBuilder.trackingLink(value))
        }

        context.template = body
        context.trackingReferences += trackingReferences.values()
    }

}
