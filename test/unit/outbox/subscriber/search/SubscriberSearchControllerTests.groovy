package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.ControllerUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicField
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService
import outbox.subscriber.search.condition.*

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberSearchControllerTests extends ControllerUnitTestCase {

    @Override protected void setUp() {
        super.setUp();
        controller.class.metaClass.message = { 'message' }
    }

    void testRenderConditions_SubscriberCondition() {

        def conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('field', ValueCondition.equal('test')))
        mockRequest.conditions = conditions
        controller.renderConditions()

        assertEquals 'subscriberCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.comparisons
        assertNotNull controller.renderArgs.model.fields
        assertNull controller.renderArgs.model.subscriptionLists
        assertEquals ConditionType.Subscriber.id, controller.renderArgs.model.type
    }

    void testRenderConditions_DynamicFieldCondition() {
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

        def conditions = new Conditions()
        conditions.and(new DynamicFieldCondition(new DynamicField(id:1), ValueCondition.empty()))
        mockRequest.conditions = conditions
        controller.renderConditions()

        assertEquals 'dynamicFieldCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.comparison
        assertNotNull controller.renderArgs.model.comparisons
        assertEquals dynamicFields, controller.renderArgs.model.dynamicFields
        assertNull controller.renderArgs.model.subscriptionLists
        assertEquals ConditionType.DynamicField.id, controller.renderArgs.model.type
    }


    void testRenderConditions_SubscriptionCondition() {
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

        def conditions = new Conditions()
        def subscriptionList = new SubscriptionList(id: 10)
        conditions.and(SubscriptionCondition.subscribed(subscriptionList))
        mockRequest.conditions = conditions
        controller.renderConditions()

        assertEquals 'subscriptionCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.subscriptionType
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.subscriptionTypes
        assertEquals subscriptionLists, controller.renderArgs.model.subscriptionLists
        assertNull controller.renderArgs.model.fields
        assertEquals ConditionType.Subscription.id, controller.renderArgs.model.type
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
        controller.params.subscriptionType = 4
        controller.params.subscriptionList = 2
        controller.renderRow()

        springSecurityServiceControl.verify()
        subscriptionListServiceControl.verify()

        assertEquals 'subscriptionCondition', controller.renderArgs.template
        assertEquals 1, controller.renderArgs.model.row
        assertNotNull controller.renderArgs.model.types
        assertNotNull controller.renderArgs.model.subscriptionType
        assertNotNull controller.renderArgs.model.subscriptionTypes
        assertEquals subscriptionLists, controller.renderArgs.model.subscriptionLists
        assertEquals 2, controller.renderArgs.model.subscriptionList
        assertEquals ConditionType.Subscription.id, controller.renderArgs.model.type
    }

    void testShowValue() {
        assertFalse controller.showValue(null)
        assertFalse controller.showValue(ValueConditionType.Empty.id)
        assertFalse controller.showValue(ValueConditionType.Filled.id)
        assertTrue controller.showValue(ValueConditionType.Equal.id)
        assertTrue controller.showValue(ValueConditionType.NotEqual.id)
    }

    void testSearchResults() {
        def subscribers = new Subscribers()
        def subscriberSearchServiceControl = mockFor(SubscriberSearchService)
        subscriberSearchServiceControl.demand.search { Conditions conditions ->
            assertFalse conditions.empty
            return subscribers
        }
        subscriberSearchServiceControl.demand.describe { Conditions conditions ->
            assertFalse conditions.empty
            return 'description'
        }
        controller.subscriberSearchService = subscriberSearchServiceControl.createMock()
        controller.searchConditionsFetcher = new SearchConditionsFetcher()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal { ->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        controller.springSecurityService = springSecurityServiceControl.createMock()

        controller.searchResults()

        subscriberSearchServiceControl.verify()

        assertNotNull controller.renderArgs.model
        assertEquals subscribers, controller.renderArgs.model.subscribers
        assertEquals 'description', controller.renderArgs.model.readableConditions
        assertEquals 'searchResult', controller.renderArgs.template
    }
}
