package outbox.subscriber.search

import outbox.subscriber.search.condition.ValueConditionType

/**
 * @author Ruslan Khmelyuk
 * @created 2010-11-25
 */
class SearchConditionsFetcherTests extends GroovyTestCase {

    def fetcher = new SearchConditionsFetcher()

    void testFetch_Nothing() {
        def conditions = fetcher.fetch([:])

        assertTrue conditions.empty
    }

    void testFetch_MultipleRows() {

    }

    void testSubscriberConditions() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscriber.id"
        params."row[1].field" = "FirstName"
        params."row[1].comparison" = "$ValueConditionType.Equal.id"
        params."row[1].value" = 'John'

        def condition = fetcher.subscriberConditions(params, '1')

        assertNotNull condition

        assertEquals 'FirstName', condition.field
        assertEquals 'John', condition.value.value
        assertEquals ValueConditionType.Equal, condition.value.type
    }

    void testSubscriberConditions_NoValue() {
        def params = [:]
        params."row[1].type" = "$ConditionType.Subscriber.id"
        params."row[1].field" = "FirstName"
        params."row[1].comparison" = "$ValueConditionType.Empty.id"
        params."row[1].value" = 'John'

        def condition = fetcher.subscriberConditions(params, '1')

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

        assertNull fetcher.subscriberConditions(params, '1')
    }
}
