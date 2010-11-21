package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class InListCriterion implements Criterion {

    def left
    List values
    boolean not

}
