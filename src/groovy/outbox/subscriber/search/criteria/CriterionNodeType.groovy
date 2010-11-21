package outbox.subscriber.search.criteria

/**
 * The criterion node type.
 *
 * @author Ruslan Khmelyuk
 */
enum CriterionNodeType {

    /**
     * Node to contain criterion.
     */
    Criterion,

    /**
     * Node to concatenate two criterion by And.
     */
    And,

    /**
     * Node to concatenate two criterion by Or.
     */
    Or,

    /**
     * Node to negate criterion.
     */
    Not
}
