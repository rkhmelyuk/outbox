package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class InSubqueryCriterion implements Criterion {

    String left
    String subquery
    boolean not
}
