package outbox.subscriber.search.criteria

import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.ValueCondition
import outbox.subscriber.search.condition.ValueConditionType

/**
 * @author Ruslan Khmelyuk
 */
class CriteriaVisitorTests extends GroovyTestCase {

    CriteriaVisitor visitor

    @Override protected void setUp() {
        super.setUp()

        visitor = new CriteriaVisitor()
    }

    void testSubscriberFieldVisitor() {
        def condition = new SubscriberFieldCondition('FirstName', ValueCondition.equal('John'))
        visitor.visitSubscriberFieldCondition condition

        def node = visitor.subscriberFieldTree.root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type

        def criterion = node.left.criterion
        assertEquals 'FirstName', criterion.left
        assertEquals 'John', criterion.right
        assertEquals ' = ', criterion.comparisonOp
    }

    void testComparisonOperation() {
        assertEquals ' = ', visitor.comparisonOperation(ValueConditionType.Equal)
        assertEquals ' <> ', visitor.comparisonOperation(ValueConditionType.NotEqual)
        assertEquals ' like ', visitor.comparisonOperation(ValueConditionType.Like)
        assertEquals ' < ', visitor.comparisonOperation(ValueConditionType.Less)
        assertEquals ' <= ', visitor.comparisonOperation(ValueConditionType.LessOrEqual)
        assertEquals ' > ', visitor.comparisonOperation(ValueConditionType.Greater)
        assertEquals ' >= ', visitor.comparisonOperation(ValueConditionType.GreaterOrEqual)
        assertNull visitor.comparisonOperation(ValueConditionType.InList)
    }
}
