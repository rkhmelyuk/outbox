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
        id column: 'TrackingInfoId'
        columns {
            campaignId column: 'CampaignId'
            subscriberId column: 'SubscriberId'
            trackingReferenceId column: 'TrackingReferenceId'
            click column: 'Click'
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

        locale maxSize: 50
        datetime nullable: false
        browserName maxSize: 100
        browserVersion maxSize: 100
        operatingSystem maxSize: 100
        ipAddress maxSize: 40

        countryCode maxSize: 2
        countryName maxSize: 100
        city maxSize: 500
        region maxSize: 500
        postalCode maxSize: 20
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
