package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser
import outbox.subscriber.DynamicFieldService
import outbox.subscription.SubscriptionListService

/**
 * @author Ruslan Khmelyuk
 */
class SearchConditionsControllerTests extends ControllerUnitTestCase {

    @Override protected void setUp() {
        super.setUp();
        controller.class.metaClass.message = { 'message' }
    }

    void testAddRow_First() {
        controller.addRow()

        assertEquals 'subscriberCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.fields
        assertNotNull controller.renderArgs.model.comparisons
        assertEquals ConditionType.Subscriber.id, controller.renderArgs.model.type
    }

    void testAddRow_Subscriber() {
        controller.params.id = 1

        controller.addRow()

        assertEquals 'subscriberCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.comparisons
        assertNotNull controller.renderArgs.model.fields
        assertEquals ConditionType.Subscriber.id, controller.renderArgs.model.type
    }

    void testAddRow_DynamicField() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1) }

        def dynamicFields = []
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicFields { _member ->
            return dynamicFields
        }
        controller.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 2
        controller.addRow()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'dynamicFieldCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.comparisons
        assertNotNull controller.renderArgs.model.dynamicFields
        assertEquals ConditionType.DynamicField.id, controller.renderArgs.model.type
    }

    void testAddRow_SubscriptionField() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1) }

        def subscriptionLists = []
        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.search { _conditions ->
            return subscriptionLists
        }
        controller.subscriptionListService = subscriptionListServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.params.id = 3
        controller.addRow()

        springSecurityServiceControl.verify()
        subscriptionListServiceControl.verify()

        assertEquals 'subscriptionCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNull controller.renderArgs.model.comparisons
        assertNotNull controller.renderArgs.model.subscriptionLists
        assertEquals ConditionType.Subscription.id, controller.renderArgs.model.type
    }
}
