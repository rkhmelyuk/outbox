package outbox.tracking

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class TrackingInfoTests extends GrailsUnitTestCase {

    void testFields() {
        def trackingInfo = new TrackingInfo()
        trackingInfo.id = 'abcdef000'
        trackingInfo.campaignId = 123
        trackingInfo.subscriberId = '123456'
        trackingInfo.trackingReferenceId = '345678'
        trackingInfo.city = 'New York'
        trackingInfo.region = 'Mann'
        trackingInfo.countryCode = 'US'
        trackingInfo.countryName = 'USA'
        trackingInfo.postalCode = '9876'
        trackingInfo.ipAddress = '123.83.23.11'
        def date = new Date()
        trackingInfo.datetime = date
        trackingInfo.browserName = 'Mozilla'
        trackingInfo.browserVersion = '3.5'
        trackingInfo.click = true
        trackingInfo.latitude = 23.2333
        trackingInfo.longitude = 45.5545
        trackingInfo.operatingSystem = 'Linux'

        assertEquals 'abcdef000', trackingInfo.id
        assertEquals 123, trackingInfo.campaignId
        assertEquals '123456', trackingInfo.subscriberId
        assertEquals '345678', trackingInfo.trackingReferenceId
        assertEquals date, trackingInfo.datetime
        assertEquals true, trackingInfo.click
        assertEquals 23.2333, trackingInfo.latitude
        assertEquals 45.5545, trackingInfo.longitude
        assertEquals 'Linux', trackingInfo.operatingSystem
        assertEquals 'Mozilla', trackingInfo.browserName
        assertEquals '3.5', trackingInfo.browserVersion
        assertEquals '123.83.23.11', trackingInfo.ipAddress
        assertEquals 'New York', trackingInfo.city
        assertEquals 'Mann', trackingInfo.region
        assertEquals '9876', trackingInfo.postalCode
        assertEquals 'US', trackingInfo.countryCode
        assertEquals 'USA', trackingInfo.countryName
    }

    void testGenerateId() {
        def trackingInfo1 = new TrackingInfo()
        def trackingInfo2 = new TrackingInfo()

        trackingInfo1.campaignId = 123
        trackingInfo1.subscriberId = '123456'
        trackingInfo1.trackingReferenceId = '345678'
        trackingInfo1.datetime = new Date()
        trackingInfo1.ipAddress = '123.83.23.11'

        trackingInfo2.campaignId = 123
        trackingInfo2.subscriberId = '123456'
        trackingInfo2.trackingReferenceId = '345678'
        trackingInfo2.datetime = new Date()
        trackingInfo2.ipAddress = '123.83.23.11'

        trackingInfo1.generateId()
        trackingInfo2.generateId()

        assertFalse trackingInfo1.id.equals(trackingInfo2.id) 
    }
}
