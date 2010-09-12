package outbox.subscription

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser

/**
 * {@link SubscriptionListController} tests.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriptionListControllerTests extends ControllerUnitTestCase {

    void testList() {
        def member = new Member(id: 10)
        def subscriptionLists = null

        Member.class.metaClass.static.load = { id -> member }

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getMemberSubscriptionList { it ->
            assertEquals member.id, it.id;
            return subscriptionLists
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.list()
        
        assertNotNull result
        assertEquals subscriptionLists, result.subscriptionLists
    }

    void testCreate() {
        def result = controller.create()
        assertNotNull result
        assertNotNull result.subscriptionList
        assertNull result.subscriptionList.id
    }

    void testAdd_Success() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return true 
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.add()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertNull result.error
    }

    void testAdd_Fail() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        mockDomain(SubscriptionList)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return false
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.add()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNotNull result.errors
    }

    void testEdit() {
        def member = new Member(id: 1)
        
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()

        assertNotNull result.subscriptionList
        assertEquals 10, result.subscriptionList.id
    }

    void testEdit_Denied() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            new SubscriptionList(id: id, owner: new Member(id: 1))
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

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
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> null }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()

        assertNull result
        assertEquals 404, mockResponse.status
    }

    void testUpdate_Success() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member)}
        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 10, it.id
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return true
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertNull result.error
    }

    void testUpdate_Fail() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscriptionList)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member)}
        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 10, it.id
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return false
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNotNull result.errors
    }

    void testUpdate_NotFound() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscriptionList)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> null}
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.errors
    }

    void testUpdate_Denied() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        mockDomain(SubscriptionList)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            new SubscriptionList(id: id, owner: new Member(id: 2))}

        subscriptionListServiceControl.demand.saveSubscriptionList {
            assertEquals 10, it.id
            assertEquals 'Subscription List Name', it.name
            assertEquals 'Subscription List Description', it.description
            return false
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 10
        controller.params.name = 'Subscription List Name'
        controller.params.description = 'Subscription List Description'

        controller.update()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.errors
    }

    void testShow() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.show()

        assertNotNull result.subscriptionList
        assertEquals 10, result.subscriptionList.id
    }

    void testShow_Denied() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            new SubscriptionList(id: id, owner: new Member(id: 1))
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 2))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.show()

        assertNull result
        assertEquals 403, mockResponse.status
    }

    void testShow_NotFound() {
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> null }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.show()

        assertNull result
        assertEquals 404, mockResponse.status
    }

    void testDelete() {
        def member = new Member(id: 1)

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id -> new SubscriptionList(id: id, owner: member) }
        subscriptionListServiceControl.demand.deleteSubscriptionList { }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.delete()

        assertEquals 'subscriptionList', controller.redirectArgs.controller
        assertEquals '', controller.redirectArgs.action
    }

}
