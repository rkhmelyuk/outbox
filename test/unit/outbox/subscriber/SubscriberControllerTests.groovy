package outbox.subscriber

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.NamePrefix
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
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member}

        def subscriberServiceControl = mockFor(SubscriberService)
        def Subscriber subscriber = null
        subscriberServiceControl.demand.getSubscriberTypes { m -> [new SubscriberType(id: 1, member: m)]}

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member) }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.create()

        assertNotNull result
        assertNotNull result.subscriber
        assertNull result.subscriber.id
        assertNull result.subscriber.gender
        assertNull result.subscriber.language
        assertNull result.subscriber.timezone
        assertTrue 'Subscriber must be active', result.subscriber.enabled
        assertNotNull result.subscriberTypes
        assertEquals 1, result.subscriberTypes.size()
        assertEquals 1, result.subscriberTypes[0].id
        assertEquals member, result.subscriberTypes[0].member
    }

    void testAdd_Success() {
        def member = new Member(id: 1)
        
        Gender.class.metaClass.static.load = { id -> return new Gender(id: id) }
        Language.class.metaClass.static.load = { id -> return new Language(id: id) }
        Timezone.class.metaClass.static.load = { id -> return new Timezone(id: id) }
        NamePrefix.class.metaClass.static.load = { id -> return new NamePrefix(id: id) }
        Member.class.metaClass.static.load = { id -> return member}

        def subscriberServiceControl = mockFor(SubscriberService)
        def Subscriber subscriber = null
        subscriberServiceControl.demand.saveSubscriber { subscr -> subscriber = subscr; return true}
        subscriberServiceControl.demand.getSubscriberTypes { m -> [new SubscriberType(id: 1, member: m)]}

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member) }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.firstName = 'Test'
        controller.params.lastName = 'Subscriber'
        controller.params.email = 'test@mailsight.com'
        controller.params.gender = '2'
        controller.params.timezone = '3'
        controller.params.language = '4'
        controller.params.namePrefix = '5'

        controller.add()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Success is expected.', result.success
        assertNull 'Error is unexpected.', result.error

        assertNotNull subscriber
        assertEquals 'Test', subscriber.firstName
        assertEquals 'Subscriber', subscriber.lastName
        assertEquals 'test@mailsight.com', subscriber.email
        assertEquals 2, subscriber.gender.id
        assertEquals 3, subscriber.timezone.id
        assertEquals 4, subscriber.language.id
        assertEquals 5, subscriber.namePrefix.id
    }

    void testAdd_Fail() {
        def member = new Member(id: 1)

        mockDomain(Subscriber)

        Language.class.metaClass.static.load = { id -> return null }
        Gender.class.metaClass.static.load = { id -> return null }
        Timezone.class.metaClass.static.load = { id -> return null }
        NamePrefix.class.metaClass.static.load = { id -> return null }
        Member.class.metaClass.static.load = { id -> return member }

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
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member }
        
        def subscriber = new Subscriber(id: '0000000', member: member)

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        subscriberServiceControl.demand.getSubscriberTypes { m -> [new SubscriberType(id: 1, member: m)]}
        controller.subscriberService = subscriberServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member) }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '0000000'

        def result = controller.edit()

        assertNotNull result
        assertEquals 'Subscriber is not found.', subscriber, result.subscriber
        assertNotNull result.subscriberTypes
        assertEquals 1, result.subscriberTypes.size()
        assertEquals 1, result.subscriberTypes[0].id
        assertEquals member, result.subscriberTypes[0].member
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

        Gender.class.metaClass.static.load = { id -> return new Gender(id: id) }
        Language.class.metaClass.static.load = { id -> return new Language(id: id) }
        Timezone.class.metaClass.static.load = { id -> return new Timezone(id: id) }
        NamePrefix.class.metaClass.static.load = { id -> return new NamePrefix(id: id) }
        Member.class.metaClass.static.load = { id -> return member}

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        subscriberServiceControl.demand.saveSubscriber { subscr -> return true}
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.email = 'test2@mailsight.com'
        controller.params.firstName = 'First'
        controller.params.lastName = 'Last'
        controller.params.gender = '2'
        controller.params.timezone = '3'
        controller.params.language = '4'
        controller.params.namePrefix = '5'

        controller.update()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Success is expected.', result.success
        assertNull 'Error is unexpected.', result.error

        assertEquals 'test2@mailsight.com', subscriber.email
        assertEquals 'First', subscriber.firstName
        assertEquals 'Last', subscriber.lastName
        assertEquals 2, subscriber.gender.id
        assertEquals 3, subscriber.timezone.id
        assertEquals 4, subscriber.language.id
        assertEquals 5, subscriber.namePrefix.id
        assertEquals member.id, subscriber.member.id
    }

    void testUpdate_EnabledOn() {
        def member = new Member(id: 1)
        def subscriber = new Subscriber(email: 'test@mailsight.com', member: member)

        mockDomain(Subscriber, [subscriber])

        Language.class.metaClass.static.load = { id -> return null}
        Gender.class.metaClass.static.load = { id -> return new Gender(id: 2)}
        Timezone.class.metaClass.static.load = { id -> return null}
        NamePrefix.class.metaClass.static.load = { id -> return null }
        Member.class.metaClass.static.load = { id -> return member}

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        subscriberServiceControl.demand.saveSubscriber { subscr -> return true}
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.email = 'test@mailsight.com'
        controller.params.enabled = 'true'

        controller.update()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Success is expected.', result.success
        assertNull 'Error is unexpected.', result.error
        assertTrue subscriber.enabled
    }

    void testUpdate_EnabledOff() {
        def member = new Member(id: 1)
        def subscriber = new Subscriber(email: 'test@mailsight.com', member: member, enabled: true)

        mockDomain(Subscriber, [subscriber])

        Language.class.metaClass.static.load = { id -> return null }
        Gender.class.metaClass.static.load = { id -> return null }
        Timezone.class.metaClass.static.load = { id -> return null }
        NamePrefix.class.metaClass.static.load = { id -> return null }
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
        NamePrefix.class.metaClass.static.load = { id -> return null }

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
        NamePrefix.class.metaClass.static.load = { id -> return null }

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

    void testTypes() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member }

        mockDomain(SubscriberType)

        def subscriberTypes = [
                new SubscriberType(id: 1, name: 'Test1'),
                new SubscriberType(id: 2, name: 'Test2')]

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriberTypes { m -> m.id == member.id ? subscriberTypes : null }
        controller.subscriberService = subscriberServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
                springSecurityServiceControl.demand.getPrincipal { ->
                    return new OutboxUser('username', 'password', true, false, false, false, [], member) }

        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.types()

        assertNotNull result
        assertNotNull result.subscriberTypes
        assertEquals 2, result.subscriberTypes.size()
    }

}
