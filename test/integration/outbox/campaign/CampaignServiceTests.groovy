package outbox.campaign

import grails.test.GrailsUnitTestCase
import outbox.member.Member
import outbox.subscriber.Subscriber
import outbox.subscriber.SubscriberService
import outbox.subscription.Subscription
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService
import outbox.subscription.SubscriptionStatus
import outbox.task.TaskService
import outbox.template.Template
import outbox.template.TemplateService

/**
 * @author Ruslan Khmelyuk
 */
class CampaignServiceTests extends GrailsUnitTestCase {

    CampaignService campaignService
    SubscriptionListService subscriptionListService
    SubscriberService subscriberService
    TemplateService templateService

    def member
    def template

    protected void setUp() {
        super.setUp();

        member = new Member(
                firstName: 'Test',
                lastName: 'Member',
                email: 'test+member@mailsight.com',
                username: 'username',
                password: 'password')

        member.save()

        template = new Template(
                name: 'Test Template',
                templateBody: 'Test Body',
                owner: member)

        template.save()
    }

    protected void tearDown() {
        template.delete()
        member.delete()

        super.tearDown();
    }

    void testAddCampaign() {
        def campaign = createTestCampaign()

        assertTrue campaignService.addCampaign(campaign)
        def found = campaignService.getCampaign(campaign.id)

        assertEquals 'Test Name', found.subject
        assertEquals campaign, found
    }

    void testSaveCampaign() {
        def campaign = createTestCampaign()

        assertTrue campaignService.addCampaign(campaign)
        def found = campaignService.getCampaign(campaign.id)

        found.name = 'Test Name 2'
        found.subject = 'Test Subject 2'
        found.description = 'Test Description 2'

        assertTrue campaignService.saveCampaign(found)

        def found2 = campaignService.getCampaign(campaign.id)

        assertEquals found, found2
    }

    void testGetMemberCampaigns() {
        def campaign1 = createTestCampaign()
        def campaign2 = createTestCampaign()
        def campaign3 = createTestCampaign()

        assertTrue campaignService.addCampaign(campaign1)
        assertTrue campaignService.addCampaign(campaign2)
        assertTrue campaignService.addCampaign(campaign3)
 
        def conditions = new CampaignConditionsBuilder().build { ownedBy member }
        def found = campaignService.searchCampaigns(conditions)

        assertNotNull found
        assertEquals 3, found.size()
        assertTrue found.contains(campaign1)
        assertTrue found.contains(campaign2)
        assertTrue found.contains(campaign3)
    }

    void testCampaignsSorting() {
        def campaign1 = createTestCampaign()
        def campaign2 = createTestCampaign()
        def campaign3 = createTestCampaign()

        campaign1.dateCreated = new Date() - 10
        campaign2.dateCreated = new Date()
        campaign3.dateCreated = new Date() - 5

        assertTrue campaignService.addCampaign(campaign1)
        assertTrue campaignService.addCampaign(campaign2)
        assertTrue campaignService.addCampaign(campaign3)

        def conditions = new CampaignConditionsBuilder().build { ownedBy member }
        def found = campaignService.searchCampaigns(conditions)

        assertEquals campaign2, found[0]
        assertEquals campaign3, found[1]
        assertEquals campaign1, found[2]
    }

    void testAddCampaignSubscription() {
        def campaign = createTestCampaign()
        def list = createTestSubscriptionList(1)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriptionListService.saveSubscriptionList(list)

        def campaignSubscription = new CampaignSubscription(campaign: campaign, subscriptionList: list)
        assertTrue campaignService.addCampaignSubscription(campaignSubscription)

        def found = campaignService.getCampaignSubscriptions(campaign)
        assertEquals 1, found.size()
        assertTrue found.contains(campaignSubscription)
    }

    void testAddCampaignSubscription_ForStartedCampaign() {
        def campaign = createTestCampaign()
        def list = createTestSubscriptionList(1)

        campaign.state = CampaignState.InProgress

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriptionListService.saveSubscriptionList(list)

        def campaignSubscription = new CampaignSubscription(campaign: campaign, subscriptionList: list)
        assertFalse campaignService.addCampaignSubscription(campaignSubscription)
    }

