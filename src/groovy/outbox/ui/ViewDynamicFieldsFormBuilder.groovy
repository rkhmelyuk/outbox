package outbox.ui

import java.text.DecimalFormat
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldType
import outbox.subscriber.field.DynamicFieldValue
import outbox.subscriber.field.DynamicFieldValues
import outbox.ui.element.UIContainer
import outbox.ui.element.UIElement
import outbox.ui.element.UILabel
import outbox.ui.element.UIOutput

/**
 * Builds form for dynamic fields values view.
 *
 * @author Ruslan Khmelyuk
 */
class ViewDynamicFieldsFormBuilder {

    static final String DYNAMIC_FIELD_PREFIX = EditDynamicFieldsFormBuilder.DYNAMIC_FIELD_PREFIX

    UIContainer build(DynamicFieldValues values) {
        def container = new UIContainer()

        values.fields.each { field ->
            def uiElement = null
            def value = values.get(field)

            if (field.type == DynamicFieldType.String) {
                uiElement = buildString(field, value)
            }
            else if (field.type == DynamicFieldType.Number) {
                uiElement = buildNumber(field, value)
            }
            else if (field.type == DynamicFieldType.Boolean) {
                uiElement = buildBoolean(field, value)
            }
            else if (field.type == DynamicFieldType.SingleSelect) {
                uiElement = buildSelect(field, value)
            }

            if (uiElement) {
                container.elements << uiElement
            }
        }

        return container
    }

    UIElement buildString(DynamicField field, DynamicFieldValue value) {
        def element = new UIOutput()

        element.id = DYNAMIC_FIELD_PREFIX + field.name
        element.label = new UILabel(text: field.label, forId: DYNAMIC_FIELD_PREFIX + field.name)
        element.text = value && value.stringValue ? value.stringValue : ''

        return element
    }

    UIElement buildNumber(DynamicField field, DynamicFieldValue value) {
        def numberValue = null
        if (value && value.numberValue) {
            numberValue = new DecimalFormat('###########.####').format(value.numberValue)
        }
        if (!numberValue) {
            numberValue = ''
        }

        def element = new UIOutput()
        element.id = DYNAMIC_FIELD_PREFIX + field.name
        element.label = new UILabel(text: field.label, forId: DYNAMIC_FIELD_PREFIX + field.name)
        element.text = numberValue

        return element
    }

    UIElement buildBoolean(DynamicField field, DynamicFieldValue value) {
        def label = new UILabel(text: field.label, forId: DYNAMIC_FIELD_PREFIX + field.name)
        new UIOutput(id: DYNAMIC_FIELD_PREFIX + field.name, label: label,
                styleClass: (field.mandatory ? ' required' : ''),
                text: value?.booleanValue ? 'Yes' : 'No')
    }

    UIElement buildSelect(DynamicField field, DynamicFieldValue value) {
        def label = new UILabel(text: field.label, forId: DYNAMIC_FIELD_PREFIX + field.name)

        def selectValue
        if (value && value.singleItem) {
            selectValue = value.singleItem.name
        }
        else {
            selectValue = ''
        }

        new UIOutput(
                id: DYNAMIC_FIELD_PREFIX + field.name,
                label: label,
                text: selectValue)
    }

}
