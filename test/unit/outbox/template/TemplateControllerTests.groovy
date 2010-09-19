package outbox.template

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser

/**
 * @author Ruslan Khmelyuk
 */
class TemplateControllerTests extends ControllerUnitTestCase {

    @Override protected void setUp() {
        super.setUp();
        controller.class.metaClass.createLink = { 'link' }
    }

    void testList() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }

        mockDomain(Template)

        def templates = []
        (1..TemplateController.ITEMS_PER_PAGE).each {
            templates << new Template(id: it)
        }

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getMemberTemplates {m, page, max ->
            assertEquals member, m
            assertEquals 1, page
            assertEquals TemplateController.ITEMS_PER_PAGE, max
            return templates 
        }
        controller.templateService = templateServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.list()

        templateServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNotNull result.templates
        assertEquals templates.size(), result.templates.size()
        assertEquals 2, result.nextPage
    }

    void testTemplatesPage() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member }

        mockDomain(Template)

        def templates = []
        (1..TemplateController.ITEMS_PER_PAGE).each {
            templates << new Template(id: it)
        }

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getMemberTemplates {m, page, max ->
            assertEquals member, m
            assertEquals 2, page
            assertEquals TemplateController.ITEMS_PER_PAGE, max
            return templates
        }
        controller.templateService = templateServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.page = 2

        controller.templatesPage()

        templateServiceControl.verify()
        springSecurityServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertEquals 'link', result.nextPage
        assertNotNull result.content
    }

    void testCreate() {
        def result = controller.create()
        assertNotNull result.template
        assertNull result.template.id
    }

    void testEdit() {
        def member = new Member(id: 1)
        def template = new Template(id: 10, name: 'Name', owner: member)

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { id ->
            assertEquals 10, id
            return template
        }
        controller.templateService = templateServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.edit()
        assertEquals template, result.template

        springSecurityServiceControl.verify()
        templateServiceControl.verify()
    }

    void testEdit_NotFound() {
        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { null }
        controller.templateService = templateServiceControl.createMock()

        controller.params.id = '10'
        controller.edit()

        assertEquals 404, mockResponse.status

        templateServiceControl.verify()
    }

    void testEdit_Denied() {
        def member = new Member(id: 1)
        def template = new Template(id: 10, name: 'Name', owner: null)

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { template }
        controller.templateService = templateServiceControl.createMock()

        controller.params.id = '10'
        controller.edit()
        assertEquals 403, mockResponse.status

        springSecurityServiceControl.verify()
        templateServiceControl.verify()
    }

    void testShow() {
        def member = new Member(id: 1)
        def template = new Template(id: 10, name: 'Name', owner: member)

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { id ->
            assertEquals 10, id
            return template
        }
        controller.templateService = templateServiceControl.createMock()

        controller.params.id = '10'
        def result = controller.show()
        assertEquals template, result.template

        springSecurityServiceControl.verify()
        templateServiceControl.verify()
    }

    void testShow_NotFound() {
        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { null }
        controller.templateService = templateServiceControl.createMock()

        controller.params.id = '10'
        controller.show()

        assertEquals 404, mockResponse.status

        templateServiceControl.verify()
    }

    void testShow_Denied() {
        def member = new Member(id: 1)
        def template = new Template(id: 10, name: 'Name', owner: null)

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { template }
        controller.templateService = templateServiceControl.createMock()

        controller.params.id = '10'
        controller.show()
        assertEquals 403, mockResponse.status

        springSecurityServiceControl.verify()
        templateServiceControl.verify()
    }

    void testAdd_Success() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.addTemplate {
            assertEquals 'Template Name', it.name
            assertEquals 'Template Description', it.description
            assertEquals 'Template Body', it.templateBody
            return true
        }
        controller.templateService = templateServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Template Name'
        controller.params.description = 'Template Description'
        controller.params.templateBody = 'Template Body'

        controller.add()

        springSecurityServiceControl.verify()
        templateServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertEquals 'link', result.redirectTo
        assertNull result.error
    }

    void testAdd_Fail() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        mockDomain(Template)

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.addTemplate {
            assertEquals 'Template Name', it.name
            assertEquals 'Template Description', it.description
            assertEquals 'Template Body', it.templateBody
            return false
        }
        controller.templateService = templateServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.name = 'Template Name'
        controller.params.description = 'Template Description'
        controller.params.templateBody = 'Template Body'

        controller.add()

        springSecurityServiceControl.verify()
        templateServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.redirectTo
        assertNotNull result.errors
    }

    void testUpdate_Success() {
        def member = new Member(id: 1)
        Member.class.metaClass.static.load = { id -> member}

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { id -> return new Template(id: id, owner: member) }
        templateServiceControl.demand.saveTemplate {
            assertEquals 1, it.id
            assertEquals 'Template Name', it.name
            assertEquals 'Template Description', it.description
            assertEquals 'Template Body', it.templateBody
            return true
        }
        controller.templateService = templateServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '1'
        controller.params.name = 'Template Name'
        controller.params.description = 'Template Description'
        controller.params.templateBody = 'Template Body'

        controller.update()

        springSecurityServiceControl.verify()
        templateServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertNull result.error
    }

    void testUpdate_Fail() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        mockDomain(Template)

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { id -> return null }
        controller.templateService = templateServiceControl.createMock()

        controller.params.id = 1
        controller.params.name = 'Template Name'
        controller.params.description = 'Template Description'
        controller.params.templateBody = 'Template Body'

        controller.update()

        templateServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.errors
    }

    void testUpdate_Denied() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1)}

        mockDomain(Template)

        def templateServiceControl = mockFor(TemplateService)
        templateServiceControl.demand.getTemplate { id -> return new Template(id: id) }
        controller.templateService = templateServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 1
        controller.params.name = 'Template Name'
        controller.params.description = 'Template Description'
        controller.params.templateBody = 'Template Body'

        controller.update()

        springSecurityServiceControl.verify()
        templateServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be error.', result.error
        assertNull result.success
        assertNull result.errors
    }
}
