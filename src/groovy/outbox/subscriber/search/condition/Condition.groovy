package outbox.subscriber.search.condition

/**
 * Base for conditions.
 *
 * @author Ruslan Khmelyuk
 */
abstract class Condition implements VisitableCondition {

    Concatenation concatenation = Concatenation.And

}
