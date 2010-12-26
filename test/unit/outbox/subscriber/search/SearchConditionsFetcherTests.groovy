package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.GrailsUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.search.condition.ValueConditionType
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService

/**
 * @author Ruslan Khmelyuk
 * @created 2010-11-25
 */
class SearchConditionsFetcherTests extends GrailsUnitTestCase {

    def fetcher = new SearchConditionsFetcher()

    void testFetch_Nothing() {
        def conditions = fetcher.fetch([:])

        assertTrue conditions.empty
    }

    void testFetch_Pagination() {
        def conditions = fetcher.fetch([page: '1', perPage: '5'])

        assertTrue conditions.empty
        assertEquals 1, conditions.page
        assertEquals 5, conditions.perPage
    }

    void testFetch_Orders() {
        def conditions = fetcher.fetch([column: 'ColumnName', sort: 'asc'])

        assertTrue conditions.empty
        assertTrue conditions.orders.size() == 1
        assertEquals 'ColumnName', conditions.orders.first().column
        assertEquals Sort.Asc, conditions.orders.first().sort

        conditions = fetcher.fetch([column: 'ColumnName', sort: 'desc'])

        assertTrue conditions.empty
        assertTrue conditions.orders.size() == 1
        assertEquals 'ColumnName', conditions.orders.first().column
        assertEquals Sort.Desc, conditions.orders.first().sort
    }

    void testFetch_MultipleRows() {

    }

    void testSubscriberConditions() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscriber.id"
        params."row[1].field" = "FirstName"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = 'John'

        def condition = fetcher.subscriberCondition(params, '1')

        assertNotNull condition

