package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class InCriterion implements Criterion {

    String left
    List values
    boolean not

}
