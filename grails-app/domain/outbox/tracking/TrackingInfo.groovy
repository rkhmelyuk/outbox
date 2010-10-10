package outbox.tracking

import org.codehaus.groovy.grails.plugins.codecs.SHA1Codec
import outbox.AppConstant

/**
 * @author Ruslan Khmelyuk
 */
class TrackingInfo {

    String id
    Long campaignId
    String subscriberId
    String trackingReferenceId
    boolean click
    boolean open

    Date datetime
    String browserName
    String browserVersion
    String operatingSystem
    String ipAddress
    String locale

    String countryCode
    String countryName
    String city
    String region
    String postalCode
    BigDecimal latitude
    BigDecimal longitude

    static mapping = {
        table 'TrackingInfo'
        id column: 'TrackingInfoId', generator: 'assigned'
        columns {
            campaignId column: 'CampaignId'
            subscriberId column: 'SubscriberId'
            trackingReferenceId column: 'TrackingReferenceId'
            click column: 'Click'
            open column: 'Open'
            datetime column: 'Date'
            locale column: 'Locale'
            browserName column: 'BrowserName'
            browserVersion column: 'BrowserVersion'
            operatingSystem column: 'OperatingSystem'
            ipAddress column: 'IPAddress'
            countryCode column: 'LocationCountryCode'
            countryName column: 'LocationCountryName'
            city column: 'LocationCity'
            region column: 'LocationRegion'
            postalCode column: 'LocationPostCode'
            latitude column: 'LocationLatitude'
            longitude column: 'LocationLongitude'
        }
        version false
        cache false
    }

    static constraints = {
        id maxSize: 40
        campaignId nullable: false
        subscriberId nullable: false, maxSize: 40
        trackingReferenceId nullable: false, maxSize: 40
        datetime nullable: false
        
        locale nullable: true, maxSize: 50
        browserName nullable: true, maxSize: 100
        browserVersion nullable: true, maxSize: 100
        operatingSystem nullable: true, maxSize: 100
        ipAddress nullable: true, maxSize: 40

        countryCode nullable: true, maxSize: 2
        countryName nullable: true, maxSize: 100
        city nullable: true, maxSize: 500
        region nullable: true, maxSize: 500
        postalCode nullable: true, maxSize: 20
        latitude nullable: true
        longitude nullable: true
    }

    def beforeInsert() {
        generateId()
    }

    /**
     * Generated id for this object.
     *
     * @return the generated id.
     */
    void generateId() {
        String string = AppConstant.TRACKING_INFO_SALT + '-' + trackingReferenceId +
                '-' + datetime?.format('yyyyMMddHHmmssSSSZ') + '-' + ipAddress

        id = SHA1Codec.encode(string.bytes)
    }

}
