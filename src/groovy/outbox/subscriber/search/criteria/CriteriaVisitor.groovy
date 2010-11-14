package outbox.subscriber.search.criteria

import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.search.condition.*

/**
 * @author Ruslan Khmelyuk
 */
class CriteriaVisitor implements ConditionVisitor {

    CriteriaTree subscriptionTree = new CriteriaTree()
    CriteriaTree dynamicFieldTree = new CriteriaTree()
    CriteriaTree subscriberFieldTree = new CriteriaTree()

    void visitSubscriberFieldCondition(SubscriberFieldCondition condition) {
        def criterion
        def type = condition.value.type
        if (type == ValueConditionType.InList || type == ValueConditionType.NotInList) {
            criterion = new InCriterion()
            criterion.not = (type == ValueConditionType.NotInList)
            criterion.values = condition.value.value
        }
        else {
            criterion = new ComparisonCriterion()
            criterion.right = condition.value.value
            criterion.comparisonOp = comparisonOperation(type)
        }
        criterion.left = condition.field

        def criterionNode = new CriterionNode()
        criterionNode.type = CriterionNodeType.Criterion
        criterionNode.criterion = criterion

        def node = new CriterionNode()
        node.type = CriterionNodeType.And
        node.left = criterionNode

        subscriberFieldTree.addNode(node)
    }

    void visitDynamicFieldCondition(DynamicFieldCondition condition) {
        def idCriterion = new ComparisonCriterion()
        idCriterion.left = 'DF.DynamicFieldId'
        idCriterion.right = condition.field.id
        idCriterion.comparisonOp = ' = '

        def criterion
        def type = condition.value.type
        if (type == ValueConditionType.InList || type == ValueConditionType.NotInList) {
            criterion = new InCriterion()
            criterion.not = (type == ValueConditionType.NotInList)
            criterion.values = condition.value.value
        }
        else {
            criterion = new ComparisonCriterion()
            criterion.right = condition.value.value
            criterion.comparisonOp = comparisonOperation(type)
        }

        def fieldType = condition.field.type
        if (fieldType == DynamicFieldType.String) {
            criterion.left = 'StringValue'
        }
        else if (fieldType == DynamicFieldType.Number) {
            criterion.left = 'NumberValue'
        }
        else if (fieldType == DynamicFieldType.Boolean) {
            criterion.left = 'BooleanValue'
        }
        else if (fieldType == DynamicFieldType.SingleSelect) {
            criterion.left = 'DynamicFieldItemId'
        }

        def criterionNode = new CriterionNode()
        criterionNode.type = CriterionNodeType.And
        criterionNode.left = new CriterionNode(type: CriterionNodeType.Criterion, criterion: idCriterion)
        criterionNode.right = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)

        def node = new CriterionNode()
        node.type = CriterionNodeType.And
        node.left = criterionNode

        dynamicFieldTree.addNode(node)
    }

    void visitSubscriptionCondition(SubscriptionCondition condition) {

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

}
