package outbox.subscriber.search.criteria

import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.search.condition.DynamicFieldCondition
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

    void testDynamicFieldVisitor_String() {
        def condition = new DynamicFieldCondition(
                new DynamicField(id: 1, type: DynamicFieldType.String),
                ValueCondition.equal('John'))
        visitor.visitDynamicFieldCondition condition

        def node = visitor.dynamicFieldTree.root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type
        def left = node.left

        assertEquals CriterionNodeType.And, left.type

        def criterion = left.left.criterion
        assertEquals 'DF.DynamicFieldId', criterion.left
        assertEquals 1, criterion.right
        assertEquals ' = ', criterion.comparisonOp

        criterion = left.right.criterion
        assertEquals 'StringValue', criterion.left
        assertEquals 'John', criterion.right
        assertEquals ' = ', criterion.comparisonOp
    }

    void testDynamicFieldVisitor_Number() {
        def condition = new DynamicFieldCondition(
                new DynamicField(id: 1, type: DynamicFieldType.Number),
                ValueCondition.equal(20))
        visitor.visitDynamicFieldCondition condition

        def node = visitor.dynamicFieldTree.root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type
        def left = node.left

        assertEquals CriterionNodeType.And, left.type

        def criterion = left.left.criterion
        assertEquals 'DF.DynamicFieldId', criterion.left
        assertEquals 1, criterion.right
        assertEquals ' = ', criterion.comparisonOp

        criterion = left.right.criterion
        assertEquals 'NumberValue', criterion.left
        assertEquals 20, criterion.right
        assertEquals ' = ', criterion.comparisonOp
    }

    void testDynamicFieldVisitor_Boolean() {
        def condition = new DynamicFieldCondition(
                new DynamicField(id: 1, type: DynamicFieldType.Boolean),
                ValueCondition.equal(false))
        visitor.visitDynamicFieldCondition condition

        def node = visitor.dynamicFieldTree.root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type
        def left = node.left

        assertEquals CriterionNodeType.And, left.type

        def criterion = left.left.criterion
        assertEquals 'DF.DynamicFieldId', criterion.left
        assertEquals 1, criterion.right
        assertEquals ' = ', criterion.comparisonOp

        criterion = left.right.criterion
        assertEquals 'BooleanValue', criterion.left
        assertEquals false, criterion.right
        assertEquals ' = ', criterion.comparisonOp
    }

    void testDynamicFieldVisitor_SingleSelect() {
        def condition = new DynamicFieldCondition(
                new DynamicField(id: 1, type: DynamicFieldType.SingleSelect),
                ValueCondition.equal(1))
        visitor.visitDynamicFieldCondition condition

        def node = visitor.dynamicFieldTree.root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type
        def left = node.left

        assertEquals CriterionNodeType.And, left.type

        def criterion = left.left.criterion
        assertEquals 'DF.DynamicFieldId', criterion.left
        assertEquals 1, criterion.right
        assertEquals ' = ', criterion.comparisonOp

        criterion = left.right.criterion
        assertEquals 'DynamicFieldItemId', criterion.left
        assertEquals 1, criterion.right
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
