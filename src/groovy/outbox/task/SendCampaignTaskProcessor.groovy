package outbox.task

import org.apache.log4j.Logger
import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.campaign.CampaignService
import outbox.campaign.CampaignState
import outbox.mail.Email
import outbox.mail.EmailService
import outbox.mail.EmailUtil
import outbox.subscriber.Subscriber
import outbox.template.Template
import outbox.template.builder.TemplateFilter
import outbox.template.builder.TemplateFilterContext
import outbox.tracking.TrackingService

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
    TrackingService trackingService
    TemplateFilter templateFilterChain

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
            def emails = []
            def messages = []
            def date = new Date()
            subscribers.each { subscriber ->
                def message  = new CampaignMessage(campaign: campaign, subscriber: subscriber,
                        email: subscriber.email, sendDate: date)
                message.generateId()
                messages << message

                emails << buildEmail(campaign, subscriber, template, message)
            }

            // send emails
            emailService.sendEmails emails

            // save messages information
            campaignService.addCampaignMessages messages

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

    Email buildEmail(Campaign campaign, Subscriber subscriber, Template template, CampaignMessage message) {
        def email = new Email()

        email.to = EmailUtil.emailAddress(subscriber.fullName, subscriber.email)
        email.subject = campaign.subject
        email.body = buildTemplate(campaign, template, subscriber, message)
        email.contentType = 'text/html'
        email.charset = 'utf-8'

        return email
    }

    String buildTemplate(Campaign campaign, Template template, Subscriber subscriber, CampaignMessage message) {
        def context = new TemplateFilterContext(
                campaign: campaign,
                subscriber: subscriber,
                message: message,
                template: template.templateBody)

        context.model.firstName = subscriber.firstName
        context.model.lastName = subscriber.lastName
        context.model.name = subscriber.fullName
        context.model.email = subscriber.email
        context.model.gender = subscriber.gender?.name
        context.model.namePrefix = subscriber.namePrefix?.name
        context.model.subscriberType = subscriber.subscriberType?.name
        context.model.campaignName = campaign.name
        
        templateFilterChain.filter context

        return context.template
    }

}
