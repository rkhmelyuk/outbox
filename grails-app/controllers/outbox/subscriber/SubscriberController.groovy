package outbox.subscriber

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_SYSADMIN', 'ROLE_CLIENT'])
class SubscriberController {

    static defaultAction = 'show'

    SubscriberService subscriberService
    SpringSecurityService springSecurityService

    def show = {
        def id = params.long('id')
        Subscriber subscriber = subscriberService.getSubscriber(id)
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
