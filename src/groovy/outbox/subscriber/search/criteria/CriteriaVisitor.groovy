package outbox.subscriber.search.criteria

import outbox.subscriber.search.*

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
