package outbox.subscriber.field

import outbox.subscriber.Subscriber

/**
 * The value for dynamic field for specified subscriber.
 *
 * @author Ruslan Khmelyuk
 */
class DynamicFieldValue {

    Long id

    Subscriber subscriber
    DynamicField dynamicField

    String stringValue
    Boolean booleanValue
    BigDecimal numberValue
    DynamicFieldItem singleItem

    static mapping = {
        table 'DynamicFieldValue'

        id column: 'DynamicFieldValueId'
        subscriber column: 'SubscriberId'
        dynamicField column: 'DynamicFieldId'

        stringValue column: 'StringValue'
        numberValue column: 'NumberValue'
        booleanValue column: 'BooleanValue'
        singleItem column: 'DynamicFieldItemId'
    }

    static constraints = {
    }
}
