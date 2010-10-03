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
    Date sendDate

    static mapping = {
        table 'CampaignMessage'
        id column: 'CampaignMessageId', generator: 'assigned'
        columns {
            campaign column: 'CampaignId'
            subscriber column: 'SubscriberId'
            email column: 'SubscriberEmail'
            sendDate column: 'SendDate'
        }
        version false
    }

    static constraints = {
        id maxSize: 40
        campaign nullable: false
        subscriber nullable: false
        email nullable: false, blank: false
        sendDate nullable: false
    }

    def beforeInsert() {
        generateId()
    }

    /**
     * Generated id for this object.
     * Before generating id next fields must be set: email, campaign, subscriber and send date.
     *
     * @return the generated id.
     */
    void generateId() {
        String string = email + '-' + campaign?.id + '-' +
                subscriber?.id + '-' + sendDate?.format('yyyyMMddHHmmssSSSZ')
        id = SHA1Codec.encode(string.bytes)
    }
}
