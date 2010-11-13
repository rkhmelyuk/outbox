package outbox.subscriber.search

/**
 * @author Ruslan Khmelyuk
 */
abstract class Condition implements VisitableCondition {

    Concatenation concatenation = Concatenation.And

}
