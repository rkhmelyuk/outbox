package outbox.subscription

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class SubscriptionListController {

    static allowedMethods = [add: 'POST', update: 'POST']

    SubscriptionListService subscriptionListService
    SpringSecurityService springSecurityService

    def list = {
        def memberId = springSecurityService.principal.id
        def subscriptionLists = subscriptionListService.getMemberSubscriptionList(Member.load(memberId))

        [subscriptionLists: subscriptionLists]
    }

    def show = {
        def subscriptionList = subscriptionListService.getSubscriptionList(params.long('id'))
        if (!subscriptionList) {
            response.sendError 404
            return
        }
        if (!subscriptionList.ownedBy(springSecurityService.principal.id)) {
            response.sendError 403
            return
        }
        [subscriptionList: subscriptionList]
    }

    def create = {
        [subscriptionList: new SubscriptionList()]
    }

    def add = {
        final SubscriptionList subscriptionList = new SubscriptionList()
        subscriptionList.name = params.name
        subscriptionList.description = params.description
        subscriptionList.owner = Member.load(springSecurityService.principal.id)

        def model = [:]
        if (subscriptionListService.saveSubscriptionList(subscriptionList)) {
            model.success = true
        }
        else {
            model.error = true
            MessageUtil.addErrors(request, model, subscriptionList.errors);
        }

        render model as JSON
    }

    def edit = {
        SubscriptionList subscriptionList = subscriptionListService.getSubscriptionList(params.long('id'))
        if (!subscriptionList) {
            response.sendError 404
            return
        }
        if (!subscriptionList.ownedBy(springSecurityService.principal.id)) {
            response.sendError 403
            return
        }

        [subscriptionList : subscriptionList]
    }

    def update = {
        def model = [:]
        final SubscriptionList subscriptionList = subscriptionListService.getSubscriptionList(params.long('id'))
        if (subscriptionList && subscriptionList.ownedBy(springSecurityService.principal.id)) {
            subscriptionList.name = params.name
            subscriptionList.description = params.description

            if (subscriptionListService.saveSubscriptionList(subscriptionList)) {
                model.success = true
            }
            else {
                MessageUtil.addErrors(request, model, subscriptionList.errors);
            }
        }

        if (!model.success) {
            model.error = true
        }

        render model as JSON
    }

    def delete = {
        final SubscriptionList subscriptionList = subscriptionListService.getSubscriptionList(params.long('id'))
        if (subscriptionList && subscriptionList.ownedBy(springSecurityService.principal.id)) {
            subscriptionListService.deleteSubscriptionList subscriptionList
        }
        redirect controller: 'subscriptionList', action: ''
    }
}
