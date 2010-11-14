package outbox.subscriber.search.condition

import grails.test.GrailsUnitTestCase
import outbox.subscriber.field.DynamicField

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldConditionTests extends GrailsUnitTestCase {

    void testFields() {
        def condition = new DynamicFieldCondition(new DynamicField(id: 2), ValueCondition.filled())
        condition.concatenation = Concatenation.Or

        assertEquals 2, condition.field.id
        assertNull condition.value.value
        assertEquals ValueConditionType.Filled, condition.value.type
        assertEquals Concatenation.Or, condition.concatenation
    }

    void testVisit() {
        def condition = new DynamicFieldCondition(null, null)

        def visitorControl = mockFor(ConditionVisitor)
        visitorControl.demand.visitDynamicFieldCondition { it ->
            assertEquals condition, it
        }

        condition.visit visitorControl.createMock()

        visitorControl.verify()
    }
}
