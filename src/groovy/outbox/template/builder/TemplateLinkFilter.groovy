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
        while (matcher.find()) {
            def resource = matcher.group(2)
            println resource
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
            }
        }

        trackingReferences.each { String key, TrackingReference value -> 
            body = body.replace(key, trackingLinkBuilder.trackingLink(value))
        }

        context.template = body
        context.trackingReferences += trackingReferences.values()
    }

}
