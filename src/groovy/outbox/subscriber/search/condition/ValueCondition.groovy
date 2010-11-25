package outbox.subscriber.search.condition

/**
 * The condition for specified value.
 * 
 * @author Ruslan Khmelyuk
 */
class ValueCondition {

    /**
     * Field is filled.
     */
    static ValueCondition filled() {
        new ValueCondition(null, ValueConditionType.Filled)
    }

    /**
     * Field is empty.
     */
    static ValueCondition empty() {
        new ValueCondition(null, ValueConditionType.Empty)
    }

    /**
     * Field has value like argument.
     */
    static ValueCondition like(String value) {
        new ValueCondition(value, ValueConditionType.Like)
    }

    /**
     * Field value is equal to the argument.
     */
    static ValueCondition equal(def value) {
        new ValueCondition(value, ValueConditionType.Equal)
    }

    /**
     * Field value is not equal to the argument.
     */
    static ValueCondition notEqual(def value) {
        new ValueCondition(value, ValueConditionType.NotEqual)
    }

    /**
     * Field value is greater than argument.
     */
    static ValueCondition greater(BigDecimal value) {
        new ValueCondition(value, ValueConditionType.Greater)
    }

    /**
     * Field value is greater or equal than argument.
     */
    static ValueCondition greaterEqual(BigDecimal value) {
        new ValueCondition(value, ValueConditionType.GreaterOrEqual)
    }

    /**
     * Field value is less than argument.
     */
    static ValueCondition less(BigDecimal value) {
        new ValueCondition(value, ValueConditionType.Less)
    }

    /**
     * Field value is less or equal than argument.
     */
    static ValueCondition lessEqual(BigDecimal value) {
        new ValueCondition(value, ValueConditionType.LessOrEqual)
    }

    /**
     * Field value is in list.
     */
    static ValueCondition inList(List list) {
        new ValueCondition(list, ValueConditionType.InList)
    }

    /**
     * Field value is not in list.
     */
    static ValueCondition notInList(List list) {
        new ValueCondition(list, ValueConditionType.NotInList)
    }

    // --------------------------------------------

    final def value
    final ValueConditionType type

    ValueCondition(def value, ValueConditionType type) {
        this.value = value
        this.type = type
    }
}
