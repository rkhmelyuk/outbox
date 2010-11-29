package outbox.subscriber.search.criteria

import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.search.CriteriaVisitor
import outbox.subscriber.search.Names
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.ColumnType
import outbox.subscriber.search.condition.*

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
        assertEquals 'S.FirstName', criterion.left.toSQL()
        assertEquals 'John', criterion.right
        assertEquals ' = ', criterion.comparisonOp
    }

    void testDynamicFieldVisitor_String() {
        def condition = new DynamicFieldCondition(
                new DynamicField(id: 1, type: DynamicFieldType.String),
                ValueCondition.equal('John'))
        visitor.visitDynamicFieldCondition condition

        def node = visitor.dynamicFieldTrees[0].root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type
        def left = node.left

        assertEquals CriterionNodeType.And, left.type

        def criterion = left.left.criterion
        assertEquals 'DF.DynamicFieldId', criterion.left.toSQL()
        assertEquals 1, criterion.right
        assertEquals ' = ', criterion.comparisonOp

        criterion = left.right.criterion
        assertEquals 'DFV.' + Names.StringValue, criterion.left.toSQL()
        assertEquals 'John', criterion.right
        assertEquals ' = ', criterion.comparisonOp
    }

    void testDynamicFieldVisitor_Number() {
        def condition = new DynamicFieldCondition(
                new DynamicField(id: 1, type: DynamicFieldType.Number),
                ValueCondition.equal(20))
        visitor.visitDynamicFieldCondition condition

        def node = visitor.dynamicFieldTrees[0].root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type
        def left = node.left

        assertEquals CriterionNodeType.And, left.type

        def criterion = left.left.criterion
        assertEquals 'DF.DynamicFieldId', criterion.left.toSQL()
        assertEquals 1, criterion.right
        assertEquals ' = ', criterion.comparisonOp

        criterion = left.right.criterion
        assertEquals 'DFV.' + Names.NumberValue, criterion.left.toSQL()
        assertEquals 20, criterion.right
        assertEquals ' = ', criterion.comparisonOp
    }

    void testDynamicFieldVisitor_Boolean() {
        def condition = new DynamicFieldCondition(
                new DynamicField(id: 1, type: DynamicFieldType.Boolean),
                ValueCondition.equal(false))
        visitor.visitDynamicFieldCondition condition

        def node = visitor.dynamicFieldTrees[0].root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type
        def left = node.left

        assertEquals CriterionNodeType.And, left.type

        def criterion = left.left.criterion
        assertEquals 'DF.DynamicFieldId', criterion.left.toSQL()
        assertEquals 1, criterion.right
        assertEquals ' = ', criterion.comparisonOp

        criterion = left.right.criterion
        assertEquals 'DFV.' + Names.BooleanValue, criterion.left.toSQL()
        assertEquals false, criterion.right
        assertEquals ' = ', criterion.comparisonOp
    }

    void testDynamicFieldVisitor_SingleSelect() {
        def condition = new DynamicFieldCondition(
                new DynamicField(id: 1, type: DynamicFieldType.SingleSelect),
                ValueCondition.equal(1))
        visitor.visitDynamicFieldCondition condition

        def node = visitor.dynamicFieldTrees[0].root

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type
        def left = node.left

        assertEquals CriterionNodeType.And, left.type

        def criterion = left.left.criterion
        assertEquals 'DF.DynamicFieldId', criterion.left.toSQL()
        assertEquals 1, criterion.right
        assertEquals ' = ', criterion.comparisonOp

        criterion = left.right.criterion
        assertEquals 'DFV.' + Names.DynamicFieldItemId, criterion.left.toSQL()
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

    void testMakeNode() {
        def condition = new SubscriberFieldCondition(null, null)
        condition.concatenation = Concatenation.AndNot

        def testNode = new CriterionNode()
        def node = visitor.makeNode(condition, testNode)

        assertEquals CriterionNodeType.Not, node.left.type
        assertEquals testNode, node.left.left
    }

    void testGetDynamicFieldColumn() {
        def condition = new DynamicFieldCondition(new DynamicField(type: DynamicFieldType.String), null)
        def column = visitor.getDynamicFieldColumn(Names.DynamicFieldValueAlias, condition)
        assertEquals Names.StringValue, column.name
        assertEquals ColumnType.String, column.type
        assertEquals Names.DynamicFieldValueAlias, column.table

        condition = new DynamicFieldCondition(new DynamicField(type: DynamicFieldType.Number), null)
        column = visitor.getDynamicFieldColumn(Names.DynamicFieldValueAlias, condition)
        assertEquals Names.NumberValue, column.name
        assertEquals ColumnType.Number, column.type
        assertEquals Names.DynamicFieldValueAlias, column.table

        condition = new DynamicFieldCondition(new DynamicField(type: DynamicFieldType.Boolean), null)
        column = visitor.getDynamicFieldColumn(Names.DynamicFieldValueAlias, condition)
        assertEquals Names.BooleanValue, column.name
        assertEquals ColumnType.Boolean, column.type
        assertEquals Names.DynamicFieldValueAlias, column.table

        condition = new DynamicFieldCondition(new DynamicField(type: DynamicFieldType.SingleSelect), null)
        column = visitor.getDynamicFieldColumn(Names.DynamicFieldValueAlias, condition)
        assertEquals Names.DynamicFieldItemId, column.name
        assertEquals ColumnType.Number, column.type
        assertEquals Names.DynamicFieldValueAlias, column.table
    }

    void testEmptyNode() {
        def condition = new SubscriberFieldCondition('FirstName', ValueCondition.empty())

        def column = new Column('S', 'FirstName')
        def node = visitor.emptyNode(condition, column)

        assertNotNull node
        assertEquals CriterionNodeType.Or, node.type

        def left = node.left
        assertNotNull left
        assertEquals CriterionNodeType.Criterion, left.type
        assertEquals column, left.criterion.left
        assertEquals ' is ', left.criterion.comparisonOp
        assertNull left.criterion.right

        def right = node.right
        assertNotNull right
        assertEquals CriterionNodeType.Criterion, right.type
        assertEquals column, right.criterion.left
        assertEquals ' = ', right.criterion.comparisonOp
        assertEquals '', right.criterion.right
    }

    void testEmptyNode_Number() {
        def condition = new SubscriberFieldCondition('FirstName', ValueCondition.empty())

        def column = new Column('S', 'FirstName', null, ColumnType.Number)
        def node = visitor.emptyNode(condition, column)

        assertNotNull node
        assertEquals CriterionNodeType.Criterion, node.type
        assertEquals column, node.criterion.left
        assertEquals ' is ', node.criterion.comparisonOp
        assertNull node.criterion.right
    }

    void testFilledNode() {
        def condition = new SubscriberFieldCondition('FirstName', ValueCondition.empty())

        def column = new Column('S', 'FirstName')
        def node = visitor.filledNode(condition, column)

        assertNotNull node
        assertEquals CriterionNodeType.And, node.type

        def left = node.left
        assertNotNull left
        assertEquals CriterionNodeType.Criterion, left.type
        assertEquals column, left.criterion.left
        assertEquals ' is not ', left.criterion.comparisonOp
        assertNull left.criterion.right

        def right = node.right
        assertNotNull right
        assertEquals CriterionNodeType.Criterion, right.type
        assertEquals column, right.criterion.left
        assertEquals ' <> ', right.criterion.comparisonOp
        assertEquals '', right.criterion.right
    }

    void testFilledNode_Number() {
        def condition = new SubscriberFieldCondition('FirstName', ValueCondition.empty())

        def column = new Column('S', 'FirstName', null, ColumnType.Number)
        def node = visitor.filledNode(condition, column)

        assertNotNull node
        assertEquals CriterionNodeType.Criterion, node.type
        assertEquals column, node.criterion.left
        assertEquals ' is not ', node.criterion.comparisonOp
        assertNull node.criterion.right
    }

    void testInListNode() {
        def condition = new SubscriberFieldCondition('Age', ValueCondition.inList([10, 20, 30]))
        def node = visitor.inListNode(condition)

        assertNotNull node
        assertEquals CriterionNodeType.Criterion, node.type
        assertEquals 'Age', node.criterion.left
        assertFalse node.criterion.not
        assertEquals([10, 20, 30], node.criterion.values)
    }

    void testNotInListNode() {
        def condition = new SubscriberFieldCondition('Age', ValueCondition.notInList([10, 20, 30]))
        def node = visitor.inListNode(condition)

        assertNotNull node
        assertEquals CriterionNodeType.Criterion, node.type
        assertEquals 'Age', node.criterion.left
        assertTrue node.criterion.not
        assertEquals([10, 20, 30], node.criterion.values)
    }

    void testComparisonNode() {
        def condition = new SubscriberFieldCondition('Age', ValueCondition.equal(23))
        def column = new Column('S', 'Age')
        def node = visitor.comparisonNode(condition, column)

        assertNotNull node
        assertEquals CriterionNodeType.Criterion, node.type
        assertEquals column, node.criterion.left
        assertEquals ' = ', node.criterion.comparisonOp
        assertEquals 23, node.criterion.right
    }
}
