package outbox.subscriber.field

import outbox.MessageUtil

/**
 * The type of dynamic field.
 * 
 * @author Ruslan Khmelyuk
 */
public enum DynamicFieldType {

    /**
     * String value.
     */
    String(1, 'dynamicFieldType.string'),

    /**
     * Number value.
     */
    Number(2, 'dynamicFieldType.number'),

    /**
     * User can check or un-check value only.
     */
    Boolean(3, 'dynamicFieldType.boolean'),

    /**
     * User can select one element from list.
     */
    SingleSelect(4, 'dynamicFieldType.singleSelect');

    final int id
    final String messageCode

    DynamicFieldType(int id, String messageCode) {
        this.id = id
        this.messageCode = messageCode
    }

    String getMessage() {
        MessageUtil.getMessage(messageCode, null, null)
    }

    static DynamicFieldType getById(Integer id) {
        if (id) {
            for (DynamicFieldType type in values()) {
                if (type.id == id) {
                    return type
                }
            }
        }
        return null
    }

}