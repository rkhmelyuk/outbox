package outbox.subscriber.search.condition

/**
 * Base for conditions.
 *
 * @author Ruslan Khmelyuk
 */
abstract class Condition implements VisitableCondition {

    /**
     * How to concatenate this condition.
     */
    Concatenation concatenation = Concatenation.And

    /**
     * Whether this condition should be shown to the user.
     */
    boolean visible = true

    /**
     * Whether this condition should be shown as read only.
     */
    boolean readOnly = false

}
