package outbox.subscriber

import grails.converters.JSON
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

    void testCreate() {
        def result = controller.create()
        assertNotNull result
        assertNotNull result.subscribersList
        assertNull result.subscribersList.id
    }

    void testAdd_Success() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.saveSubscribersList {
            assertEquals 'Subscribers List Name', it.name
            assertEquals 'Subscribers List Description', it.description
            return true 
        }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Subscribers List Name'
        controller.params.description = 'Subscribers List Description'

        controller.add()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertNull result.error
    }

    void testAdd_Fail() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        mockDomain(SubscribersList)

        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.saveSubscribersList {
            assertEquals 'Subscribers List Name', it.name
            assertEquals 'Subscribers List Description', it.description
            return false
        }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Subscribers List Name'
        controller.params.description = 'Subscribers List Description'

        controller.add()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNotNull result.errors
    }

    void testEdit() {
        def member = new Member(id: 1)
        
        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.getSubscribersList { id -> new SubscribersList(id: id, owner: member) }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()

        assertNotNull result.subscribersList
        assertEquals 10, result.subscribersList.id
    }

    void testEdit_Denied() {
        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.getSubscribersList { id ->
            new SubscribersList(id: id, owner: new Member(id: 1))
        }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 2))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()

        assertNull result
        assertEquals 403, mockResponse.status
    }

    void testEdit_NotFound() {
        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.getSubscribersList { id -> null }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()

        assertNull result
        assertEquals 404, mockResponse.status
    }

    void testUpdate_Success() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.getSubscribersList { id -> new SubscribersList(id: id, owner: member)}
        subscribersListServiceControl.demand.saveSubscribersList {
            assertEquals 10, it.id
            assertEquals 'Subscribers List Name', it.name
            assertEquals 'Subscribers List Description', it.description
            return true
        }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscribers List Name'
        controller.params.description = 'Subscribers List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertNull result.error
    }

    void testUpdate_Fail() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscribersList)

        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.getSubscribersList { id -> new SubscribersList(id: id, owner: member)}
        subscribersListServiceControl.demand.saveSubscribersList {
            assertEquals 10, it.id
            assertEquals 'Subscribers List Name', it.name
            assertEquals 'Subscribers List Description', it.description
            return false
        }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscribers List Name'
        controller.params.description = 'Subscribers List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNotNull result.errors
    }

    void testUpdate_NotFound() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscribersList)

        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.getSubscribersList { id -> null}
        controller.subscribersListService = subscribersListServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscribers List Name'
        controller.params.description = 'Subscribers List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.errors
    }

    void testUpdate_Denied() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscribersList)

        def subscribersListServiceControl = mockFor(SubscribersListService)
        subscribersListServiceControl.demand.getSubscribersList { id ->
            new SubscribersList(id: id, owner: new Member(id: 2))}

        subscribersListServiceControl.demand.saveSubscribersList {
            assertEquals 10, it.id
            assertEquals 'Subscribers List Name', it.name
            assertEquals 'Subscribers List Description', it.description
            return false
        }
        controller.subscribersListService = subscribersListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscribers List Name'
        controller.params.description = 'Subscribers List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.errors
    }

}
