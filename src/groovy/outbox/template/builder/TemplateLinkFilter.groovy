package outbox.template.builder

import outbox.tracking.TrackingReference
import outbox.tracking.TrackingReferenceType

/**
 * @author Ruslan Khmelyuk
 */
class TemplateLinkFilter implements TemplateFilter {

    TrackingLinkBuilder trackingLinkBuilder

    void filter(TemplateFilterContext context) {
        def body = context.template

        def reference = ~/<a[^>]*href=(["']([^"']*)["'])/
        def link = ~/(https?|ftp|ftps|sftp).*/
        def matcher = (body =~ reference)

        def trackingReferences = [:]
        def positions = [:]
        while (matcher.find()) {
            def resource = matcher.group(2)
            def position = matcher.start(2)
            if (resource && resource ==~ link) {
                def trackingRef = trackingReferences[resource]
                if (!trackingRef) {
                    trackingRef = new TrackingReference(
                            campaignId: context.campaign.id,
                            subscriberId: context.subscriber.id,
                            campaignMessageId: context.message.id,
                            reference: resource,
                            type: TrackingReferenceType.Link)

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
                delta += trackingLink.length() - resource.length()
            }
        }

        context.template = builder.toString()
        context.trackingReferences += trackingReferences.values()
    }

}
