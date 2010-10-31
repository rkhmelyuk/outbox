package outbox.subscriber

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.field.DynamicFieldType

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldControllerTests extends ControllerUnitTestCase {

    @Override protected void setUp() {
        super.setUp();
        controller.class.metaClass.createLink = { 'link' }
    }

    void testIndex() {
        def member = new Member(id: 10)
        def dynamicFields = []

        Member.class.metaClass.static.load = { id -> member }

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicFields { _member ->
            assertEquals member.id, _member.id
            return dynamicFields
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.index()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNotNull result
        assertEquals dynamicFields, result.dynamicFields
    }

    void testDynamicFields() {
        def member = new Member(id: 10)
        def dynamicFields = []

        Member.class.metaClass.static.load = { id -> member }

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicFields { _member ->
            assertEquals member.id, _member.id
            return dynamicFields
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.dynamicFields()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'dynamicFields', controller.renderArgs.template
        assertEquals dynamicFields, controller.renderArgs.model.dynamicFields
    }

    void testCreate() {
        def result = controller.create()
        assertNotNull result.dynamicField
    }

    void testEdit() {
        def dynamicField = new DynamicField(id: 1, owner: new Member(id: 1))
        def dynamicFieldItems = []

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return dynamicField
        }
        dynamicFieldServiceControl.demand.getDynamicFieldItems { field ->
            assertEquals dynamicField, field
            return dynamicFieldItems
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '1'
        def result = controller.edit()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals dynamicField, result.dynamicField
        assertEquals dynamicFieldItems, result.dynamicFieldItems
    }

    void testEdit_Denied() {
        def dynamicField = new DynamicField(id: 1, owner: new Member(id: 2))

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return dynamicField
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '1'
        controller.edit()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 404, mockResponse.status
    }

    void testAdd_String() {
        def member = new Member(id: 10)

        Member.class.metaClass.static.load = { id -> member }

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.addDynamicField { field ->
            assertEquals member, field.owner
            assertEquals 'Label', field.label
            assertEquals 'label', field.name
            assertEquals DynamicFieldType.String, field.type
            assertEquals 20, field.maxlength
            assertNull field.max
            assertNull field.min
            assertTrue field.mandatory
            return true
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.label = 'Label'
        controller.params.name = 'label'
        controller.params.type = '1'
        controller.params.maxlength = '20'
        controller.params.min = '0'
        controller.params.max = '10'
        controller.params.mandatory = 'true'

        controller.add()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertEquals 'link', result.dynamicFieldsLink
        assertNull result.error
    }

    void testAdd_Number() {
        def member = new Member(id: 10)

        Member.class.metaClass.static.load = { id -> member }

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.addDynamicField { field ->
            assertEquals member, field.owner
            assertEquals 'Label', field.label
            assertEquals 'label', field.name
            assertEquals DynamicFieldType.Number, field.type
            assertEquals 10, field.max
            assertEquals 0, field.min
            assertNull field.maxlength
            assertTrue field.mandatory
            return true
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.label = 'Label'
        controller.params.name = 'label'
        controller.params.type = '2'
        controller.params.maxlength = '20'
        controller.params.min = '0'
        controller.params.max = '10'
        controller.params.mandatory = 'true'

        controller.add()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertEquals 'link', result.dynamicFieldsLink
        assertNull result.error
    }

    void testAdd_Boolean() {
        def member = new Member(id: 10)

        Member.class.metaClass.static.load = { id -> member }

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.addDynamicField { field ->
            assertEquals member, field.owner
            assertEquals 'Label', field.label
            assertEquals 'label', field.name
            assertEquals DynamicFieldType.Boolean, field.type
            assertNull field.max
            assertNull field.min
            assertNull field.maxlength
            assertTrue field.mandatory
            return true
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.label = 'Label'
        controller.params.name = 'label'
        controller.params.type = '3'
        controller.params.maxlength = '20'
        controller.params.min = '0'
        controller.params.max = '10'
        controller.params.mandatory = 'true'

        controller.add()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertEquals 'link', result.dynamicFieldsLink
        assertNull result.error
    }

    void testAdd_SingleSelect() {
        def member = new Member(id: 10)

        Member.class.metaClass.static.load = { id -> member }

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.addDynamicField { field ->
            assertEquals member, field.owner
            assertEquals 'Label', field.label
            assertEquals 'label', field.name
            assertEquals DynamicFieldType.SingleSelect, field.type
            assertNull field.max
            assertNull field.min
            assertNull field.maxlength
            assertTrue field.mandatory

            field.id = 123
            return true
        }
        dynamicFieldServiceControl.demand.addDynamicFieldItems { field, items ->
            assertEquals 123, field.id
            items.each { assertTrue(['hello', 'world', 'how', 'are', 'you'].contains(it.name)) }
            return true
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.label = 'Label'
        controller.params.name = 'label'
        controller.params.type = '4'
        controller.params.maxlength = '20'
        controller.params.min = '0'
        controller.params.max = '10'
        controller.params.mandatory = 'true'
        controller.params.selectValue = ['hello', 'world', 'how', 'are', 'you']

        controller.add()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertEquals 'link', result.dynamicFieldsLink
        assertNull result.error
    }

    void testUpdate_SingleSelect() {
        def member = new Member(id: 10)

        Member.class.metaClass.static.load = { id -> member }

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            new DynamicField(id: 1, owner: member)
        }
        dynamicFieldServiceControl.demand.saveDynamicField { field ->
            assertEquals member, field.owner
            assertEquals 'Label', field.label
            assertEquals 'label', field.name
            assertEquals DynamicFieldType.SingleSelect, field.type
            assertNull field.max
            assertNull field.min
            assertNull field.maxlength
            assertTrue field.mandatory
            return true
        }
        dynamicFieldServiceControl.demand.getDynamicFieldItems { field ->
            assertEquals 1, field.id
            [
                    new DynamicFieldItem(id: 1, name: 'hello'),
                    new DynamicFieldItem(id: 2, name: 'how'),
                    new DynamicFieldItem(id: 3, name: 'u'),
            ]

        }
        dynamicFieldServiceControl.demand.updateDynamicFieldItems { field, items ->
            assertEquals 1, field.id
            items.each {
                if (it.name == 'hello') {
                    assertEquals 1, it.id
                }
                else if (it.name == 'how') {
                    assertEquals 2, it.id
                }
                else if (it.name == 'world' || it.name == 'are' || it.name == 'you') {
                    assertNull it.id
                }
                else {
                    fail "Unexpected item $it.name"
                }
            }
            return true
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = '1'
        controller.params.label = 'Label'
        controller.params.name = 'label'
        controller.params.type = '4'
        controller.params.maxlength = '20'
        controller.params.min = '0'
        controller.params.max = '10'
        controller.params.mandatory = 'true'
        controller.params.selectValue = ['hello', 'world', 'how', 'are', 'you']

        controller.update()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        def result = JSON.parse(mockResponse.contentAsString)

        assertTrue 'Must be successful.', result.success
        assertEquals 'link', result.dynamicFieldsLink
        assertNull result.error
    }

}
