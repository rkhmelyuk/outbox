package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 * @created 2010-11-14
 */
class CriterionNode {

    CriterionNodeType type

    CriterionNode left
    CriterionNode right

    Criterion criterion
}
