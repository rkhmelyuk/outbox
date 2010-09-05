package outbox.subscriber

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService

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
        else if (subscriber.member?.id != springSecurityService.getPrincipal().id) {
            response.sendError 403
            return
        }
        
        [subscriber: subscriber]
    }

    def edit = {

    }

    def update = {

    }

    def create = {

    }

    def save = {
        
    }
}
