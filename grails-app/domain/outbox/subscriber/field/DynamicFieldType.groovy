package outbox.subscriber.field

/**
 * The type of dynamic field.
 * 
 * @author Ruslan Khmelyuk
 */
public enum DynamicFieldType {

    /**
     * String value.
     */
    String(1),

    /**
     * Number value.
     */
    Number(2),

    /**
     * User can check or un-check value only.
     */
    Boolean(3),

    /**
     * User can select one element from list.
     */
    SingleSelect(4);

    final int id

    DynamicFieldType(int id) {
        this.id = id
    }
}