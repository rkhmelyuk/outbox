package outbox.subscriber.search

import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.ColumnType
import outbox.subscription.SubscriptionStatus
import outbox.subscriber.search.condition.*
import outbox.subscriber.search.criteria.*

/**
 * @author Ruslan Khmelyuk
 */
class CriteriaVisitor implements ConditionVisitor {

    CriteriaTree subscriberFieldTree = new CriteriaTree()
    List<CriteriaTree> dynamicFieldTrees = []
    List<CriteriaTree> subscriptionTrees = []

    void visitSubscriberFieldCondition(SubscriberFieldCondition condition) {
        def criterionNode = builderFieldsCriterionNode(condition,
                new Column(Names.SubscriberAlias, condition.field))
        subscriberFieldTree.addNode(makeNode(condition, criterionNode))
    }

    void visitDynamicFieldCondition(DynamicFieldCondition condition) {
        def idCriterion = new ComparisonCriterion()
        idCriterion.left = new Column(Names.DynamicFieldAlias, Names.DynamicFieldId)
        idCriterion.right = condition.field.id
        idCriterion.comparisonOp = ' = '

        def column = getDynamicFieldColumn(Names.DynamicFieldValueAlias, condition)

        def criterionNode = new CriterionNode()
        criterionNode.type = CriterionNodeType.And
        criterionNode.left = new CriterionNode(type: CriterionNodeType.Criterion, criterion: idCriterion)
        criterionNode.right = builderFieldsCriterionNode(condition, column)

        def dynamicFieldTree = new CriteriaTree()
        dynamicFieldTree.addNode(makeNode(condition, criterionNode))
        dynamicFieldTrees << dynamicFieldTree
    }

    void visitSubscriptionCondition(SubscriptionCondition condition) {
        def subscriberCriterion = new ComparisonCriterion()
        subscriberCriterion.left = new Column(Names.SubscriptionAlias, Names.SubscriberId)
        subscriberCriterion.right = new Column(Names.SubscriberAlias, Names.SubscriberId)
        subscriberCriterion.comparisonOp = ' = '

        def statusCriterion = new ComparisonCriterion()
        statusCriterion.left = new Column(Names.SubscriptionAlias, Names.SubscriptionStatusId)
        statusCriterion.right = SubscriptionStatus.subscribed().id
        statusCriterion.comparisonOp = ' = '

        def node = new CriterionNode()
        node.type = CriterionNodeType.And
        node.left = new CriterionNode(type: CriterionNodeType.Criterion, criterion: subscriberCriterion)
        node.right = new CriterionNode(type: CriterionNodeType.Criterion, criterion: statusCriterion)

        condition.subscriptionListIds.each {
            def idCriterion = new ComparisonCriterion()
            idCriterion.left = new Column(Names.SubscriptionAlias, Names.SubscriptionListId)
            idCriterion.right = it
            idCriterion.comparisonOp = ' = '

            def criterionNode = new CriterionNode()
            criterionNode.right = node
            criterionNode.type = CriterionNodeType.And
            criterionNode.left = new CriterionNode(type: CriterionNodeType.Criterion, criterion: idCriterion)

            if (!condition.subscribedTo) {
                def notNode = new CriterionNode()
                notNode.type = CriterionNodeType.Not
                notNode.left = criterionNode
                criterionNode = notNode
            }

            def tree = new CriteriaTree()
            tree.addNode(criterionNode)
            subscriptionTrees << tree
        }
    }

    String comparisonOperation(ValueConditionType type) {
        switch (type) {
            case ValueConditionType.Equal:
                return ' = '
            case ValueConditionType.Greater:
                return ' > '
            case ValueConditionType.GreaterOrEqual:
                return ' >= '
            case ValueConditionType.Less:
                return ' < '
            case ValueConditionType.LessOrEqual:
                return ' <= '
            case ValueConditionType.NotEqual:
                return ' <> '
            case ValueConditionType.Like:
                return ' like '
        }

        return null
    }

