package outbox.ui

import java.text.DecimalFormat
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.*
import outbox.ui.element.*

/**
 * Builds form for dynamic fields values editing.
 *
 * @author Ruslan Khmelyuk
 */
class EditDynamicFieldsFormBuilder {
    
    static final String DYNAMIC_FIELD_PREFIX = 'df_'

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
        if (field.maxlength && field.maxlength > 500) {
            element = new UIInputTextArea()
        }
        else {
            element = new UIInputText()
        }
        element.id = DYNAMIC_FIELD_PREFIX + field.name
        element.name = DYNAMIC_FIELD_PREFIX + field.name
        element.label = new UILabel(text: field.label, forId: DYNAMIC_FIELD_PREFIX + field.name)
        element.value = value && value.stringValue ? value.stringValue : ''
        element.maxlength = field.maxlength

        element.mandatory = field.mandatory
        if (element.mandatory) {
            element.styleClass += ' required'
        }

        if (field.maxlength) {
            def styleClass
            if (field.maxlength <= 100) {
                styleClass = 'small'
            }
            else if (field.maxlength <= 250) {
                styleClass = 'normal'
            }
            else if (field.maxlength <= 500) {
                styleClass = 'large'
            }
            else if (field.maxlength <= 1000) {
                styleClass = 'small'
            }
            else if (field.maxlength <= 2000) {
                styleClass = 'normal'
            }
            else {
                styleClass = 'large'
            }
            element.styleClass += ' ' + styleClass
        }
        else {
            element.styleClass += ' normal'
        }

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

        def element = new UIInputText()
        element.id = DYNAMIC_FIELD_PREFIX + field.name
        element.name = DYNAMIC_FIELD_PREFIX + field.name
        element.label = new UILabel(text: field.label, forId: DYNAMIC_FIELD_PREFIX + field.name)
        element.mandatory = field.mandatory
        element.value = numberValue
        element.styleClass += (field.mandatory ? ' required' : '') + ' number'
        element.maxlength = 14
        if (field.min != null) {
            element.args.min = field.min.toPlainString()
            element.styleClass += ' min'
        }
        if (field.max != null) {
            element.args.max = field.max.toPlainString()
            element.styleClass += ' max'
        }

        return element
    }

    UIElement buildBoolean(DynamicField field, DynamicFieldValue value) {
        def label = new UILabel(text: field.label, forId: DYNAMIC_FIELD_PREFIX + field.name)
        new UICheckbox(
                id: DYNAMIC_FIELD_PREFIX + field.name,
                name: DYNAMIC_FIELD_PREFIX + field.name,
                mandatory: false,
                label: label,
                value: value?.booleanValue ?: false)
    }

    UIElement buildSelect(DynamicField field, List<DynamicFieldItem> items, DynamicFieldValue value) {
        def label = new UILabel(text: field.label, forId: DYNAMIC_FIELD_PREFIX + field.name)
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
                id: DYNAMIC_FIELD_PREFIX + field.name,
                name: DYNAMIC_FIELD_PREFIX + field.name,
                mandatory: field.mandatory,
                options: false,
                label: label,
                value: selectValue,
                styleClass: (field.mandatory ? ' required' : ''),
                selectItems: selectItems)
    }
}
