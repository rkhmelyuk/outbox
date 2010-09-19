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

    def edit = {

    }

    def update = {

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

        [campaign: campaign] 
    }

    def details = {
        
    }
}
