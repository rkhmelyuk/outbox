package outbox.subscriber.search.criteria

/**
 * Value or column value is w/ or w/o list of values.
 *
 * @author Ruslan Khmelyuk
 */
class InListCriterion implements Criterion {

    def left
    List values
    boolean not

}
