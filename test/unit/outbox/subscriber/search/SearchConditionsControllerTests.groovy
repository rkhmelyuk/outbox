package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicField
import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.ValueCondition
import outbox.subscriber.search.condition.ValueConditionType
import outbox.subscription.SubscriptionListService

/**
 * @author Ruslan Khmelyuk
 */
class SearchConditionsControllerTests extends ControllerUnitTestCase {

    @Override protected void setUp() {
        super.setUp();
        controller.class.metaClass.message = { 'message' }
    }

    void testRenderConditions() {

        def conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('field', ValueCondition.equal('test')))
        mockRequest.conditions = conditions
        controller.renderConditions()

        assertEquals 'subscriberCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.comparisons
        assertNotNull controller.renderArgs.model.fields
        assertEquals ConditionType.Subscriber.id, controller.renderArgs.model.type
    }

    void testRenderConditionsNull() {
        controller.renderConditions()
        assertEquals '', mockResponse.contentAsString
    }

    void testRenderRow_First() {
        controller.params.row = 1
        controller.renderRow()

        assertEquals 'subscriberCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.fields
        assertNotNull controller.renderArgs.model.comparisons
        assertEquals ConditionType.Subscriber.id, controller.renderArgs.model.type
    }

    void testRenderRow_Subscriber() {
        controller.params.row = 1
        controller.params.type = 1

        controller.renderRow()

        assertEquals 'subscriberCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.comparisons
        assertNotNull controller.renderArgs.model.fields
        assertEquals ConditionType.Subscriber.id, controller.renderArgs.model.type
    }

    void testRenderRow_DynamicFields() {
        Member.class.metaClass.static.load = { id -> new Member(id: 1) }

        def dynamicFields = [new DynamicField(id: 1)]
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

        controller.params.row = 1
        controller.params.type = 2
        controller.params.value = '1'
        controller.renderRow()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'dynamicFieldCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertEquals dynamicFields, controller.renderArgs.model.dynamicFields
        assertNotNull controller.renderArgs.model.comparisons
        assertEquals '1', controller.renderArgs.model.value
        assertEquals ConditionType.DynamicField.id, controller.renderArgs.model.type
    }

    void testRenderRow_DynamicFieldsEmpty() {
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

        controller.params.row = 1
        controller.params.type = 2
        controller.renderRow()

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertEquals 'dynamicFieldCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNull controller.renderArgs.model.dynamicFields
        assertNull controller.renderArgs.model.comparisons
        assertNull controller.renderArgs.model.values
        assertNull controller.renderArgs.model.value
        assertEquals ConditionType.DynamicField.id, controller.renderArgs.model.type
    }

    void testRenderRow_Subscription() {
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

        controller.params.row = 1
        controller.params.type = 3
        controller.params.subscriptionList = 2
        controller.renderRow()

        springSecurityServiceControl.verify()
        subscriptionListServiceControl.verify()

        assertEquals 'subscriptionCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.comparisons
        assertEquals subscriptionLists, controller.renderArgs.model.subscriptionLists
        assertEquals 2, controller.renderArgs.model.subscriptionList
        assertEquals ConditionType.Subscription.id, controller.renderArgs.model.type
    }

    void testShowValue() {
        assertFalse controller.showValue(null)
        assertFalse controller.showValue(ValueConditionType.Empty.id)
        assertFalse controller.showValue(ValueConditionType.Filled.id)
        assertTrue controller.showValue(ValueConditionType.Equal.id)
    }
}
