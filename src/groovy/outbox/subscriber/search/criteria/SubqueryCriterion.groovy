package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class SubqueryCriterion implements Criterion {

    String left
    String subquery
    boolean not
}
