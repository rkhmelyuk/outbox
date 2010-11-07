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
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldValues
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService
import outbox.subscription.SubscriptionStatus
import outbox.ui.EditDynamicFieldsFormBuilder
import outbox.ui.ViewDynamicFieldsFormBuilder
import outbox.ui.element.UIContainer

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberControllerTests extends ControllerUnitTestCase {

    protected void setUp() {
        super.setUp();
        controller.class.metaClass.createLink = { null }
    }

    void testShow() {
        Member member = new Member(id: 1)
        OutboxUser principal = new OutboxUser('username', 'password', true, false, false, false, [], member)
        Subscriber subscriber = new Subscriber(id: 'test123', member: member)

        def dynamicFieldValues = new DynamicFieldValues([], [])
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber }
        subscriberServiceControl.demand.getSubscriberDynamicFields { s ->
            assertEquals subscriber, s
            return dynamicFieldValues
        }

        def elements = new UIContainer()
        def viewDynamicFieldsFormBuilderControl = mockFor(ViewDynamicFieldsFormBuilder)
        viewDynamicFieldsFormBuilderControl.demand.build { f ->
            assertEquals dynamicFieldValues, f
            return elements
        }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {-> return principal }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()
        controller.viewDynamicFieldsFormBuilder = viewDynamicFieldsFormBuilderControl.createMock()

        controller.params.id = 'test123'

        def result = controller.show()

        subscriberServiceControl.verify()
        springSecurityServiceControl.verify()
        viewDynamicFieldsFormBuilderControl.verify()

        assertNotNull result
        assertEquals subscriber, result.subscriber
        assertEquals elements, result.dynamicFieldsForm
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
        springSecurityServiceControl.demand.getPrincipal {-> return principal }

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
        subscriberServiceControl.demand.getSubscriberTypes { m -> [new SubscriberType(id: 1, member: m)]}

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicFields { m -> [new DynamicField(id: 2)] }

        def elements = new UIContainer()
        def editDynamicFieldsFormBuilderControl = mockFor(EditDynamicFieldsFormBuilder)
        editDynamicFieldsFormBuilderControl.demand.build { f ->
            return elements
        }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()
        controller.editDynamicFieldsFormBuilder = editDynamicFieldsFormBuilderControl.createMock()

        def result = controller.create()

        subscriberServiceControl.verify()
        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()
        editDynamicFieldsFormBuilderControl.verify()

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
        assertEquals elements, result.dynamicFieldsForm
    }

    void testCreate_WithSubscription() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member}

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriberTypes { m -> [new SubscriberType(id: 1, member: m)]}

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicFields { m -> [new DynamicField(id: 2)] }

        def editDynamicFieldsFormBuilderControl = mockFor(EditDynamicFieldsFormBuilder)
        editDynamicFieldsFormBuilderControl.demand.build { f -> return new UIContainer() }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()
        controller.editDynamicFieldsFormBuilder = editDynamicFieldsFormBuilderControl.createMock()

        controller.params.list = '10'
        def result = controller.create()

        subscriberServiceControl.verify()
        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()
        editDynamicFieldsFormBuilderControl.verify()

        assertNotNull result
        assertEquals '10', result.listId
    }

    void testAdd_Success() {
        def member = new Member(id: 1)

        Gender.class.metaClass.static.load = { id -> return new Gender(id: id) }
        Language.class.metaClass.static.load = { id -> return new Language(id: id) }
        Timezone.class.metaClass.static.load = { id -> return new Timezone(id: id) }
        NamePrefix.class.metaClass.static.load = { id -> return new NamePrefix(id: id) }
        SubscriberType.class.metaClass.static.load = { id -> return new SubscriberType(id: id) }
        Member.class.metaClass.static.load = { id -> return member}

        def values = new DynamicFieldValues([], [])
        def subscriberServiceControl = mockFor(SubscriberService)
        def Subscriber subscriber = null
        subscriberServiceControl.demand.getSubscriberDynamicFields { subscr -> return values}
        subscriberServiceControl.demand.saveSubscriber { subscr, _values ->
            subscriber = subscr;
            assertEquals values, _values
            return true
        }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.firstName = 'Test'
        controller.params.lastName = 'Subscriber'
        controller.params.email = 'test@mailsight.com'
        controller.params.gender = '2'
        controller.params.timezone = '3'
        controller.params.language = '4'
        controller.params.namePrefix = '5'
        controller.params.subscriberType = '6'

        controller.add()

        subscriberServiceControl.verify()
        springSecurityServiceControl.verify()

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
        assertEquals 6, subscriber.subscriberType.id
    }

    void testAdd_WithSubscription() {
        def member = new Member(id: 1)

        Gender.class.metaClass.static.load = { id -> return new Gender(id: id) }
        Language.class.metaClass.static.load = { id -> return new Language(id: id) }
        Timezone.class.metaClass.static.load = { id -> return new Timezone(id: id) }
        NamePrefix.class.metaClass.static.load = { id -> return new NamePrefix(id: id) }
        SubscriberType.class.metaClass.static.load = { id -> return new SubscriberType(id: id) }
        Member.class.metaClass.static.load = { id -> return member}
        SubscriptionStatus.class.metaClass.static.get = { id -> return new SubscriptionStatus(id: id)}

        def Subscriber subscriber = null
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriberDynamicFields { subscr -> new DynamicFieldValues([], []) }
        subscriberServiceControl.demand.saveSubscriber { subscr, values ->
            subscriber = subscr;
            subscriber.id = '123';
            return true
        }

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            assertEquals 22, id
            return new SubscriptionList(id: id, owner: member)
        }
        subscriptionListServiceControl.demand.addSubscription { subscription ->
            assertEquals '123', subscription.subscriber.id
            assertEquals 22, subscription.subscriptionList.id
            return true
        }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.subscriptionListService = subscriptionListServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.email = 'test@mailsight.com'
        controller.params.listId = 22

        controller.class.metaClass.createLink = { '/outbox/list/22' }

        controller.add()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Success is expected.', result.success
        assertEquals '/outbox/list/22', result.redirectTo
        assertNull 'Error is unexpected.', result.error

        assertNotNull subscriber

        subscriberServiceControl.verify()
        subscriptionListServiceControl.verify()
    }

    void testAdd_WithFailedSubscription() {
        def member = new Member(id: 1)

        Gender.class.metaClass.static.load = { id -> return new Gender(id: id) }
        Language.class.metaClass.static.load = { id -> return new Language(id: id) }
        Timezone.class.metaClass.static.load = { id -> return new Timezone(id: id) }
        NamePrefix.class.metaClass.static.load = { id -> return new NamePrefix(id: id) }
        SubscriberType.class.metaClass.static.load = { id -> return new SubscriberType(id: id) }
        Member.class.metaClass.static.load = { id -> return member}

        def Subscriber subscriber = null
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriberDynamicFields { subscr -> new DynamicFieldValues([], [])}
        subscriberServiceControl.demand.saveSubscriber { subscr, values ->
            subscriber = subscr;
            subscriber.id = 123;
            return true
        }

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            assertEquals 22, id
            return new SubscriptionList(id: id)
        }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }

        controller.subscriberService = subscriberServiceControl.createMock()
        controller.subscriptionListService = subscriptionListServiceControl.createMock()
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.email = 'test@mailsight.com'
        controller.params.listId = 22

        controller.add()
        assertNotNull mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue 'Success is expected.', result.success
        assertNull 'Error is unexpected.', result.error

        assertNotNull subscriber

        subscriberServiceControl.verify()
        subscriptionListServiceControl.verify()
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
        subscriberServiceControl.demand.getSubscriberDynamicFields { subscr -> new DynamicFieldValues([], []) }
        subscriberServiceControl.demand.saveSubscriber { subscr, values -> return false }

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }

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

        def dynamicFieldValues = new DynamicFieldValues([], [])
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        subscriberServiceControl.demand.getSubscriberTypes { m -> [new SubscriberType(id: 1, member: m)]}
        subscriberServiceControl.demand.getSubscriberDynamicFields { s ->
            assertEquals subscriber, s
            return dynamicFieldValues
        }
        controller.subscriberService = subscriberServiceControl.createMock()

        def elements = new UIContainer()
        def editDynamicFieldsFormBuilderControl = mockFor(EditDynamicFieldsFormBuilder)
        editDynamicFieldsFormBuilderControl.demand.build { f ->
            assertEquals dynamicFieldValues, f
            return elements
        }
        controller.editDynamicFieldsFormBuilder = editDynamicFieldsFormBuilderControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '0000000'

        def result = controller.edit()

        subscriberServiceControl.verify()
        springSecurityServiceControl.verify()
        editDynamicFieldsFormBuilderControl.verify()

        assertNotNull result
        assertEquals 'Subscriber is not found.', subscriber, result.subscriber
        assertNotNull result.subscriberTypes
        assertEquals 1, result.subscriberTypes.size()
        assertEquals 1, result.subscriberTypes[0].id
        assertEquals member, result.subscriberTypes[0].member
        assertEquals elements, result.dynamicFieldsForm
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
        SubscriberType.class.metaClass.static.load = { id -> return new SubscriberType(id: id) }
        Member.class.metaClass.static.load = { id -> return member}

        def values = new DynamicFieldValues([], [])
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getSubscriber { id -> return subscriber}
        subscriberServiceControl.demand.getSubscriberDynamicFields { subscr -> values }
        subscriberServiceControl.demand.saveSubscriber { subscr, _values->
            assertEquals values, _values
            return true
        }
        controller.subscriberService = subscriberServiceControl.createMock()

        controller.params.email = 'test2@mailsight.com'
        controller.params.firstName = 'First'
        controller.params.lastName = 'Last'
        controller.params.gender = '2'
        controller.params.timezone = '3'
        controller.params.language = '4'
        controller.params.namePrefix = '5'
        controller.params.subscriberType = '6'

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
        assertEquals 6, subscriber.subscriberType.id
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
        subscriberServiceControl.demand.getSubscriberDynamicFields { subscr -> new DynamicFieldValues([], []) }
        subscriberServiceControl.demand.saveSubscriber { subscr, values -> return true}
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
        subscriberServiceControl.demand.getSubscriberDynamicFields { subscr -> new DynamicFieldValues([], []) }
        subscriberServiceControl.demand.saveSubscriber { subscr, values -> return true}
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
        subscriberServiceControl.demand.getSubscriberDynamicFields { subscr -> new DynamicFieldValues([], []) }
        subscriberServiceControl.demand.saveSubscriber { subscr, values -> return false}
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
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }

        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.types()

        assertNotNull result
        assertNotNull result.subscriberTypes
        assertEquals 2, result.subscriberTypes.size()
    }

    void testAddSubscriberType() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member }

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.addSubscriberType { type -> type.id = 12; return true}
        controller.subscriberService = subscriberServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Software Developer'
        controller.addSubscriberType()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue result.success
        assertEquals 'Software Developer', result.subscriberType.name
        assertEquals 12, result.subscriberType.id
    }

    void testAddSubscriberType_Fail() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member }

        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.addSubscriberType { type -> return false}
        controller.subscriberService = subscriberServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        mockDomain(SubscriberType)

        controller.params.name = 'Software Developer'
        controller.addSubscriberType()

        println mockResponse.contentAsString

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue result.error
    }

    void testUpdateSubscriberType() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member }

        mockDomain(SubscriberType)

        def subscriberType = new SubscriberType(id: 2, name: 'Tester', member: member)
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getMemberSubscriberType { memberId, subscriberTypeId ->
            assertEquals subscriberType.id, subscriberTypeId;
            assertEquals member.id, memberId;
            return subscriberType
        }
        subscriberServiceControl.demand.saveSubscriberType { type -> return true}
        controller.subscriberService = subscriberServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 2
        controller.params.name = 'Software Developer'
        controller.updateSubscriberType()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue result.success
        assertEquals 'Software Developer', result.subscriberType.name
        assertEquals 2, result.subscriberType.id
    }

    void testUpdateSubscriberType_Fail() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member }

        mockDomain(SubscriberType)

        def subscriberType = new SubscriberType(id: 2, name: 'Tester', member: member)
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getMemberSubscriberType { memberId, subscriberTypeId ->
            assertEquals subscriberType.id, subscriberTypeId;
            assertEquals member.id, memberId;
            return subscriberType
        }
        subscriberServiceControl.demand.saveSubscriberType { type -> return false}
        controller.subscriberService = subscriberServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 2
        controller.params.name = 'Software Developer'
        controller.updateSubscriberType()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue result.error
    }

    void testDeleteSubscriberType() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member }

        def subscriberType = new SubscriberType(id: 2, name: 'Tester', member: member)
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getMemberSubscriberType { memberId, subscriberTypeId ->
            assertEquals subscriberType.id, subscriberTypeId;
            assertEquals member.id, memberId;
            return subscriberType
        }
        subscriberServiceControl.demand.deleteSubscriberType { type -> return true}
        controller.subscriberService = subscriberServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 2
        controller.deleteSubscriberType()
        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue result.success
    }

    void testDeleteSubscriberType_Fail() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> return member }

        def subscriberType = new SubscriberType(id: 2, name: 'Tester', member: member)
        def subscriberServiceControl = mockFor(SubscriberService)
        subscriberServiceControl.demand.getMemberSubscriberType { memberId, subscriberTypeId ->
            assertEquals subscriberType.id, subscriberTypeId;
            assertEquals member.id, memberId;
            return null
        }
        controller.subscriberService = subscriberServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 2
        controller.deleteSubscriberType()
        def result = JSON.parse(mockResponse.contentAsString)
        assertTrue result.error
    }
}