    void testGetCampaignSubscription() {
        def campaign = createTestCampaign()
        def list = createTestSubscriptionList(1)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriptionListService.saveSubscriptionList(list)

        def campaignSubscription = new CampaignSubscription(campaign: campaign, subscriptionList: list)
        assertTrue campaignService.addCampaignSubscription(campaignSubscription)

        def found = campaignService.getCampaignSubscription(campaignSubscription.id)
        assertNotNull found
        assertEquals campaignSubscription.campaign.id, found.campaign.id
        assertEquals campaignSubscription.subscriptionList.id, found.subscriptionList.id
    }

    void testGetSubscriptions() {
        def campaign1 = createTestCampaign()
        def campaign2 = createTestCampaign()
        def list = createTestSubscriptionList(1)

        assertTrue campaignService.addCampaign(campaign1)
        assertTrue campaignService.addCampaign(campaign2)
        assertTrue subscriptionListService.saveSubscriptionList(list)

        def campaignSubscription1 = new CampaignSubscription(campaign: campaign1, subscriptionList: list)
        def campaignSubscription2 = new CampaignSubscription(campaign: campaign2, subscriptionList: list)

        assertTrue campaignService.addCampaignSubscription(campaignSubscription1)
        assertTrue campaignService.addCampaignSubscription(campaignSubscription2)

        def found = campaignService.getSubscriptions(list)
        assertEquals 2, found.size()
        assertTrue found.contains(campaignSubscription1)
        assertTrue found.contains(campaignSubscription2)
    }

    void testDeleteCampaignSubscription() {
        def campaign = createTestCampaign()
        def list = createTestSubscriptionList(1)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriptionListService.saveSubscriptionList(list)

        def campaignSubscription = new CampaignSubscription(campaign: campaign, subscriptionList: list)

        assertTrue campaignService.addCampaignSubscription(campaignSubscription)
        assertEquals 1, campaignService.getCampaignSubscriptions(campaign).size()
        assertTrue campaignService.deleteCampaignSubscription(campaignSubscription)
        assertEquals 0, campaignService.getCampaignSubscriptions(campaign).size()
    }

    void testDeleteCampaignSubscription_ForStartedCampaign() {
        def campaign = createTestCampaign()
        def list = createTestSubscriptionList(1)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriptionListService.saveSubscriptionList(list)
        def campaignSubscription = new CampaignSubscription(campaign: campaign, subscriptionList: list)

        assertTrue campaignService.addCampaignSubscription(campaignSubscription)

        campaign.state = CampaignState.InProgress
        assertTrue campaignService.saveCampaign(campaign, false)

        assertEquals 1, campaignService.getCampaignSubscriptions(campaign).size()
        assertFalse campaignService.deleteCampaignSubscription(campaignSubscription)
        assertEquals 1, campaignService.getCampaignSubscriptions(campaign).size()
    }

