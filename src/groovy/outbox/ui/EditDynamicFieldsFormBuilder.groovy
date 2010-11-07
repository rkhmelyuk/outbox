package outbox.ui

import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.*
import outbox.ui.element.*

/**
 * Builds form for dynamic fields values editing.
 *
 * @author Ruslan Khmelyuk
 */
class EditDynamicFieldsFormBuilder {

    DynamicFieldService dynamicFieldService

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
                def items = dynamicFieldService.getDynamicFieldItems(field)
                uiElement = buildSelect(field, items, value)
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
        element.value = value && value.stringValue ? value.stringValue : ''
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
        if (value && value.numberValue) {
            numberValue = value.numberValue.toPlainString()
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
                value: value?.booleanValue ?: false)
    }

    UIElement buildSelect(DynamicField field, List<DynamicFieldItem> items, DynamicFieldValue value) {
        def label = new UILabel(text: field.label, forId: field.name)
        def selectItems = items.collect { item ->
            new SelectItem(value: item.id, label: item.name)
        }

        def selectValue
        if (value && value.singleItem) {
            selectValue = Long.toString(value.singleItem.id)
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
