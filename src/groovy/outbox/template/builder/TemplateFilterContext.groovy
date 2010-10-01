package outbox.template.builder

import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.subscriber.Subscriber
import outbox.tracking.TrackingReference

/**
 * @author Ruslan Khmelyuk
 * @since  2010-10-01
 */
class TemplateFilterContext {

    Campaign campaign
    Subscriber subscriber
    CampaignMessage message

    String template
    Map<String, Object> model = [:]

    List<TrackingReference> trackingReferences = []
}
