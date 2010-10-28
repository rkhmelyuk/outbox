package outbox.subscriber

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldControllerTests extends ControllerUnitTestCase {

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
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], member)
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        def result = controller.index()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNotNull result
        assertEquals dynamicFields, result.dynamicFields

    }
}
