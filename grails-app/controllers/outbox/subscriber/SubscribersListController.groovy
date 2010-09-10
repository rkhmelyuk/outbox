package outbox.subscriber

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class SubscribersListController {

    static defaultAction = 'list'
    static allowedMethods = []

    SubscribersListService subscribersListService
    SpringSecurityService springSecurityService

    def list = {
        def memberId = springSecurityService.principal.id
        def subscribersLists = subscribersListService.getMemberSubscribersList(Member.load(memberId))

        [subscribersLists: subscribersLists]
    }
}
