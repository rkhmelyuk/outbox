package outbox.subscriber.search.criteria

/**
 * To compare left and right parts.
 * Left and right parts can be rather {@link outbox.subscriber.search.query.elems.Column} or scalar value.
 * 
 * @author Ruslan Khmelyuk
 */
class ComparisonCriterion implements Criterion {

    def left
    def right
    String comparisonOp

}
