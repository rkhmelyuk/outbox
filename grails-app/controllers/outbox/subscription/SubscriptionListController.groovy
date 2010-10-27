package outbox.subscription

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.campaign.CampaignService
import outbox.member.Member
import outbox.subscriber.SubscriberService

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class SubscriptionListController {

    static allowedMethods = [add: 'POST', update: 'POST']

    SubscriberService subscriberService
    SubscriptionListService subscriptionListService
    SpringSecurityService springSecurityService
    CampaignService campaignService

    /**
     * Shows the list of Subscription Lists.
     */
    def list = { SubscriptionListConditions conditions ->
        def member = Member.load(springSecurityService.principal.id)
        int freeSubscribersCount = subscriberService.getSubscribersWithoutSubscriptionCount(member)

        def searchConditions = new SubscriptionListConditionsBuilder().build {
            ownedBy member
            archived false
            max conditions.itemsPerPage
            page conditions.page
            returnCount true
            order conditions.column, conditions.sort
        }

        def result = subscriptionListService.search(searchConditions)

        return [
                subscriptionLists: result.list,
                total: result.total,
                freeSubscribersCount: freeSubscribersCount,
                conditions: conditions
        ]
    }

    /**
     * Shows the list of Subscription Lists.
     */
    def archived = { SubscriptionListConditions conditions ->
        def member = Member.load(springSecurityService.principal.id)
        def searchConditions = new SubscriptionListConditionsBuilder().build {
            ownedBy member
            archived true
            max conditions.itemsPerPage
            page conditions.page
            returnCount true
            order conditions.column, conditions.sort
        }

        def result = subscriptionListService.search(searchConditions)
        return [
                subscriptionLists: result.list,
                total: result.total,
                conditions: conditions
        ]
    }

    /**
     * Shows the list of subscribers without subscription if not specified other option).
     */
    def freeSubscribers = {
        def member = Member.load(springSecurityService.principal.id)
        def subscribersList = subscriberService.getSubscribersWithoutSubscription(member)
        
        [subscribers: subscribersList]
    }

    /**
     * Shows specified Subscription list or 404 if not found or 403 if not permitted.
     */
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

        def subscriptions = subscriptionListService.getSubscriptionsForList(subscriptionList)
        def campaignSubscriptions = campaignService.getSubscriptions(subscriptionList)

        return [
                subscriptionList: subscriptionList,
                subscriptions: subscriptions,
                campaignSubscriptions: campaignSubscriptions,
                canDelete: campaignSubscriptions.empty
        ]
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
            model.redirectTo = g.createLink(controller: 'subscriptionList')
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

        [subscriptionList: subscriptionList]
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
        def subscriptionList = subscriptionListService.getSubscriptionList(params.long('id'))
        if (subscriptionList && subscriptionList.ownedBy(springSecurityService.principal.id)) {
            if (!subscriptionListService.deleteSubscriptionList(subscriptionList)) {
                redirect controller: 'subscriptionList', action: 'show', id: subscriptionList.id
                return
            }
        }
        redirect controller: 'subscriptionList', action: ''
    }

    def archive = {
        def subscriptionList = subscriptionListService.getSubscriptionList(params.long('id'))
        if (subscriptionList && subscriptionList.ownedBy(springSecurityService.principal.id)) {
            subscriptionListService.archiveSubscriptionList(subscriptionList)
            redirect action: 'show', id: subscriptionList.id
            return
        }
        redirect controller: 'subscriptionList', action: ''
    }

    def restore = {
        def subscriptionList = subscriptionListService.getSubscriptionList(params.long('id'))
        if (subscriptionList && subscriptionList.ownedBy(springSecurityService.principal.id)) {
            subscriptionListService.restoreSubscriptionList(subscriptionList)
            redirect action: 'show', id: subscriptionList.id
            return
        }
        redirect controller: 'subscriptionList', action: ''
    }

    /**
     * Filter conditions.
     */
    static class SubscriptionListConditions {
        int page = 1
        int itemsPerPage = 10

        String column
        String sort
    }
}
