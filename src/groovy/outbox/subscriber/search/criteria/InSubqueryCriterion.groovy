package outbox.subscriber.search.criteria

/**
 * Value or column value is w/ or w/o subquery results.
 *
 * @author Ruslan Khmelyuk
 */
class InSubqueryCriterion implements Criterion {

    def left
    String subquery
    boolean not
}
