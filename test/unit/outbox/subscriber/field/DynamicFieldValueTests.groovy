package outbox.subscriber.field

import grails.test.GrailsUnitTestCase
import outbox.subscriber.Subscriber

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldValueTests extends GrailsUnitTestCase {

    void testFields() {
        def value = new DynamicFieldValue()
        value.id = 1
        value.subscriber = new Subscriber(id: '00000')
        value.dynamicField = new DynamicField(id: 2)
        value.stringValue = 'test'
        value.numberValue = 53.25
        value.booleanValue = true
        value.singleItem = new DynamicFieldItem(id: 3)

        assertEquals 1, value.id
        assertEquals 2, value.dynamicField.id
        assertEquals 3, value.singleItem.id
        assertEquals 53,25, value.numberValue
        assertEquals 'test', value.stringValue
        assertEquals '00000', value.subscriber.id
        assertTrue value.booleanValue
    }

    void testGetValue() {
        def value = new DynamicFieldValue()
        value.dynamicField = new DynamicField(id: 2)
        value.stringValue = 'test'
        value.numberValue = 53.25
        value.booleanValue = true
        value.singleItem = new DynamicFieldItem(id: 3)

        assertEquals value.stringValue, value.value

        value.dynamicField.type = DynamicFieldType.String
        assertEquals value.stringValue, value.value

        value.dynamicField.type = DynamicFieldType.Number
        assertEquals value.numberValue, value.value

        value.dynamicField.type = DynamicFieldType.Boolean
        assertEquals value.booleanValue, value.value

        value.dynamicField.type = DynamicFieldType.SingleSelect
        assertEquals value.singleItem, value.value
    }

    void testGetValue_OtherType() {
        def value = new DynamicFieldValue()
        value.dynamicField = new DynamicField(id: 2)
        value.stringValue = 'test'

        value.dynamicField.type = DynamicFieldType.String
        assertEquals value.stringValue, value.value

        value.dynamicField.type = DynamicFieldType.Number
        assertEquals value.stringValue, value.value

        value.value = 12.34
        assertEquals value.numberValue, value.value
        assertNull value.stringValue
    }

    void testSetValue() {
        def value = new DynamicFieldValue()
        value.dynamicField = new DynamicField(id: 2)

        value.value = 'most'
        assertEquals 'most', value.stringValue

        value.dynamicField.type = DynamicFieldType.String
        value.value = 123
        assertEquals '123', value.stringValue

        value.dynamicField.type = DynamicFieldType.Number
        value.value = '12.22'
        assertEquals 12.22, value.numberValue
        assertNull value.stringValue

        value.dynamicField.type = DynamicFieldType.Boolean
        value.value = true
        assertEquals true, value.booleanValue
        assertNull value.numberValue

        value.dynamicField.type = DynamicFieldType.SingleSelect
        value.value = new DynamicFieldItem(id: 2)
        assertEquals 2, value.singleItem.id
        assertNull value.booleanValue
    }
}