        assertEquals 'FirstName', condition.field
        assertEquals 'John', condition.value.value
        assertEquals ValueConditionType.Equal, condition.value.type
    }

    void testSubscriberConditions_Trimming() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscriber.id"
        params."row[1].field" = "FirstName"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = '   John  '

        def condition = fetcher.subscriberCondition(params, '1')

        assertNotNull condition

        assertEquals 'FirstName', condition.field
        assertEquals 'John', condition.value.value
        assertEquals ValueConditionType.Equal, condition.value.type
    }

    void testSubscriberConditions_Like() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscriber.id"
        params."row[1].field" = "FirstName"
        params."row[1].comparison" = "$ValueConditionType.Like.id"
        params."row[1].value" = 'John'

        def condition = fetcher.subscriberCondition(params, '1')

        assertNotNull condition

        assertEquals 'FirstName', condition.field
        assertEquals 'John', condition.value.value
        assertEquals ValueConditionType.Like, condition.value.type
    }

    void testSubscriberConditions_NoValue() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscriber.id"
        params."row[1].field" = "FirstName"
        params."row[1].comparison" = "$ValueConditionType.Empty.id"
        params."row[1].value" = 'John'

        def condition = fetcher.subscriberCondition(params, '1')

        assertNotNull condition

        assertNull condition.value.value
        assertEquals 'FirstName', condition.field
        assertEquals ValueConditionType.Empty, condition.value.type
    }


    void testSubscriberConditions_WrongFieldName() {
        def params = [:]
        params.row = '1'
        params."row[1].type" = "$ConditionType.Subscriber.id"
        params."row[1].field" = "WrongField"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = 'John'

        assertNull fetcher.subscriberCondition(params, '1')
    }

    void testDynamicFieldConditions() {
        def params = [:]
        params."row[1].type" = "$ConditionType.DynamicField.id"
        params."row[1].field" = "1"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = 'John'

        def dynamicField = new DynamicField(id: 1, owner: new Member(id: 1))
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return dynamicField
        }
        fetcher.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        fetcher.springSecurityService = springSecurityServiceControl.createMock()

        def condition = fetcher.dynamicFieldCondition(params, '1')

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNotNull condition

        assertEquals dynamicField, condition.field
        assertEquals 'John', condition.value.value
        assertEquals ValueConditionType.Equal, condition.value.type
    }

    void testDynamicFieldConditions_Trimming() {
        def params = [:]
        params."row[1].type" = "$ConditionType.DynamicField.id"
        params."row[1].field" = "1"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = '   John '

        def dynamicField = new DynamicField(id: 1, owner: new Member(id: 1))
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return dynamicField
        }
        fetcher.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        fetcher.springSecurityService = springSecurityServiceControl.createMock()

        def condition = fetcher.dynamicFieldCondition(params, '1')

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNotNull condition

        assertEquals dynamicField, condition.field
        assertEquals 'John', condition.value.value
        assertEquals ValueConditionType.Equal, condition.value.type
    }

    void testDynamicFieldConditions_SingleSelect() {
        def params = [:]
        params."row[1].type" = "$ConditionType.DynamicField.id"
        params."row[1].field" = "1"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = '2'

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        def dynamicField = new DynamicField(id: 1, owner: new Member(id: 1), type: DynamicFieldType.SingleSelect)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return dynamicField
        }
        def dynamicFieldItem = new DynamicFieldItem(field: dynamicField)
        dynamicFieldServiceControl.demand.getDynamicFieldItem { id ->
            assertEquals 2, id
            return dynamicFieldItem
        }
        fetcher.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        fetcher.springSecurityService = springSecurityServiceControl.createMock()

        def condition = fetcher.dynamicFieldCondition(params, '1')

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNotNull condition

        assertEquals dynamicField, condition.field
        assertEquals dynamicFieldItem, condition.value.value
        assertEquals ValueConditionType.Equal, condition.value.type
    }

    void testDynamicFieldConditions_DynamicFieldItemDenied() {
        def params = [:]
        params."row[1].type" = "$ConditionType.DynamicField.id"
        params."row[1].field" = "1"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = '2'

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        def dynamicField = new DynamicField(id: 1, owner: new Member(id: 1), type: DynamicFieldType.SingleSelect)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return dynamicField
        }
        def dynamicFieldItem = new DynamicFieldItem()
        dynamicFieldServiceControl.demand.getDynamicFieldItem { id ->
            assertEquals 2, id
            return dynamicFieldItem
        }
        fetcher.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        fetcher.springSecurityService = springSecurityServiceControl.createMock()

        def condition = fetcher.dynamicFieldCondition(params, '1')

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNull condition
    }

    void testDynamicFieldConditions_DynamicFieldItemNotFound() {
        def params = [:]
        params."row[1].type" = "$ConditionType.DynamicField.id"
        params."row[1].field" = "1"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = '2'

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        def dynamicField = new DynamicField(id: 1, owner: new Member(id: 1), type: DynamicFieldType.SingleSelect)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return dynamicField
        }
        dynamicFieldServiceControl.demand.getDynamicFieldItem { id ->
            assertEquals 2, id
            return null
        }
        fetcher.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        fetcher.springSecurityService = springSecurityServiceControl.createMock()

        def condition = fetcher.dynamicFieldCondition(params, '1')

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNull condition
    }

    void testDynamicFieldConditions_DeniedDynamicField() {
        def params = [:]
        params."row[1].type" = "$ConditionType.DynamicField.id"
        params."row[1].field" = "1"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = 'John'

        def dynamicField = new DynamicField(id: 1, owner: new Member(id: 2))
        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return dynamicField
        }
        fetcher.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        fetcher.springSecurityService = springSecurityServiceControl.createMock()

        def condition = fetcher.dynamicFieldCondition(params, '1')

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNull condition
    }

    void testDynamicFieldConditions_WrongDynamicField() {
        def params = [:]
        params."row[1].type" = "$ConditionType.DynamicField.id"
        params."row[1].field" = "1"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = 'John'

        def dynamicFieldServiceControl = mockFor(DynamicFieldService)
        dynamicFieldServiceControl.demand.getDynamicField { id ->
            assertEquals 1, id
            return null
        }
        fetcher.dynamicFieldService = dynamicFieldServiceControl.createMock()

        def springSecurityServiceControl = mockFor(SpringSecurityService)
        springSecurityServiceControl.demand.getPrincipal {->
            return new OutboxUser('username', 'password', true, false, false, false, [], new Member(id: 1))
        }
        fetcher.springSecurityService = springSecurityServiceControl.createMock()

        def condition = fetcher.dynamicFieldCondition(params, '1')

        dynamicFieldServiceControl.verify()
        springSecurityServiceControl.verify()

        assertNull condition
    }

    void testDynamicFieldConditions_NoDynamicField() {
        def params = [:]
        params."row[1].type" = "$ConditionType.DynamicField.id"
        assertNull fetcher.dynamicFieldCondition(params, '1')
    }

    void testSubscriptionConditions() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscription.id"
        params."row[1].subscriptionType" = '1'
        params."row[1].subscriptionList" = '2'

        def subscriptionListServiceControl = mockFor(SubscriptionListService)
        subscriptionListServiceControl.demand.getSubscriptionList { id ->
            assertEquals 2, id
            return new SubscriptionList(id: id)
        }
        fetcher.subscriptionListService = subscriptionListServiceControl.createMock()

        def condition = fetcher.subscriptionCondition(params, '1')

        subscriptionListServiceControl.verify()

        assertNotNull condition

        assertTrue condition.subscribedTo
        assertEquals 2, condition.subscriptionList.id
    }

    void testSubscriptionConditions_Empty() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscription.id"
        params."row[1].subscriptionType" = ''
        params."row[1].subscriptionList" = '2'

        assertNull fetcher.subscriptionCondition(params, '1')
    }
}
