package outbox.subscriber.search

/**
 * The condition by subscriber field, like email, name or language.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriberFieldCondition implements VisitableCondition {

    final String field
    final ValueCondition value

    SubscriberFieldCondition(String field, ValueCondition value) {
        this.field = field
        this.value = value
    }

    void visit(ConditionVisitor visitor) {
        visitor.visitSubscriberFieldCondition this
    }

}
