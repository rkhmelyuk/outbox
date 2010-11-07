package outbox.subscriber

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.NamePrefix
import outbox.dictionary.Timezone
import outbox.member.Member
import outbox.subscriber.field.DynamicFieldValues
import outbox.subscription.Subscription
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService
import outbox.subscription.SubscriptionStatus
import outbox.ui.EditDynamicFieldsFormBuilder

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class SubscriberController {

    static defaultAction = 'show'
    static allowedMethods = [updateSubscriberType: 'POST', addSubscriberType: 'POST',
            deleteSubscriberType: 'POST', update: 'POST', add: 'POST']

    SubscriberService subscriberService
    DynamicFieldService dynamicFieldService
    SubscriptionListService subscriptionListService
    SpringSecurityService springSecurityService
    EditDynamicFieldsFormBuilder editDynamicFieldsFormBuilder

    def show = {
        Subscriber subscriber = subscriberService.getSubscriber(params.id)
        if (!subscriber) {
            response.sendError 404
            return
        }
        else if (subscriber.member?.id != springSecurityService.principal.id) {
            response.sendError 403
            return
        }

        [subscriber: subscriber]
    }

    def edit = {
        def subscriber = subscriberService.getSubscriber(params.id)
        if (!subscriber) {
            response.sendError 404
            return
        }

        def memberId = springSecurityService.principal.id
        def subscriberTypes = subscriberService.getSubscriberTypes(Member.load(memberId))

        def dynamicFieldValues = subscriberService.getSubscriberDynamicFields(subscriber)
        def dynamicFieldsForm = editDynamicFieldsFormBuilder.build(dynamicFieldValues)
        [subscriber: subscriber, subscriberTypes: subscriberTypes, dynamicFieldsForm: dynamicFieldsForm]
    }

    def update = {
        def model = [:]
        final Subscriber subscriber = subscriberService.getSubscriber(params.id)
        if (subscriber) {
            subscriber.firstName = params.firstName
            subscriber.lastName = params.lastName
            subscriber.email = params.email
            subscriber.enabled = params.enabled ? true : false
            subscriber.gender = Gender.load(params.int('gender'))
            subscriber.language = Language.load(params.int('language'))
            subscriber.timezone = Timezone.load(params.int('timezone'))
            subscriber.namePrefix = NamePrefix.load(params.int('namePrefix'))
            subscriber.subscriberType = SubscriberType.load(params.long('subscriberType'))

            if (subscriberService.saveSubscriber(subscriber)) {
                model << [success: true]
            }
            else {
                MessageUtil.addErrors(request, model, subscriber.errors);
            }
        }

        if (!model.success) {
            model << [error: true]
        }

        render model as JSON
    }

    def create = {
        def member = Member.load(springSecurityService.principal.id)
        def subscriberTypes = subscriberService.getSubscriberTypes(member)

        def dynamicFields = dynamicFieldService.getDynamicFields(member)
        def dynamicFieldsForm = editDynamicFieldsFormBuilder.build(new DynamicFieldValues(dynamicFields, []))

        return [
                subscriber: new Subscriber(enabled: true),
                subscriberTypes: subscriberTypes,
                listId: params.list,
                dynamicFieldsForm: dynamicFieldsForm]
    }

    def add = {
        final Subscriber subscriber = new Subscriber()
        subscriber.firstName = params.firstName
        subscriber.lastName = params.lastName
        subscriber.email = params.email
        subscriber.enabled = params.enabled ? true : false
        subscriber.gender = Gender.load(params.int('gender'))
        subscriber.language = Language.load(params.int('language'))
        subscriber.timezone = Timezone.load(params.int('timezone'))
        subscriber.member = Member.load(springSecurityService.getPrincipal().id)
        subscriber.namePrefix = NamePrefix.load(params.int('namePrefix'))
        subscriber.subscriberType = SubscriberType.load(params.long('subscriberType'))

        def model = [:]
        if (subscriberService.saveSubscriber(subscriber)) {
            def listId = params.long('listId')
            model.success = true
            if (listId) {
                subscribe(subscriber, listId)
                model.redirectTo = g.createLink(controller: 'subscriptionList', action: 'show', id: listId)
            }
            else {
                model.redirectTo = g.createLink(controller: 'subscriptionList', action: 'freeSubscribers')
            }
        }
        else {
            model.error = true
            MessageUtil.addErrors(request, model, subscriber.errors);
        }

        render model as JSON
    }

    private void subscribe(Subscriber subscriber, Long listId) {
        if (!listId) {
            return
        }

        SubscriptionList list = subscriptionListService.getSubscriptionList(listId)
        if (list && list.ownedBy(subscriber.member.id)) {
            Subscription subscription = new Subscription()
            subscription.subscriber = subscriber
            subscription.subscriptionList = list
            subscription.status = SubscriptionStatus.subscribed()

            subscriptionListService.addSubscription subscription
        }
    }

    def types = {
        def id = springSecurityService.principal.id
        def subscriberTypes = subscriberService.getSubscriberTypes(Member.load(id))

        [subscriberTypes: subscriberTypes]
    }

    def addSubscriberType = {
        def memberId = springSecurityService.principal.id
        def subscriberType = new SubscriberType(
                name: params.name,
                member: Member.load(memberId))

        def model = [:]
        if (subscriberService.addSubscriberType(subscriberType)) {
            model << [success: true, subscriberType: [id: subscriberType.id, name: subscriberType.name]]
        }
        else {
            model << [error: true]
            MessageUtil.addErrors(request, model, subscriberType.errors)
        }

        render model as JSON
    }

    def updateSubscriberType = {
        def model = [:]
        def memberId = springSecurityService.principal.id
        def subscriberType = subscriberService.getMemberSubscriberType(memberId, params.long('id'))
        if (subscriberType) {
            subscriberType.name = params.name

            if (subscriberService.saveSubscriberType(subscriberType)) {
                model << [success: true, subscriberType: [id: subscriberType.id, name: subscriberType.name]]
            }
            else {
                model << [error: true]
                MessageUtil.addErrors(request, model, subscriberType.errors)
            }
        }
        else {
            model << [error: true]
        }

        render model as JSON
    }

    def deleteSubscriberType = {
        def model = [:]
        def memberId = springSecurityService.principal.id
        def subscriberType = subscriberService.getMemberSubscriberType(memberId, params.long('id'))
        if (subscriberType) {
            subscriberService.deleteSubscriberType(subscriberType)
            model.success = true
        }
        else {
            model.error = true
        }

        render model as JSON
    }
}
