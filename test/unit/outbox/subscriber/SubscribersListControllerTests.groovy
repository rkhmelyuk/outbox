package outbox.subscriber

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser

/**
 * {@link SubscribersListController} tests.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscribersListControllerTests extends ControllerUnitTestCase {

    void testList() {
        def member = new Member(id: 10)
        def subscribersLists = null

        Member.class.metaClass.static.load = { id -> member }

        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.getMemberSubscribersList { it ->
            assertEquals member.id, it.id;
            return subscribersLists
        }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.list()
        
        assertNotNull result
        assertEquals subscribersLists, result.subscribersLists
    }
}
