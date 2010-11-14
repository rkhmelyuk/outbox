package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class InListCriterion implements Criterion {

    String left
    List values
    boolean not

}
