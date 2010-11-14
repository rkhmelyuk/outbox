package outbox.subscriber.search.condition

/**
 * @author Ruslan Khmelyuk
 */
abstract class Condition implements VisitableCondition {

    Concatenation concatenation = Concatenation.And

}
