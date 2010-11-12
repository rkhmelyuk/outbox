package outbox.subscriber.search

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberFieldConditionTests extends GrailsUnitTestCase {

    void testFields() {
        def condition = new SubscriberFieldCondition('language', ValueCondition.filled())

        assertEquals 'language', condition.field
        assertNull condition.value.value
        assertEquals ValueConditionType.Filled, condition.value.type
    }

    void testVisit() {
        def condition = new SubscriberFieldCondition(null, null)

        def visitorControl = mockFor(ConditionVisitor)
        visitorControl.demand.visitSubscriberFieldCondition { it ->
            assertEquals condition, it
        }

        condition.visit visitorControl.createMock()

        visitorControl.verify()
    }

}
