package outbox.subscriber.search.criteria

/**
 * To compare column with value.
 * 
 * @author Ruslan Khmelyuk
 */
class ComparisonCriterion implements Criterion {

    String left
    String comparisonOp
    def right

}
