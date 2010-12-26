package outbox.subscriber.search

import outbox.subscriber.search.condition.Concatenation
import outbox.subscriber.search.condition.Condition

/**
 * Used to gather a set of conditions into one object.
 * 
 * @author Ruslan Khmelyuk
 */
class Conditions {

    final List<FieldOrder> orders = []
    final List<Condition> conditions = []

    Integer page
    Integer perPage

    void add(Concatenation concatenation, Condition condition) {
        if (condition && concatenation) {
            condition.concatenation = concatenation
            conditions << condition
        }
    }

    void addFirst(Concatenation concatenation, Condition condition) {
        if (condition && concatenation) {
            condition.concatenation = concatenation
            conditions.add(0, condition)
        }
    }

    void add(Condition condition) {
        if (condition && condition.concatenation) {
            conditions << condition
        }
    }

    void and(Condition condition) {
        add(Concatenation.And, condition)
    }

    void or(Condition condition) {
        add(Concatenation.Or, condition)
    }

    void andNot(Condition condition) {
        add(Concatenation.AndNot, condition)
    }

    void orNot(Condition condition) {
        add(Concatenation.OrNot, condition)
    }

    void orderBy(String column, Sort sort = null) {
        if (column) {
            orders << new FieldOrder(column, sort ?: Sort.Asc)
        }
    }

    boolean isEmpty() {
        conditions.empty
    }

    void visit(ConditionVisitor visitor) {
        if (visitor) {
            conditions*.visit(visitor)
        }
    }

}
