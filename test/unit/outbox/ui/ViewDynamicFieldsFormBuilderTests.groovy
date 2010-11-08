package outbox.ui

import grails.test.GrailsUnitTestCase
import outbox.ui.element.UIOutput
import outbox.subscriber.field.*

/**
 * @author Ruslan Khmelyuk
 */
class ViewDynamicFieldsFormBuilderTests extends GrailsUnitTestCase {
    
    static final String DYNAMIC_FIELD_PREFIX = ViewDynamicFieldsFormBuilder.DYNAMIC_FIELD_PREFIX

    ViewDynamicFieldsFormBuilder builder

    @Override protected void setUp() {
        super.setUp()

        builder = new ViewDynamicFieldsFormBuilder()
    }

    void testBuild() {
        def field1 = new DynamicField(name: 'field1', label: 'Label', type: DynamicFieldType.String)
        def field2 = new DynamicField(name: 'field2', label: 'Label', type: DynamicFieldType.Number)
        def field3 = new DynamicField(name: 'field3', label: 'Label', type: DynamicFieldType.Boolean)
        def field4 = new DynamicField(name: 'field4', label: 'Label', type: DynamicFieldType.SingleSelect)

        def value1 = new DynamicFieldValue(dynamicField: field1)
        def value2 = new DynamicFieldValue(dynamicField: field2)
        def value3 = new DynamicFieldValue(dynamicField: field3)
        def value4 = new DynamicFieldValue(dynamicField: field3)

        def values = new DynamicFieldValues(
                [field1, field2, field3, field4],
                [value1, value2, value3, value4])

        def elements = builder.build(values)

        assertEquals 4, elements.elements.size()
        assertEquals DYNAMIC_FIELD_PREFIX + 'field1', elements.elements[0].id
        assertEquals DYNAMIC_FIELD_PREFIX + 'field2', elements.elements[1].id
        assertEquals DYNAMIC_FIELD_PREFIX + 'field3', elements.elements[2].id
        assertEquals DYNAMIC_FIELD_PREFIX + 'field4', elements.elements[3].id
    }

    void testBuildString() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.String, maxlength: 200)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: 'test string')
        def element = builder.buildString(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UIOutput
        assertEquals dynamicField.label, element.label.text
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.label.forId
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.id
        assertEquals 'test string', element.text
    }

    void testBuildString_Area() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.String, maxlength: 1200)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: 'test string')
        def element = builder.buildString(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UIOutput
        assertEquals dynamicField.label, element.label.text
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.label.forId
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.id
        assertEquals 'test string', element.text
    }

    void testBuildString_NoValue() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.String, maxlength: 400)
        def element = builder.buildString(dynamicField, null)

        assertTrue element instanceof UIOutput
        assertEquals dynamicField.label, element.label.text
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.label.forId
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.id
        assertEquals '', element.text
    }

    void testBuildNumber() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.Number, maxlength: 200, min: 0, max: 2000)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: 123)
        def element = builder.buildNumber(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UIOutput
        assertEquals dynamicField.label, element.label.text
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.label.forId
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.id
        assertEquals '123', element.text
    }

    void testBuildBoolean() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.Boolean)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField, value: true)
        def element = builder.buildBoolean(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UIOutput
        assertEquals dynamicField.label, element.label.text
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.label.forId
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.id
        assertEquals 'Yes', element.text
    }

    void testBuildBoolean_NoValue() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label', type: DynamicFieldType.Boolean)
        def element = builder.buildBoolean(dynamicField, null)

        assertTrue element instanceof UIOutput
        assertEquals dynamicField.label, element.label.text
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.label.forId
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.id
        assertEquals 'No', element.text
    }

    void testBuildSelect() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label',
                mandatory: true, type: DynamicFieldType.SingleSelect)
        def dynamicFieldValue = new DynamicFieldValue(dynamicField: dynamicField,
                value: new DynamicFieldItem(id: 1, name: 'Item1'))
        def element = builder.buildSelect(dynamicField, dynamicFieldValue)

        assertTrue element instanceof UIOutput
        assertEquals dynamicField.label, element.label.text
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.label.forId
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.id
        assertEquals 'Item1', element.text
    }

    void testBuildSelect_NoValue() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label', mandatory: true, type: DynamicFieldType.SingleSelect)
        def element = builder.buildSelect(dynamicField, null)

        assertTrue element instanceof UIOutput
        assertEquals dynamicField.label, element.label.text
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.label.forId
        assertEquals DYNAMIC_FIELD_PREFIX + dynamicField.name, element.id
        assertEquals '', element.text
    }

    void testBuildSelect_SelectBox() {
        def dynamicField = new DynamicField(name: 'name', label: 'Label', type: DynamicFieldType.SingleSelect)
        def items = [
                new DynamicFieldItem(id: 1, name: 'Item1'),
                new DynamicFieldItem(id: 2, name: 'Item2'),
                new DynamicFieldItem(id: 3, name: 'Item3'),
                new DynamicFieldItem(id: 4, name: 'Item4'),
                new DynamicFieldItem(id: 5, name: 'Item5')]
        def element = builder.buildSelect(dynamicField, null)

        assertTrue element instanceof UIOutput
    }

}
