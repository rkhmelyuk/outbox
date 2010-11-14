package outbox.subscriber.search.condition

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberFieldConditionTests extends GrailsUnitTestCase {

    void testFields() {
        def condition = new SubscriberFieldCondition('language', ValueCondition.filled())
        condition.concatenation = Concatenation.And

        assertEquals 'language', condition.field
        assertNull condition.value.value
        assertEquals ValueConditionType.Filled, condition.value.type
        assertEquals Concatenation.And, condition.concatenation
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
