package outbox.campaign

import grails.test.GrailsUnitTestCase
import outbox.subscriber.Subscriber

/**
 * @author Ruslan Khmelyuk
 */
class CampaignMessageTests extends GrailsUnitTestCase {

    void testFields() {
        def date = new Date()
        def message = new CampaignMessage()
        message.id = '0123456789abcdef'
        message.campaign = new Campaign(id: 1)
        message.subscriber = new Subscriber(id: 'abcdef0123456789')
        message.email = 'test@mailsight.com'
        message.sentDate = date

        assertEquals '0123456789abcdef', message.id
        assertEquals 1, message.campaign.id
        assertEquals 'abcdef0123456789', message.subscriber.id
        assertEquals 'test@mailsight.com', message.email
        assertEquals date, message.sentDate
    }
}
