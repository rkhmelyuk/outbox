package outbox.ui

import outbox.subscriber.field.*
import outbox.ui.element.*

/**
 * Builds form for dynamic fields values editing.
 *
 * @author Ruslan Khmelyuk
 */
class EditDynamicFieldsFormBuilder {

    UIContainer build(DynamicFieldValues values) {
        def container = new UIContainer()

        values.fields.each { field ->
            def uiElement = null
            def value = values.value(field)

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
        def element
        if (field.maxlength && field.maxlength > 200) {
            element = new UIInputTextArea()
        }
        else {
            element = new UIInputText()
        }
        element.id = field.name
        element.name = field.name
        element.label = new UILabel(text: field.label, forId: field.name)
        element.mandatory = field.mandatory
        element.value = value.value
        element.maxlength = field.maxlength

        if (field.maxlength) {
            def styleClass
            if (field.maxlength <= 50) {
                styleClass = 'small'
            }
            else if (field.maxlength <= 100) {
                styleClass = 'normal'
            }
            else if (field.maxlength <= 200) {
                styleClass = 'large'
            }
            else if (field.maxlength <= 500) {
                styleClass = 'small'
            }
            else if (field.maxlength <= 2000) {
                styleClass = 'normal'
            }
            else {
                styleClass = 'large'
            }
            element.styleClass = styleClass
        }

        return element
    }

    UIElement buildNumber(DynamicField field, DynamicFieldValue value) {
        def numberValue = null
        if (value) {
            numberValue = value.value?.toString()
        }
        if (!numberValue) {
            numberValue = ''
        }

        def element = new UIInputText()
        element.id = field.name
        element.name = field.name
        element.label = new UILabel(text: field.label, forId: field.name)
        element.mandatory = field.mandatory
        element.value = numberValue
        element.styleClass = 'number'
        element.maxlength = 14
        return element
    }

    UIElement buildBoolean(DynamicField field, DynamicFieldValue value) {
        def label = new UILabel(text: field.label, forId: field.name)
        new UICheckbox(
                id: field.name,
                mandatory: field.mandatory,
                label: label,
                value: value?.value ?: false)
    }

    UIElement buildSelect(DynamicField field, List<DynamicFieldItem> items, DynamicFieldValue value) {
        def label = new UILabel(text: field.label, forId: field.name)
        def selectItems = items.collect { item ->
            new SelectItem(key: item.id, value: item.name)
        }

        def selectValue
        if (value) {
            selectValue = value.value?.id
            selectValue = selectValue ? Long.toString(selectValue) : ''
        }
        else {
            selectValue = ''
        }

        new UISelectSingle(
                id: field.name,
                mandatory: field.mandatory,
                options: selectItems.size() <= 3,
                label: label,
                value: selectValue,
                selectItems: selectItems)
    }
}
