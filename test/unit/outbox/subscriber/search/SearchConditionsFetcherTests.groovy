package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.GrailsUnitTestCase
import outbox.member.Member
import outbox.security.OutboxUser
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicField
import outbox.subscriber.search.condition.ValueConditionType

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

    void testSubscriberConditions_Like() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscriber.id"
        params."row[1].field" = "FirstName"
        params."row[1].comparison" = "$ValueConditionType.Like.id"
        params."row[1].value" = 'John'

        def condition = fetcher.subscriberCondition(params, '1')

        assertNotNull condition

        assertEquals 'FirstName', condition.field
        assertEquals '%John%', condition.value.value
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
}
