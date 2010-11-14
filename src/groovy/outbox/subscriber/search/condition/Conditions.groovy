package outbox.subscriber.search.condition

/**
 * Used to gather a set of conditions into one object.
 * 
 * @author Ruslan Khmelyuk
 */
class Conditions {

    final List<Condition> conditions = []

    int page
    int perPage

    void add(Concatenation concatenation, Condition condition) {
        if (condition && concatenation) {
            condition.concatenation = concatenation
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

    boolean isEmpty() {
        conditions.empty
    }

    void visit(ConditionVisitor visitor) {
        if (visitor) {
            conditions*.visit(visitor)
        }
    }

}
