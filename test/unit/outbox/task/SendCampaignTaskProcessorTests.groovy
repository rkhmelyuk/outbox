package outbox.task

import grails.test.GrailsUnitTestCase
import outbox.campaign.Campaign
import outbox.campaign.CampaignService
import outbox.campaign.CampaignState
import outbox.mail.EmailService
import outbox.mail.EmailUtil
import outbox.subscriber.Subscriber
import outbox.template.Template

/**
 * @author Ruslan Khmelyuk
 */
class SendCampaignTaskProcessorTests extends GrailsUnitTestCase {

    SendCampaignTaskProcessor processor

    @Override protected void setUp() {
        super.setUp();

        processor = new SendCampaignTaskProcessor()
    }

    void testProcess_NoCampaign() {
        def campaignServiceControl = mockFor(CampaignService)
        campaignServiceControl.demand.getCampaign { id, lazy ->
            assertEquals 11, id
            assertFalse lazy
            return null
        }
        processor.campaignService = campaignServiceControl.createMock()

        Task task = new Task(params: [campaignId: 11])

        processor.process task

        campaignServiceControl.verify()
    }

    void testProcess() {
        def campaignServiceControl = mockFor(CampaignService, true)
        def saveCall = 0
        campaignServiceControl.demand.getCampaignSubscribers { campaign ->
            assertEquals 11, campaign.id
            new Subscriber(id: 1, email: 'test@mailsight.com')
        }
        campaignServiceControl.demand.getCampaign { id, lazy ->
            assertEquals 11, id
            new Campaign(id: id, template: new Template(templateBody: 'body'))
        }
        campaignServiceControl.demand.getCampaign { id ->
            assertEquals 11, id
            new Campaign(id: id, template: new Template(templateBody: 'body'))
        }
        campaignServiceControl.demand.saveCampaign(1..2) { campaign ->
            if (!saveCall) {
                assertEquals CampaignState.Sending, campaign.state
            }
            else {
                assertEquals CampaignState.InProgress, campaign.state
            }
            saveCall++
            return true
        }
        processor.campaignService = campaignServiceControl.createMock()

        def emailServiceControl = mockFor(EmailService, true)
        emailServiceControl.demand.sendEmails { emails ->
            assertNotNull emails
            assertEquals 1, emails.size()
            def email = emails.first()
            assertEquals EmailUtil.emailAddress(null, 'test@mailsight.com'), email.to
            assertEquals 'body', email.body
        }
        processor.emailService = emailServiceControl.createMock()

        Task task = new Task(params: [campaignId: 11])

        Campaign.class.metaClass.static.withTransaction = { closure -> closure()}

        processor.process task

        campaignServiceControl.verify()
        emailServiceControl.verify()
    }

    void testProcess_NotSave() {
        def campaignServiceControl = mockFor(CampaignService, true)
        campaignServiceControl.demand.getCampaign { id, lazy ->
            assertEquals 11, id
            new Campaign(id: id, template: new Template(templateBody: 'body'))
        }
        campaignServiceControl.demand.saveCampaign(1..2) { campaign ->

            assertEquals CampaignState.Sending, campaign.state
            return false
        }
        processor.campaignService = campaignServiceControl.createMock()


        Task task = new Task(params: [campaignId: 11])
        Campaign.class.metaClass.static.withTransaction = { closure -> closure()}

        processor.process task

        campaignServiceControl.verify()
    }

}