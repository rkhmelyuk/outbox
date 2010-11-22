package outbox.subscriber.search

/**
 * Conditions types to be selected by user.
 *
 * @author Ruslan Khmelyuk
 */
enum ConditionType {

    Subscriber(1, 'conditionType.subscriber'),
    DynamicField(2, 'conditionType.dynamicField'),
    Subscription(3, 'conditionType.subscription')

    final int id
    final String message

    ConditionType(int id, String message) {
        this.id = id
        this.message = message
    }

    static ConditionType getById(int id) {
        values().find { it.id == id }
    }
}
