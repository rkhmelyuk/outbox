package outbox.campaign

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.member.Member
import outbox.report.ReportUtil
import outbox.report.ReportsHolder
import outbox.subscription.SubscriptionListService
import outbox.template.TemplateService

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class CampaignController {

    def defaultAction = ''
    def allowedMethods = [add: 'POST', update: 'POST',
            addSubscriptionList: 'POST',
            removeSubscriptionList: 'POST']

    static def SHOW_HANDLERS = ['reports', 'details', 'subscribers', 'template']

    CampaignService campaignService
    SubscriptionListService subscriptionListService
    SpringSecurityService springSecurityService
    TemplateService templateService
    ReportsHolder reportsHolder

    def index = { 
        def conditions = new CampaignConditionsBuilder().build {
            ownedBy Member.load(springSecurityService.principal.id)
            order 'dateCreated', 'desc'
            max 10
        }
        def campaigns = campaignService.searchCampaigns(conditions)

        [campaigns: campaigns]
    }

    def create = {
        [campaign: new Campaign()]
    }

    def add = {
        def campaign = new Campaign()
        campaign.name = params.name
        campaign.description = params.description
        campaign.owner = Member.load(springSecurityService.principal.id)

        def model = [:]
        if (campaignService.addCampaign(campaign)) {
            model.success = true
            model.redirectTo = g.createLink(controller: 'campaign', action: 'show', id: campaign.id)
        }
        else {
            model.error = true
            MessageUtil.addErrors(request, model, campaign.errors);
        }

        render model as JSON
    }

    def update = {
        def model = [:]
        def campaign = campaignService.getCampaign(params.long('id'))
        if (campaign && campaign.ownedBy(springSecurityService.principal.id)) {
            campaign.name = params.name
            campaign.description = params.description
            campaign.endDate = params.endDate
            if (campaign.notStarted) {
                campaign.subject = params.subject
            }

            if (campaignService.saveCampaign(campaign)) {
                model.success = true
                model.name = campaign.name
            }
            else {
                model.error = true
                MessageUtil.addErrors(request, model, campaign.errors);
            }
        }
        else {
            model.error = true
        }

        render model as JSON
    }

    def delete = {
        def campaign = campaignService.getCampaign(params.long('id'))
        if (campaign && campaign.ownedBy(springSecurityService.principal.id)) {
            if (campaignService.deleteCampaign(campaign)) {
                redirect controller: 'campaign'
                return
            }
        }

        if (campaign == null) {
            flash.message = 'campaign.not.found'
            redirect controller: 'campaign'
            return
        }

        flash.message = 'campaign.remove.failed'
        redirect controller: 'campaign', action: 'show', id: campaign?.id
    }

    def send = {
        def model = [:]
        def campaign = campaignService.getCampaign(params.long('id'))
        if (campaign && campaign.ownedBy(springSecurityService.principal.id)) {
            if (campaignService.sendCampaign(campaign)) {
                model.success = true

                def data = handle(params.page, campaign)
                model.notifications = g.render(template: 'campaignNotifications', model: data)
                model.actions = g.render(template: 'campaignActions', model: data)
                model.stateName = message(code: campaign.state.messageCode)
            }
        }
        else {
            model.error = true
        }

        render model as JSON
    }

    def show = {
        def campaign = campaignService.getCampaign(params.long('id'))
        if (!campaign) {
            response.sendError 404
            return
        }
        if (!campaign.ownedBy(springSecurityService.principal.id)) {
            response.sendError 403
            return
        }

        def page = fetchPage(campaign)
        def model = handle(page, campaign)

        return model
    }

    def handle(String page, Campaign campaign) {
        def result
        switch (page) {
            case 'details':
                result = handleDetails(campaign)
                break
            case 'reports':
                result = handleReports(campaign)
                break
            case 'subscribers':
                result = handleSubscribers(campaign)
                break
            case 'template':
                result = handleTemplate(campaign)
                break
        }

        result = (result != null ? result : [:])

        def needTemplate = false
        def needSubscribers = false

        if (campaign.notStarted) {
            // Don't check whether we need template or subscribers, as it's started already. Too late..

            needTemplate = (campaign.template == null)
            def totalSubscribers = result.totalSubscribers
            if (totalSubscribers == null) {
                totalSubscribers = campaignService.getTotalSubscribersNumber(campaign)
            }

            needSubscribers = !totalSubscribers
        }

        result << [
                page: page,
                campaign: campaign,
                needTemplate: needTemplate,
                needSubscribers: needSubscribers
        ]

        return result
    }

    def handleReports(Campaign campaign) {
        def model = [:]

        def totalClicksReport = reportsHolder.getReport('totalClicks')
        def totalClicks = totalClicksReport.extract([campaignId: campaign.id])
        model.totalClicks = totalClicks.single('clicks')

        def totalOpensReport = reportsHolder.getReport('totalOpens')
        def totalOpens = totalOpensReport.extract([campaignId: campaign.id])
        model.totalOpens = totalOpens.single('opens')

        def openedReport = reportsHolder.getReport('opened')
        def opened = openedReport.extract([campaignId: campaign.id])
        model.opened = opened.single('number') ?: 0

        def totalSubscribersReport = reportsHolder.getReport('totalSubscribers')
        def totalSubscribers = totalSubscribersReport.extract([campaignId: campaign.id])
        model.totalSubscribers = totalSubscribers.single('number') ?: 0

        model.notOpened = model.totalSubscribers - model.opened

        def period = ReportUtil.bestPeriod(campaign.startDate)
        model.period = period

        def clicksByDateReport = reportsHolder.getReport('clicksByDate')
        def clicksByDate = clicksByDateReport.extract([campaignId: campaign.id, period: period])
        model.clicksByDate = clicksByDate.dataSet('clicks')?.list()

        def opensByDateReport = reportsHolder.getReport('opensByDate')
        def opensByDate = opensByDateReport.extract([campaignId: campaign.id, period: period])
        model.opensByDate = opensByDate.dataSet('opens')?.list()

        return model
    }

    def handleDetails(Campaign campaign) {
        // do nothing yet
    }

    def handleSubscribers(Campaign campaign) {
        def model = [:]
        model.proposedSubscriptions = campaignService.getProposedSubscriptionLists(campaign)
        model.totalSubscribers = campaignService.getTotalSubscribersNumber(campaign)
        model.subscriptions = campaignService.getCampaignSubscriptions(campaign)
        return model
    }

    def handleTemplate(Campaign campaign) {
        [proposedTemplates : campaignService.getProposedTemplates(campaign)]
    }

    private String fetchPage(Campaign campaign) {
        def page = params.page
        if (!page) {
            page = campaign.hasReports ? 'reports' : 'details'
        }
        else if (page == 'reports' && !campaign.hasReports) {
            page = 'details'
        }
        else if (!SHOW_HANDLERS.contains(page)) {
            page = 'details'
        }
        return page
    }

    def addSubscriptionList = {
        def model = [:]
        def campaign = campaignService.getCampaign(params.long('campaignId'))
        def memberId = springSecurityService.principal.id
        if (campaign && campaign.ownedBy(memberId) && campaign.notStarted) {
            def subscriptionList = subscriptionListService.getSubscriptionList(params.long('subscriptionList'))
            if (subscriptionList && subscriptionList.ownedBy(memberId)) {
                CampaignSubscription campaignSubscription = new CampaignSubscription()
                campaignSubscription.campaign = campaign
                campaignSubscription.subscriptionList = subscriptionList
                if (campaignService.addCampaignSubscription(campaignSubscription)) {
                    model.success = true
                    def data = handle('subscribers', campaign)
                    model.content = g.render(template: 'campaignSubscribers', model: data)
                    model.notifications = g.render(template: 'campaignNotifications', model: data)
                    model.actions = g.render(template: 'campaignActions', model: data)
                    model.stateName = message(code: campaign.state.messageCode)
                }
                else {
                    MessageUtil.addErrors(request, model, campaignSubscription.errors)
                }
            }
        }

        if (!model.success) {
            model.error = true
        }
        
        render model as JSON
    }

    def removeSubscriptionList = {
        def model = [:]
        def campaignSubscription = campaignService.getCampaignSubscription(params.long('campaignSubscriptionId'))
        def memberId = springSecurityService.principal.id
        def campaign = campaignSubscription?.campaign
        if (campaign && campaign.ownedBy(memberId) && campaign.notStarted) {
            if (campaignService.deleteCampaignSubscription(campaignSubscription)) {
                model.success = true
                def data = handle('subscribers', campaignSubscription.campaign)
                model.content = g.render(template: 'campaignSubscribers', model: data)
                model.notifications = g.render(template: 'campaignNotifications', model: data)
                model.actions = g.render(template: 'campaignActions', model: data)
                model.stateName = message(code: campaign.state.messageCode)
            }
            else {
                MessageUtil.addErrors(request, model, campaignSubscription.errors)
            }
        }

        if (!model.success) {
            model.error = true
        }

        render model as JSON
    }

    def selectTemplate = {
        def model = [:]
        def campaign = campaignService.getCampaign(params.long('campaignId'))
        def memberId = springSecurityService.principal.id
        if (campaign && campaign.ownedBy(memberId) && campaign.notStarted) {
            def template = templateService.getTemplate(params.long('template'))
            if (template && template.ownedBy(memberId)) {
                campaign.template = template
                if (campaignService.saveCampaign(campaign)) {
                    model.success = true
                    
                    def data = handle('template', campaign)
                    model.content = g.render(template: 'campaignTemplate', model: data)
                    model.notifications = g.render(template: 'campaignNotifications', model: data)
                    model.actions = g.render(template: 'campaignActions', model: data)
                    model.stateName = message(code: campaign.state.messageCode)
                }
                else {
                    MessageUtil.addErrors(request, model, campaign.errors)
                }
            }
        }

        if (!model.success) {
            model.error = true
        }

        render model as JSON
    }
}
