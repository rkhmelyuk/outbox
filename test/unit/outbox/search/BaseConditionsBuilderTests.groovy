package outbox.search

import grails.test.GrailsUnitTestCase
import outbox.campaign.CampaignConditionsBuilder
import outbox.member.Member

/**
 * {@link CampaignConditionsBuilder} tests.
 *
 * @author Ruslan Khmelyuk
 * @since  2010-09-19
 */
class BaseConditionsBuilderTests extends GrailsUnitTestCase {

    def condition = new BaseConditionsBuilder()

    void testOwnerCondition() {
        def conditions = condition.build {
            ownedBy new Member(id: 1)
        }

        assertNotNull conditions
        assertNotNull conditions.get(OwnedByCondition).member
        assertEquals 1, conditions.get(OwnedByCondition).member.id
    }

    void testPageCondition() {
        def conditions = condition.build {
            page 3
            max 10
        }

        assertNotNull conditions
        assertEquals 3, conditions.get(PageCondition).page
        assertEquals 10, conditions.get(PageCondition).max
    }

    void testOrderCondition() {
        def conditions = condition.build {
            order 'name', 'asc'
            order 'date', 'desc'
            order null, 'desc'
            order 'type', null
        }

        assertNotNull conditions
        assertNotNull conditions.get(OrderCondition)
        assertEquals 2, conditions.get(OrderCondition).order.size()
    }

    void testFlags() {
        def conditions = condition.build {
            returnCount true
            returnResult true
            cache true
        }

        assertNotNull conditions
        assertTrue conditions.includeCount
        assertTrue conditions.includeFound
        assertTrue conditions.cacheQuery
    }

    void testDefaultFlags() {
        def conditions = condition.build { }

        assertNotNull conditions
        assertTrue conditions.includeFound
        assertFalse conditions.includeCount
        assertFalse conditions.cacheQuery
    }

}
