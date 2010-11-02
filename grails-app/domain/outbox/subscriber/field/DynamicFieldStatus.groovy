package outbox.subscriber.field

/**
 * The status of Dynamic Field.
 * 
 * @author Ruslan Khmelyuk
 */
enum DynamicFieldStatus {

    Active(1),
    Hidden(2),
    Removed(3)

    final int id

    DynamicFieldStatus(int id) {
        this.id = id
    }
}
