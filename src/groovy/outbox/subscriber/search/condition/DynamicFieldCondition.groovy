package outbox.subscriber.search.condition

import outbox.subscriber.field.DynamicField
import outbox.subscriber.search.ConditionVisitor

/**
 * Condition used to search by dynamic field values.
 * 
 * @author Ruslan Khmelyuk
 */
class DynamicFieldCondition extends Condition {

    final DynamicField field
    final ValueCondition value

    DynamicFieldCondition(DynamicField field, ValueCondition value) {
        this.field = field
        this.value = value
    }

    void visit(ConditionVisitor visitor) {
        visitor.visitDynamicFieldCondition this
    }

}
