package outbox.search

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class SearchConditionsTests extends GrailsUnitTestCase {

    SearchConditions conditions

    @Override protected void setUp() {
        super.setUp()

        conditions = new SearchConditions()
    }

    void testFields() {
        conditions.includeCount = true
        conditions.includeFound = true
        conditions.cacheQuery = true

        assertTrue conditions.includeCount
        assertTrue conditions.includeFound
        assertTrue conditions.cacheQuery
    }

    void testAddCondition() {
        def condition = new PageCondition()
        conditions.add condition

        assertTrue conditions.has(PageCondition)
        assertSame condition, conditions.get(PageCondition)
    }

    void testGetCondition() {
        def condition1 = new PageCondition()
        def condition2 = new OwnedByCondition()

        conditions.add condition1
        conditions.add condition2

        assertSame condition1, conditions.get(PageCondition)
        assertSame condition2, conditions.get(OwnedByCondition)
        assertNull conditions.get(ArchivedCondition)
    }

    void testHas() {
        conditions.add new PageCondition()
        assertTrue conditions.has(PageCondition)
        assertFalse conditions.has(OwnedByCondition)
    }
}
