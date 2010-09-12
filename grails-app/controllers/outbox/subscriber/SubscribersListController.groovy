package outbox.subscriber

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class SubscribersListController {

    static allowedMethods = [add: 'POST', update: 'POST']

    SubscribersListService subscribersListService
    SpringSecurityService springSecurityService

    def list = {
        def memberId = springSecurityService.principal.id
        def subscribersLists = subscribersListService.getMemberSubscribersList(Member.load(memberId))

        [subscribersLists: subscribersLists]
    }

    def show = {
        def subscribersList = subscribersListService.getSubscribersList(params.long('id'))
        if (!subscribersList) {
            response.sendError 404
            return
        }
        if (!subscribersList.ownedBy(springSecurityService.principal.id)) {
            response.sendError 403
            return
        }
        [subscribersList: subscribersList]
    }

    def create = {
        [subscribersList: new SubscribersList()]
    }

    def add = {
        final SubscribersList subscribersList = new SubscribersList()
        subscribersList.name = params.name
        subscribersList.description = params.description
        subscribersList.owner = Member.load(springSecurityService.principal.id)

        def model = [:]
        if (subscribersListService.saveSubscribersList(subscribersList)) {
            model.success = true
        }
        else {
            model.error = true
            MessageUtil.addErrors(request, model, subscribersList.errors);
        }

        render model as JSON
    }

    def edit = {
        SubscribersList subscribersList = subscribersListService.getSubscribersList(params.long('id'))
        if (!subscribersList) {
            response.sendError 404
            return
        }
        if (!subscribersList.ownedBy(springSecurityService.principal.id)) {
            response.sendError 403
            return
        }

        [subscribersList : subscribersList]
    }

    def update = {
        def model = [:]
        final SubscribersList subscribersList = subscribersListService.getSubscribersList(params.long('id'))
        if (subscribersList && subscribersList.ownedBy(springSecurityService.principal.id)) {
            subscribersList.name = params.name
            subscribersList.description = params.description

            if (subscribersListService.saveSubscribersList(subscribersList)) {
                model.success = true
            }
            else {
                MessageUtil.addErrors(request, model, subscribersList.errors);
            }
        }

        if (!model.success) {
            model.error = true
        }

        render model as JSON
    }

    def delete = {
        final SubscribersList subscribersList = subscribersListService.getSubscribersList(params.long('id'))
        if (subscribersList && subscribersList.ownedBy(springSecurityService.principal.id)) {
            subscribersListService.deleteSubscribersList subscribersList
        }
        redirect controller: 'subscribersList'
    }
}
