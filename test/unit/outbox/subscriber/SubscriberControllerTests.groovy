package outbox.subscriber

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.Timezone
import outbox.member.Member
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

    void testShow_NotFound() {
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return null }
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.id = 'test123'

        def result = controller.show()

        assertNull result
        assertEquals 404, mockResponse.status
    }

    void testShow_Denied() {
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

    void testCreate() {
        def result = controller.create()

        assertNotNull result
        assertNotNull result.subscriber
        assertNull result.subscriber.id
        assertNull result.subscriber.gender
        assertNull result.subscriber.language
        assertNull result.subscriber.timezone
        assertTrue 'Subscriber must be active', result.subscriber.enabled
    }

    void testAdd_Success() {
        def member = new Member(id: 1)
        
        Language.class.metaClass.static.load = { id -> return null}
        Gender.class.metaClass.static.load = { id -> return null}
        Timezone.class.metaClass.static.load = { id -> return null}
        Member.class.metaClass.static.load = { id -> return member}

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.saveSubscriber { subscriber -> return true}

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member) }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.firstName = 'Test'
        controller.params.lastName = 'Subscriber'
        controller.params.email = 'test@mailsight.com'

        controller.add()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Success is expected.', result.success
        assertNull 'Error is unexpected.', result.error
    }

    void testAdd_Fail() {
        def member = new Member(id: 1)

        mockDomain(Subscriber)

        Language.class.metaClass.static.load = { id -> return null}
        Gender.class.metaClass.static.load = { id -> return null}
        Timezone.class.metaClass.static.load = { id -> return null}
        Member.class.metaClass.static.load = { id -> return member}

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.saveSubscriber { subscriber -> return false}

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member) }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.firstName = 'Test'
        controller.params.lastName = 'Subscriber'
        controller.params.email = ''

        controller.add()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        
        assertTrue 'Error is expected.', result.error
        assertNull 'Success is unexpected.', result.success
        assertNotNull result.errors
    }

    void testEdit() {
        Subscriber subscriber = new Subscriber(id: '0000000')

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.id = '0000000'

        def result = controller.edit()

        assertNotNull result
        assertEquals 'Subscriber is not found.', subscriber, result.subscriber
    }

    void testEditAbsent() {
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> null}
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.id = '0000000'

        def result = controller.edit()

        assertNull result
        assertEquals 404, mockResponse.status
    }

    void testUpdate_Success() {
        def member = new Member(id: 1)
        def subscriber = new Subscriber(email: 'test@mailsight.com', member: member)

        mockDomain(Subscriber, [subscriber])

        Language.class.metaClass.static.load = { id -> return null}
        Gender.class.metaClass.static.load = { id -> return new Gender(id: 2)}
        Timezone.class.metaClass.static.load = { id -> return null}
        Member.class.metaClass.static.load = { id -> return member}

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        subscriberServiceControl.demand.saveSubscriber { subscr -> return true}
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.email = 'test2@mailsight.com'
        controller.params.firstName = 'First'
        controller.params.lastName = 'Last'
        controller.params.enabled = 'true'
        controller.params.gender = 2

        controller.update()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Success is expected.', result.success
        assertNull 'Error is unexpected.', result.error

        assertEquals 'test2@mailsight.com', subscriber.email
        assertEquals 'First', subscriber.firstName
        assertEquals 'Last', subscriber.lastName
        assertEquals 2, subscriber.gender.id
        assertTrue subscriber.enabled
    }

    void testUpdate_Success2() {
        def member = new Member(id: 1)
        def subscriber = new Subscriber(email: 'test@mailsight.com', member: member, enabled: true)

        mockDomain(Subscriber, [subscriber])

        Language.class.metaClass.static.load = { id -> return null }
        Gender.class.metaClass.static.load = { id -> return null }
        Timezone.class.metaClass.static.load = { id -> return null }
        Member.class.metaClass.static.load = { id -> return member }

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        subscriberServiceControl.demand.saveSubscriber { subscr -> return true}
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.email = 'test@mailsight.com'

        controller.update()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Success is expected.', result.success
        assertNull 'Error is unexpected.', result.error

        assertFalse subscriber.enabled
    }

    void testUpdate_Fail() {
        def member = new Member(id: 1)
        def subscriber = new Subscriber(email: 'test@mailsight.com', member: member)

        mockDomain(Subscriber, [subscriber])

        Language.class.metaClass.static.load = { id -> return null}
        Gender.class.metaClass.static.load = { id -> return null}
        Timezone.class.metaClass.static.load = { id -> return null}

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        subscriberServiceControl.demand.saveSubscriber { subscr -> return false}
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.email = ''

        controller.update()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Error is expected.', result.error
        assertNull 'Success is unexpected.', result.success
        assertNotNull result.errors
    }

    void testUpdate_NotFound() {
        
        mockDomain(Subscriber)

        Language.class.metaClass.static.load = { id -> return null}
        Gender.class.metaClass.static.load = { id -> return null}
        Timezone.class.metaClass.static.load = { id -> return null}

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return null}
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.email = 'test@mailsight.com'

        controller.update()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Error is expected.', result.error
        assertNull 'Success is unexpected.', result.success
        assertNull result.errors
    }

}
