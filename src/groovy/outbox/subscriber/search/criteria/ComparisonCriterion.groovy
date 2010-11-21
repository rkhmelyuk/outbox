package outbox.subscriber.search.criteria

/**
 * To compare column with value.
 * 
 * @author Ruslan Khmelyuk
 */
class ComparisonCriterion implements Criterion {

    def left
    def right
    String comparisonOp

}
