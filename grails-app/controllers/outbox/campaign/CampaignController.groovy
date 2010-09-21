package outbox.campaign

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class CampaignController {

    def defaultAction = ''
    def allowedMethods = [add: 'POST', update: 'POST']

    static def SHOW_MODES = ['reports', 'details', 'subscribers', 'template', 'edit']

    CampaignService campaignService
    SpringSecurityService springSecurityService

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
            campaign.subject = params.subject
            campaign.description = params.description
            campaign.endDate = params.endDate

            if (campaignService.saveCampaign(campaign)) {
                model.success = true
                model.name = campaign.name
                model.redirectTo = g.createLink(controller: 'campaign', action: 'show', id: campaign.id)
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

        def needTemplate = (campaign.template == null)
        def needSubscribers = true

        return [
                page: fetchPage(campaign),
                campaign: campaign,
                needTemplate: needTemplate,
                needSubscribers: needSubscribers
        ]
    }

    private String fetchPage(Campaign campaign) {
        def page = params.page
        if (!page) {
            page = campaign.hasReports ? 'reports' : 'details'
        }
        else if (page == 'reports' && !campaign.hasReports) {
            page = 'details'
        }
        else if (!SHOW_MODES.contains(page)) {
            page = 'details'
        }
        return page
    }

    def details = {
        
    }
}
