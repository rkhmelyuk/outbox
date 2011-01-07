package outbox.subscriber.search.criteria

/**
 * The node of {@link CriteriaTree}.
 *
 * Can have left or right part or be a criteria node and contain criterion.
 *
 * @see Criterion
 * @see CriterionNodeType
 *
 *
 * @author Ruslan Khmelyuk
 */
class CriterionNode {

    CriterionNodeType type

    CriterionNode left
    CriterionNode right

    Criterion criterion

    CriterionNode clone() {
        return new CriterionNode(type: type, left: left, right: right, criterion: criterion)
    }
}
