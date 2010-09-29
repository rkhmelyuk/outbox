package outbox.tracking

import org.codehaus.groovy.grails.plugins.codecs.SHA1Codec
import outbox.AppConstant

/**
 * @author Ruslan Khmelyuk
 */
class TrackingReference {

    String id
    Long campaignId
    String subscriberId
    String campaignMessageId

    TrackingReferenceType type
    String reference

    static mapping = {
        table 'TrackingReference'
        id column: 'TrackingReferenceId'
        columns {
            campaignId column: 'CampaignId'
            subscriberId column: 'SubscriberId'
            campaignMessageId column: 'CampaignMessageId'
            type column: 'ReferenceType'
            reference column: 'Reference'
        }
        version false
        cache true
    }

    static constraints = {
        id maxSize: 40
        campaignId nullable: false
        subscriberId nullable: false
        campaignMessageId nullable: false
        type nullable: false
        reference nullable: false, maxSize: 4000
    }

    def beforeInsert() {
        id = generateId()
    }

    /**
     * Generated id for this object.
     * Before generating id next fields must be set: campaign, message, subscriber and reference and reference type.
     *
     * @return the generated id.
     */
    String generateId() {
        String string = campaignId + '-' + subscriberId + '-'
                + campaignMessageId + '-' + reference + '-'
                + type + '-' + AppConstant.TRACKING_REFERENCE_SALT
        
        SHA1Codec.encode(string.bytes)
    }
}
