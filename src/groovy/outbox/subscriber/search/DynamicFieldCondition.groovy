package outbox.subscriber.search

import outbox.subscriber.field.DynamicField

/**
 * Condition used to search by dynamic field values.
 * 
 * @author Ruslan Khmelyuk
 */
class DynamicFieldCondition implements VisitableCondition {

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
