package outbox.campaign

import grails.test.GrailsUnitTestCase
import outbox.subscription.SubscriptionList

/**
 * @author Ruslan Khmelyuk
 */
class CampaignSubscriptionTests extends GrailsUnitTestCase {

    void testFields() {
        CampaignSubscription subscription = new CampaignSubscription()
        subscription.id = 1
        subscription.campaign = new Campaign(id: 2)
        subscription.subscriptionList = new SubscriptionList(id: 3)

        assertEquals 1, subscription.id
        assertEquals 2, subscription.campaign.id
        assertEquals 3, subscription.subscriptionList.id
    }

    void testSorting() {
        CampaignSubscription subscription1 = new CampaignSubscription()
        CampaignSubscription subscription2 = new CampaignSubscription()

        assertEquals(0, subscription1.compareTo(subscription2))
        assertEquals(0, subscription2.compareTo(subscription1))

        subscription1.id = 10
        assertEquals(+1, subscription1.compareTo(subscription2))
        assertEquals(-1, subscription2.compareTo(subscription1))

        subscription1.id = 10
        subscription2.id = 20
        assertEquals(+1, subscription1.compareTo(subscription2))
        assertEquals(-1, subscription2.compareTo(subscription1))

        subscription1.id = null
        subscription2.id = 20
        assertEquals(-1, subscription1.compareTo(subscription2))
        assertEquals(+1, subscription2.compareTo(subscription1))
    }

    void testNonDuplicate() {
        Campaign campaign = new Campaign(id: 1)
        CampaignSubscription.class.metaClass.static.findAllByCampaignAndSubscriptionList = { c, l -> return [] }

        SubscriptionList list2 = new SubscriptionList(id: 2)
        CampaignSubscription subscription2 = new CampaignSubscription(id: 2, campaign: campaign, subscriptionList: list2)
        assertFalse CampaignSubscription.duplicateSubscription(subscription2)
    }

    void testDuplicateEmail() {
        Campaign campaign = new Campaign(id: 1)
        SubscriptionList list1 = new SubscriptionList(id: 1)
        CampaignSubscription subscription1 = new CampaignSubscription(id: 1, campaign: campaign, subscriptionList: list1)
        mockDomain(CampaignSubscription, [subscription1])

        CampaignSubscription.class.metaClass.static.findAllByCampaignAndSubscriptionList = { c, l -> return [subscription1] }

        SubscriptionList list2 = new SubscriptionList(id: 1)
        CampaignSubscription subscription2 = new CampaignSubscription(id: 2, campaign: campaign, subscriptionList: list2)
        assertTrue CampaignSubscription.duplicateSubscription(subscription2)
    }
}
