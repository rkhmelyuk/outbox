package outbox.subscriber.search

import outbox.subscriber.field.DynamicFieldType
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
        def criterionNode = builderFieldsCriterionNode(condition, condition.field)
        subscriberFieldTree.addNode(makeNode(condition, criterionNode))
    }

    void visitDynamicFieldCondition(DynamicFieldCondition condition) {
        def idCriterion = new ComparisonCriterion()
        idCriterion.left = 'DF.DynamicFieldId'
        idCriterion.right = condition.field.id
        idCriterion.comparisonOp = ' = '

        def column = getDynamicFieldColumn(condition)

        def criterionNode = new CriterionNode()
        criterionNode.type = CriterionNodeType.And
        criterionNode.left = new CriterionNode(type: CriterionNodeType.Criterion, criterion: idCriterion)
        criterionNode.right = builderFieldsCriterionNode(condition, column)

        def dynamicFieldTree = new CriteriaTree()
        dynamicFieldTree.addNode(makeNode(condition, criterionNode))
        dynamicFieldTrees << dynamicFieldTree
    }

    void visitSubscriptionCondition(SubscriptionCondition condition) {
        // exists (select null from Subscriber as s where s.SubscriptionListId = 10 and s.SubscriberId = SubscriberId and s.status = Active)
        // not exists (select null from Subscriber as s where s.SubscriptionListId = 20 and s.SubscriberId = xxx and s.Status = Active)

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

    String getDynamicFieldColumn(DynamicFieldCondition condition) {
        def fieldType = condition.field.type
        if (fieldType == DynamicFieldType.String) {
            return Columns.StringValue
        }
        else if (fieldType == DynamicFieldType.Number) {
            return Columns.NumberValue
        }
        else if (fieldType == DynamicFieldType.Boolean) {
            return Columns.BooleanValue
        }
        else if (fieldType == DynamicFieldType.SingleSelect) {
            return Columns.DynamicFieldItemId
        }
        return null
    }

    CriterionNode builderFieldsCriterionNode(def condition, String column) {
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

    CriterionNode comparisonNode(def condition, String column) {
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

    CriterionNode emptyNode(def condition, String column) {
        def leftCriterion = new ComparisonCriterion()
        leftCriterion.left = column
        leftCriterion.comparisonOp = ' is '
        leftCriterion.right = null

        def rightCriterion = new ComparisonCriterion()
        rightCriterion.left = column
        rightCriterion.comparisonOp = ' = '
        rightCriterion.right = ''

        def leftNode = new CriterionNode(
                type: CriterionNodeType.Criterion,
                criterion: leftCriterion)

        def rightNode = new CriterionNode(
                type: CriterionNodeType.Criterion,
                criterion: rightCriterion)

        new CriterionNode(type: CriterionNodeType.Or,
                left: leftNode, right: rightNode)
    }

    CriterionNode filledNode(def condition, String column) {
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

        def rightNode = new CriterionNode(
                type: CriterionNodeType.Criterion,
                criterion: rightCriterion)

        new CriterionNode(type: CriterionNodeType.And,
                left: leftNode, right: rightNode)
    }

}
