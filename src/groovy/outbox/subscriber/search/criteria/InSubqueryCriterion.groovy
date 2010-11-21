package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class InSubqueryCriterion implements Criterion {

    def left
    String subquery
    boolean not
}
