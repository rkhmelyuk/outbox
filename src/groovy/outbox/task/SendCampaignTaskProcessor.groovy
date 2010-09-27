package outbox.task

import org.apache.log4j.Logger
import outbox.campaign.Campaign
import outbox.campaign.CampaignService
import outbox.campaign.CampaignState
import outbox.mail.Email
import outbox.mail.EmailService
import outbox.mail.EmailUtil
import outbox.subscriber.Subscriber
import outbox.template.Template

/**
 * Responsible for sending campaign
 *
 * TODO - @SupportsTask(name = 'SendCampaign', version = 1)
 * 
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class SendCampaignTaskProcessor implements TaskProcessor {

    static final Logger log = Logger.getLogger(SendCampaignTaskProcessor.class)

    EmailService emailService
    CampaignService campaignService

    void process(Task task) {
        def campaignId = task?.params?.campaignId
        def campaign = campaignService.getCampaign(campaignId, false)

        if (campaign == null)  {
            return
        }

        // try to switch to Sending state and if switched than continue
        if (startSending(campaign)) {
            def template = campaign.template
            def subscribers = campaignService.getCampaignSubscribers(campaign)
            def emails = subscribers.collect { subscriber -> buildEmail(campaign, subscriber, template) }

            // send emails
            emailService.sendEmails emails

            // switch campaign to the In Progress state
            startInProgress(campaign)
        }
        else {
            log.error "Error to change campaign $campaign state to Sending"
        }
    }

    boolean startSending(Campaign campaign) {
        campaign.state = CampaignState.Sending
        saveCampaign(campaign)
    }

    boolean startInProgress(Campaign campaign) {
        campaign = campaignService.getCampaign(campaign.id)
        campaign.state = CampaignState.InProgress
        saveCampaign(campaign)
    }

    def saveCampaign(Campaign campaign) {
        def result = false
        Campaign.withTransaction {
            result = campaignService.saveCampaign(campaign)
        }
        return result
    }

    Email buildEmail(Campaign campaign, Subscriber subscriber, Template template) {
        def email = new Email()

        email.to = EmailUtil.emailAddress(subscriber.fullName, subscriber.email)
        email.subject = campaign.subject
        email.body = template.templateBody
                .replace('{{firstName}}', subscriber.firstName ?: '')
                .replace('{{lastName}}', subscriber.lastName ?: '')
                .replace('{{name}}', subscriber.fullName ?: '')
        email.contentType = 'text/html'

        return email
    }

}
