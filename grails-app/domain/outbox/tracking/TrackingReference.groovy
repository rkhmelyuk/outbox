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
        id column: 'TrackingReferenceId', generator: 'assigned'
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
        generateId()
    }

    /**
     * Generated id for this object.
     * Before generating id next fields must be set: campaign, message, subscriber and reference and reference type.
     *
     * @return the generated id.
     */
    void generateId() {
        String string = AppConstant.TRACKING_REFERENCE_SALT + '-' +
                campaignId + '-' + subscriberId + '-' + 
                campaignMessageId + '-' + reference + '-' + type.name()
        
        id = SHA1Codec.encode(string.bytes)
    }
}