    CriterionNode makeNode(Condition condition, CriterionNode criterionNode) {
        def node = new CriterionNode()
        if (condition.concatenation == Concatenation.And ||
                condition.concatenation == Concatenation.AndNot) {
            node.type = CriterionNodeType.And
        }
        else if (condition.concatenation == Concatenation.Or ||
                condition.concatenation == Concatenation.OrNot) {
            node.type = CriterionNodeType.Or
        }
        else {
            throw new IllegalStateException('Unexpected Concatenation')
        }

        if (condition.concatenation == Concatenation.AndNot ||
                condition.concatenation == Concatenation.OrNot) {
            def notNode = new CriterionNode()
            notNode.type = CriterionNodeType.Not
            notNode.left = criterionNode
            node.left = notNode
        }
        else {
            node.left = criterionNode
        }

        return node
    }

    Column getDynamicFieldColumn(String table, DynamicFieldCondition condition) {
        def fieldType = condition.field.type
        if (fieldType == DynamicFieldType.String) {
            return new Column(table, Names.StringValue, null, ColumnType.String)
        }
        else if (fieldType == DynamicFieldType.Number) {
            return new Column(table, Names.NumberValue, null, ColumnType.Number)
        }
        else if (fieldType == DynamicFieldType.Boolean) {
            return new Column(table, Names.BooleanValue, null, ColumnType.Boolean)
        }
        else if (fieldType == DynamicFieldType.SingleSelect) {
            return new Column(table, Names.DynamicFieldItemId, null, ColumnType.Number)
        }
        return null
    }

    CriterionNode builderFieldsCriterionNode(def condition, Column column) {
        def type = condition.value.type

        CriterionNode criterionNode
        if (type == ValueConditionType.Empty) {
            criterionNode = emptyNode(condition, column)
        }
        else if (type == ValueConditionType.Filled) {
            criterionNode = filledNode(condition, column)
        }
        else if (type == ValueConditionType.InList || type == ValueConditionType.NotInList) {
            criterionNode = inListNode(condition)
        }
        else {
            criterionNode = comparisonNode(condition, column)
        }

        return criterionNode
    }

    CriterionNode comparisonNode(def condition, Column column) {
        def criterion = new ComparisonCriterion()
        criterion.left = column
        criterion.right = condition.value.value
        criterion.comparisonOp = comparisonOperation(condition.value.type)

        def criterionNode = new CriterionNode()
        criterionNode.type = CriterionNodeType.Criterion
        criterionNode.criterion = criterion
        return criterionNode
    }

    CriterionNode inListNode(def condition) {
        def criterion = new InListCriterion()
        criterion.not = (condition.value.type == ValueConditionType.NotInList)
        criterion.values = condition.value.value
        criterion.left = condition.field

        def criterionNode = new CriterionNode()
        criterionNode.type = CriterionNodeType.Criterion
        criterionNode.criterion = criterion
        return criterionNode
    }

    CriterionNode emptyNode(def condition, Column column) {
        def leftCriterion = new ComparisonCriterion()
        leftCriterion.left = column
        leftCriterion.comparisonOp = ' is '
        leftCriterion.right = null

        def leftNode = new CriterionNode(
                type: CriterionNodeType.Criterion,
                criterion: leftCriterion)

        if (column.type == ColumnType.Number || column.type == ColumnType.Boolean) {
            return leftNode
        }

        def rightCriterion = new ComparisonCriterion()
        rightCriterion.left = column
        rightCriterion.comparisonOp = ' = '
        rightCriterion.right = ''

        def rightNode = new CriterionNode(
                type: CriterionNodeType.Criterion,
                criterion: rightCriterion)

        new CriterionNode(type: CriterionNodeType.Or,
                left: leftNode, right: rightNode)
    }

    CriterionNode filledNode(def condition, Column column) {
        def leftCriterion = new ComparisonCriterion()
        leftCriterion.left = column
        leftCriterion.comparisonOp = ' is not '
        leftCriterion.right = null

        def rightCriterion = new ComparisonCriterion()
        rightCriterion.left = column
        rightCriterion.comparisonOp = ' <> '
        rightCriterion.right = ''

        def leftNode = new CriterionNode(
                type: CriterionNodeType.Criterion,
                criterion: leftCriterion)

        if (column.type == ColumnType.Number || column.type == ColumnType.Boolean) {
            return leftNode
        }

        def rightNode = new CriterionNode(
                type: CriterionNodeType.Criterion,
                criterion: rightCriterion)

        new CriterionNode(type: CriterionNodeType.And,
                left: leftNode, right: rightNode)
    }

}
