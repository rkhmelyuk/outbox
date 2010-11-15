package outbox.subscriber.search.condition

import outbox.subscriber.search.ConditionVisitor

/**
 * The condition by subscriber field, like email, name or language.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriberFieldCondition extends Condition {

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
