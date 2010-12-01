package outbox.subscriber.search.condition

/**
 * The enumeration of value condition types.
 * @author Ruslan Khmelyuk
 */
public enum ValueConditionType {

    Equal(1, 'valueConditionType.equal'),

    NotEqual(2, 'valueConditionType.notEqual'),

    Greater(3, 'valueConditionType.greater'),

    Less(4, 'valueConditionType.less'),

    GreaterOrEqual(5, 'valueConditionType.greaterOrEqual'),

    LessOrEqual(6, 'valueConditionType.lessOrEqual'),

    Like(7, 'valueConditionType.like'),

    InList(8, 'valueConditionType.inList'),

    NotInList(9, 'valueConditionType.notInList'),

    Empty(10, 'valueConditionType.empty'),

    Filled(11, 'valueConditionType.filled')

    final int id
    final String message

    ValueConditionType(int id, String message) {
        this.id = id
        this.message = message
    }

    String getDescriptionMessage() {
        message + '.desc'
    }

    static ValueConditionType getById(Integer id) {
        for (ValueConditionType type in values()) {
            if (type.id == id) return type
        }
        return null
    }
}