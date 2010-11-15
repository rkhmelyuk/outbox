package outbox.subscriber.field

import outbox.ValueUtil
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.Subscriber
import outbox.subscriber.search.Columns

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

    DynamicFieldService dynamicFieldService

    static mapping = {
        table 'DynamicFieldValue'

        id column: 'DynamicFieldValueId'
        subscriber column: 'SubscriberId'
        dynamicField column: 'DynamicFieldId'

        stringValue column: Columns.StringValue
        numberValue column: Columns.NumberValue
        booleanValue column: Columns.BooleanValue
        singleItem column: Columns.DynamicFieldItemId

        version false
        cache true

        sort 'dynamicField'
    }

    static constraints = {
        subscriber nullable: false
        dynamicField nullable: false
        stringValue nullable: true, maxSize: 9999
        numberValue nullable: true
        booleanValue nullable: true
        singleItem nullable: true
    }

    static transients = ['value', 'dynamicFieldService']

    /**
     * Gets correct value by dynamic field type.
     * @return the value by type.
     */
    def getValue() {
        def result = null
        if (dynamicField?.type != null) {
            switch (dynamicField.type) {
                case DynamicFieldType.String:
                    result = stringValue
                    break
                case DynamicFieldType.Number:
                    result = numberValue
                    break
                case DynamicFieldType.Boolean:
                    result = booleanValue
                    break
                case DynamicFieldType.SingleSelect:
                    result = singleItem
                    break
            }
        }
        if (result == null) {
            result = stringValue
            if (result == null) {
                result = numberValue
            }
            if (result == null) {
                result = booleanValue
            }
            if (result == null) {
                result = singleItem
            }
        }
        return result
    }

    /**
     * Sets correct value by dynamic field type.
     * @param value the new value.
     */
    void setValue(def value) {

        stringValue = null
        numberValue = null
        booleanValue = null
        singleItem = null

        if (dynamicField?.type != null) {
            switch (dynamicField.type) {
                case DynamicFieldType.String:
                    stringValue = value as String
                    return
                case DynamicFieldType.Number:
                    numberValue = value as BigDecimal
                    return
                case DynamicFieldType.Boolean:
                    booleanValue = value as Boolean
                    return
                case DynamicFieldType.SingleSelect:
                    if (value instanceof DynamicFieldItem) {
                        singleItem = value
                        return
                    }
                    else if (value) {
                        def id = ValueUtil.getLong(value.toString())
                        if (id) {
                            def item = dynamicFieldService.getDynamicFieldItem(id)
                            if (item.field.id == dynamicField.id) {
                                singleItem = item
                                return
                            }
                        }
                    }
                    singleItem = null
                    return
            }
        }
        stringValue = value
    }
}
