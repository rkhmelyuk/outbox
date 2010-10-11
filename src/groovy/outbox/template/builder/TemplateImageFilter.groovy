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
        def positions = [:]
        while (matcher.find()) {
            def resource = matcher.group(2)
            def position = matcher.start(2)
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
                def positionList = positions[resource]
                if (!positionList) {
                    positionList = []
                    positions[resource] = positionList
                }
                positionList << position
            }
        }

        def builder = new StringBuilder(body)
        int delta = 0
        trackingReferences.each { String resource, TrackingReference value ->
            def resourcePositions = positions[resource]
            def resourceLength = resource.length()
            resourcePositions?.each { start ->
                def trackingLink = trackingLinkBuilder.trackingLink(value)
                start += delta
                builder.replace(start, start + resourceLength, trackingLink)
                delta += trackingLink.length() - resourceLength
            }
        }

        context.template = builder.toString()
        context.trackingReferences += trackingReferences.values()
    }

}
