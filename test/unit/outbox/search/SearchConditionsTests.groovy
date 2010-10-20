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

    void testAddCondition() {
        def condition = new PageCondition()
        conditions.add condition

        assertTrue conditions.has(PageCondition)
        assertSame condition, conditions.get(PageCondition)
    }
}
