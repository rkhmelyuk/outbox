package outbox.subscriber

import grails.test.*
import outbox.member.Member
import grails.plugins.springsecurity.SpringSecurityService
import outbox.security.OutboxUser

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberControllerTests extends ControllerUnitTestCase {

    void testShow() {
        Member member = new Member(id: 1)
        OutboxUser principal = new OutboxUser('username', 'password', true, false, false, false, [], member)
        Subscriber subscriber = new Subscriber(id: 'test123', member: member)

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { -> return principal }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 'test123'

        def result = controller.show()

        assertNotNull result
        assertEquals subscriber, result.subscriber
    }

    void testNotFound() {
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return null }
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.id = 'test123'

        def result = controller.show()

        assertNull result
        assertEquals 404, mockResponse.status
    }

    void testDeniedSubscriber() {
        OutboxUser principal = new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 2))
        Subscriber subscriber = new Subscriber(id: 'test123', member: new Member(id: 1))

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { -> return principal }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 'test123'

        def result = controller.show()

        assertNull result
        assertEquals 403, mockResponse.status
    }
}