    void testTotalSubscribersNumber() {
        def campaign = createTestCampaign()
        def list1 = createTestSubscriptionList(1)
        def list2 = createTestSubscriptionList(2)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriptionListService.saveSubscriptionList(list1)
        assertTrue subscriptionListService.saveSubscriptionList(list2)

        def campaignSubscription1 = new CampaignSubscription(campaign: campaign, subscriptionList: list1)
        def campaignSubscription2 = new CampaignSubscription(campaign: campaign, subscriptionList: list2)

        assertTrue campaignService.addCampaignSubscription(campaignSubscription1)
        assertTrue campaignService.addCampaignSubscription(campaignSubscription2)

        def subscribed = SubscriptionStatus.findById(1)
        if (!subscribed) {
            subscribed = new SubscriptionStatus(id: 1, name: 'subscribed').save()
        }
        def unsubscribed = SubscriptionStatus.findById(2)
        if (!unsubscribed) {
            unsubscribed = new SubscriptionStatus(id: 2, name: 'unsubscribed').save()
        }

        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)
        def subscriber3 = createTestSubscriber(3)
        def subscriber4 = createTestSubscriber(4)
        def subscriber5 = createTestSubscriber(5)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)
        assertTrue subscriberService.saveSubscriber(subscriber3)
        assertTrue subscriberService.saveSubscriber(subscriber4)
        assertTrue subscriberService.saveSubscriber(subscriber5)

        def subscription1 = new Subscription(subscriber: subscriber1, subscriptionList: list1, status: subscribed)
        def subscription2 = new Subscription(subscriber: subscriber1, subscriptionList: list2, status: subscribed)
        def subscription3 = new Subscription(subscriber: subscriber2, subscriptionList: list1, status: unsubscribed)
        def subscription4 = new Subscription(subscriber: subscriber3, subscriptionList: list1, status: subscribed)
        def subscription5 = new Subscription(subscriber: subscriber3, subscriptionList: list2, status: subscribed)
        def subscription6 = new Subscription(subscriber: subscriber4, subscriptionList: list1, status: subscribed)
        def subscription7 = new Subscription(subscriber: subscriber4, subscriptionList: list2, status: subscribed)
        def subscription8 = new Subscription(subscriber: subscriber5, subscriptionList: list1, status: unsubscribed)
        def subscription9 = new Subscription(subscriber: subscriber5, subscriptionList: list2, status: subscribed)

        assertTrue subscriptionListService.addSubscription(subscription1)
        assertTrue subscriptionListService.addSubscription(subscription2)
        assertTrue subscriptionListService.addSubscription(subscription3)
        assertTrue subscriptionListService.addSubscription(subscription4)
        assertTrue subscriptionListService.addSubscription(subscription5)
        assertTrue subscriptionListService.addSubscription(subscription6)
        assertTrue subscriptionListService.addSubscription(subscription7)
        assertTrue subscriptionListService.addSubscription(subscription8)
        assertTrue subscriptionListService.addSubscription(subscription9)

        assertEquals 4, campaignService.getTotalSubscribersNumber(campaign)
    }

    void testGetProposedSubscriptionLists() {
        def campaign = createTestCampaign()
        def list1 = createTestSubscriptionList(1)
        def list2 = createTestSubscriptionList(2)
        def list3 = createTestSubscriptionList(3)
        def list4 = createTestSubscriptionList(4)

        list3.archived = true
        list4.archived = false

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriptionListService.saveSubscriptionList(list1)
        assertTrue subscriptionListService.saveSubscriptionList(list2)
        assertTrue subscriptionListService.saveSubscriptionList(list3)
        assertTrue subscriptionListService.saveSubscriptionList(list4)

        def campaignSubscription1 = new CampaignSubscription(campaign: campaign, subscriptionList: list1)
        def campaignSubscription2 = new CampaignSubscription(campaign: campaign, subscriptionList: list2)

        assertTrue campaignService.addCampaignSubscription(campaignSubscription1)
        assertTrue campaignService.addCampaignSubscription(campaignSubscription2)

        def proposed = campaignService.getProposedSubscriptionLists(campaign)

        assertNotNull proposed
        assertEquals 1, proposed.size()
        assertEquals list4, proposed[0]
    }

    void testGetProposedTemplates() {
        def campaign = createTestCampaign()
        def template1 = createTestTemplate(1)
        def template2 = createTestTemplate(2)
        def template3 = createTestTemplate(3)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue templateService.addTemplate(template1)
        assertTrue templateService.addTemplate(template2)
        assertTrue templateService.addTemplate(template3)

        def proposed = campaignService.getProposedTemplates(campaign)

        assertNotNull proposed
        assertEquals 3, proposed.size()
        assertTrue proposed.contains(template1)
        assertTrue proposed.contains(template2)
        assertTrue proposed.contains(template3)
    }

    void testDeleteCampaign() {
        def campaign = createTestCampaign()

        assertTrue campaignService.addCampaign(campaign)
        try {
            assertTrue campaignService.deleteCampaign(campaign)
            assertNull campaignService.getCampaign(campaign.id)
        }
        catch (Exception e) {
            fail 'Error to delete campaign'
        }
    }

    void testDeleteUsedCampaign() {
        def campaign = createTestCampaign()
        def list = createTestSubscriptionList(1)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriptionListService.saveSubscriptionList(list)
        assertTrue campaignService.addCampaignSubscription(
                new CampaignSubscription(campaign: campaign, subscriptionList: list))

        try {
            assertTrue campaignService.deleteCampaign(campaign)
            assertNull campaignService.getCampaign(campaign.id)
        }
        catch (Exception e) {
            fail 'Error to delete campaign'
        }
    }

    void testDeleteCampaign_ForStartedCampaign() {
        def campaign = createTestCampaign()

        campaign.state = CampaignState.InProgress

        try {
            assertTrue campaignService.addCampaign(campaign)
            assertFalse campaignService.deleteCampaign(campaign)
            assertNotNull campaignService.getCampaign(campaign.id)
        }
        catch (Exception e) {
            fail 'Error to delete campaign'
        }
    }

    void testSend() {
        final def taskService = campaignService.taskService

        def date = new Date()
        try {
            def campaign = createTestCampaign()
            campaign.state = CampaignState.Ready

            def taskServiceControl = mockFor(TaskService)
            taskServiceControl.demand.enqueueTask { true }
            campaignService.taskService = taskServiceControl.createMock()
            
            assertTrue campaignService.addCampaign(campaign)
            assertTrue campaignService.sendCampaign(campaign)

            def found = campaignService.getCampaign(campaign.id)
            assertNotNull found
            assertEquals CampaignState.Queued, found.state
            assertNotNull found.startDate
            assertFalse found.startDate.before(date)
            assertFalse new Date().before(found.startDate)

            taskServiceControl.verify()
        }
        finally {
            campaignService.taskService = taskService
        }
    }

    void testAddMessage() {
        def campaign = createTestCampaign()
        def subscriber = createTestSubscriber(1)

        assertTrue campaignService.addCampaign(campaign)
        assertTrue subscriberService.saveSubscriber(subscriber)

        def message = new CampaignMessage(campaign: campaign, subscriber: subscriber,
                sendDate: new Date(), email: 'test@mailsight.com')

        campaignService.addCampaignMessages([message])

        def found = CampaignMessage.findById(message.id)

        assertEquals message.campaign.id, found.campaign.id
        assertEquals message.subscriber.id, found.subscriber.id
        assertEquals message.sendDate, found.sendDate
        assertEquals message.email, found.email
    }

    void assertEquals(Campaign campaign, Campaign found) {
        assertNotNull found.dateCreated
        assertEquals CampaignState.New, found.state
        assertEquals campaign.subject, found.subject
        assertEquals campaign.name, found.name
        assertEquals campaign.description, found.description
        assertEquals campaign.owner.id, found.owner.id
        assertEquals campaign.template.id, found.template.id
    }

    Campaign createTestCampaign() {
        Campaign campaign = new Campaign()
        campaign.name = 'Test Name'
        campaign.description = 'Test Description'
        campaign.owner = member
        campaign.template = template
        return campaign
    }

    SubscriptionList createTestSubscriptionList(id) {
        SubscriptionList subscriptionList = new SubscriptionList()
        subscriptionList.name = 'Subscribers list' + id
        subscriptionList.description = 'Subscribers list description'
        subscriptionList.owner = member
        subscriptionList.subscribersNumber = 100
        return subscriptionList
    }

    Subscriber createTestSubscriber(id) {
        Subscriber subscriber = new Subscriber()
        subscriber.firstName = 'John'
        subscriber.lastName = 'Doe'
        subscriber.email = 'john.doe' + id + '@nowhere.com'
        subscriber.gender = null
        subscriber.timezone = null
        subscriber.language = null
        subscriber.namePrefix = null
        subscriber.enabled = true
        subscriber.member = member
        return subscriber
    }

    Template createTestTemplate(id) {
        Template template = new Template()
        template.name = 'Test Name' + id
        template.description = 'Test Description'
        template.templateBody = 'Test Template Body'
        template.owner = member
        return template
    }
}
