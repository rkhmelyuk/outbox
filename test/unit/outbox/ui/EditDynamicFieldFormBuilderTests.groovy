package outbox.ui

import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.field.DynamicFieldValue
import outbox.ui.element.UICheckbox
import outbox.ui.element.UIInputText
import outbox.ui.element.UIInputTextArea
import outbox.ui.element.UISelectSingle

/**
 * @author Ruslan Khmelyuk
 */
class EditDynamicFieldFormBuilderTests extends GroovyTestCase {

    def builder

    @Override protected void setUp() {
        super.setUp()

        builder = new EditDynamicFieldsFormBuilder()
    }

    void testBuildString() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.String, maxlength: 200)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: 'test string')
        def element = builder.buildString(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UIInputText
        assertEquals dynamicField.name, element.label.forId
        assertEquals dynamicField.label, element.label.text
        assertEquals dynamicField.name, element.id
        assertEquals dynamicField.name, element.name
        assertEquals 'large', element.styleClass
        assertEquals 'test string', element.value
        assertTrue element.mandatory
    }

    void testBuildString_Area() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.String, maxlength: 1200)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: 'test string')
        def element = builder.buildString(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UIInputTextArea
        assertEquals dynamicField.name, element.label.forId
        assertEquals dynamicField.label, element.label.text
        assertEquals dynamicField.name, element.id
        assertEquals dynamicField.name, element.name
        assertEquals 'normal', element.styleClass
        assertEquals 'test string', element.value
        assertTrue element.mandatory
    }

    void testBuildNumber() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.Number, maxlength: 200)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: 123)
        def element = builder.buildNumber(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UIInputText
        assertEquals dynamicField.name, element.label.forId
        assertEquals dynamicField.label, element.label.text
        assertEquals dynamicField.name, element.id
        assertEquals dynamicField.name, element.name
        assertEquals 14, element.maxlength
        assertEquals 'number', element.styleClass
        assertEquals '123', element.value
        assertTrue element.mandatory
    }

    void testBuildBoolean() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.Boolean)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: true)
        def element = builder.buildBoolean(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UICheckbox
        assertEquals dynamicField.name, element.label.forId
        assertEquals dynamicField.label, element.label.text
        assertEquals dynamicField.name, element.id
        assertTrue element.value
        assertTrue element.mandatory
    }

    void testBuildBoolean_NoValue() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label', type: DynamicFieldType.Boolean)
        def element = builder.buildBoolean(dynamicField, null)

        assertTrue element instanceof UICheckbox
        assertEquals dynamicField.name, element.label.forId
        assertEquals dynamicField.label, element.label.text
        assertEquals dynamicField.name, element.id
        assertFalse element.value
    }

    void testBuildSelect() {
        def dynamicField = new DynamicField(
                name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.SingleSelect)
        def items = [new DynamicFieldItem(id: 1, name: 'Item1'), new DynamicFieldItem(id: 2, name: 'Item2')]
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: new DynamicFieldItem(id: 1))
        def element = builder.buildSelect(dynamicField, items, dynamicFieldValue)

        assertTrue element instanceof UISelectSingle
        assertEquals dynamicField.name, element.label.forId
        assertEquals dynamicField.label, element.label.text
        assertEquals dynamicField.name, element.id
        assertEquals '1', element.value
        assertEquals 2, element.selectItems.size()
        assertTrue element.mandatory
        assertTrue element.options
    }

    void testBuildSelect_NoValue() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label', mandatory: true, type: DynamicFieldType.SingleSelect)
        def items = [new DynamicFieldItem(id: 1, name: 'Item1'), new DynamicFieldItem(id: 2, name: 'Item2')]
        def element = builder.buildSelect(dynamicField, items, null)

        assertTrue element instanceof UISelectSingle
        assertEquals dynamicField.name, element.label.forId
        assertEquals dynamicField.label, element.label.text
        assertEquals dynamicField.name, element.id
        assertEquals '', element.value
        assertEquals 2, element.selectItems.size()
        assertTrue element.mandatory
    }

    void testBuildSelect_SelectBox() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label', type: DynamicFieldType.SingleSelect)
        def items = [
                new DynamicFieldItem(id: 1, name: 'Item1'),
                new DynamicFieldItem(id: 2, name: 'Item2'),
                new DynamicFieldItem(id: 3, name: 'Item3'),
                new DynamicFieldItem(id: 4, name: 'Item4'),
                new DynamicFieldItem(id: 5, name: 'Item5')]
        def element = builder.buildSelect(dynamicField, items, null)

        assertTrue element instanceof UISelectSingle
        assertEquals 5, element.selectItems.size()
        assertFalse element.options
    }

}
