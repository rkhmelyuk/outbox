package outbox.subscriber.field

/**
 * The type of dynamic field.
 * 
 * @author Ruslan Khmelyuk
 */
public enum DynamicFieldType {

    String(1),
    Integer(2),
    Boolean(3),
    SingleSelect(4);

    final int id

    DynamicFieldType(int id) {
        this.id = id
    }
}