package outbox.campaign

import org.codehaus.groovy.grails.plugins.codecs.SHA1Codec
import outbox.subscriber.Subscriber

/**
 * @author Ruslan Khmelyuk
 */
class CampaignMessage {

    String id
    Campaign campaign
    Subscriber subscriber
    String email
    Date sentDate

    static mapping = {
        table 'CampaignMessage'
        id column: 'CampaignMessageId', generator: 'assigned'
        columns {
            campaign column: 'CampaignId'
            subscriber column: 'SubscriberId'
            email column: 'SubscriberEmail'
            sentDate column: 'SentDate'
        }
        version false
    }

    static constraints = {
        id maxSize: 40
        campaign nullable: false
        subscriber nullable: false
        email nullable: false, blank: false
        sentDate nullable: false
    }

    def beforeInsert() {
        id = generateId()
    }

    /**
     * Generated id for this object.
     * Before generating id, both email and member must be set.
     *
     * @return the generated id.
     */
    String generateId() {
        String string = email + '-' + campaign?.id + '-' + subscriber?.id + '-' + sentDate.format('yyyyMMddHHmmss')
        SHA1Codec.encode(string.bytes)
    }
}
