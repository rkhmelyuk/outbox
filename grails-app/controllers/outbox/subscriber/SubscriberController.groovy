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

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_SYSADMIN', 'ROLE_CLIENT'])
class SubscriberController {

    static defaultAction = 'show'

    SubscriberService subscriberService
    SpringSecurityService springSecurityService

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

        [subscriber: subscriber, subscriberTypes: subscriberTypes]
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

        render(model as JSON)
    }

    def create = {
        def memberId = springSecurityService.principal.id
        def subscriberTypes = subscriberService.getSubscriberTypes(Member.load(memberId))

        [subscriber: new Subscriber(enabled: true), subscriberTypes: subscriberTypes]
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

        def model = [:]
        if (subscriberService.saveSubscriber(subscriber)) {
            model << [success: true]
        }

        if (!model.success) {
            model << [error: true]
            MessageUtil.addErrors(request, model, subscriber.errors);
        }

        render(model as JSON)
    }

    def types = {
        def id = springSecurityService.principal.id
        def types = subscriberService.getSubscriberTypes(Member.load(id))
        
        [subscriberTypes: types]
    }
}
