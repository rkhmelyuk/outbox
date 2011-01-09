package outbox.subscriber

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.ValueUtil
import outbox.member.Member
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.field.DynamicFieldStatus
import outbox.subscriber.field.DynamicFieldType

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class DynamicFieldController {

    static allowedMethods = [add: 'POST', update: 'POST', delete: 'POST', hide: 'POST']

    DynamicFieldService dynamicFieldService
    SpringSecurityService springSecurityService

    /**
     * Show the list of dynamic fields.
     */
    def index = {
        def member = Member.load(springSecurityService.principal.id)
        def dynamicFields = dynamicFieldService.getDynamicFields(member)

        [dynamicFields: dynamicFields]
    }

    /**
     * Returns rendered template with dynamic fields.
     */
    def dynamicFields = {
        def member = Member.load(springSecurityService.principal.id)
        def dynamicFields = dynamicFieldService.getDynamicFields(member)

        render template: 'dynamicFields', model: [dynamicFields: dynamicFields]
    }

    /**
     * Create dynamic field view.
     */
    def create = {
        [dynamicField: new DynamicField()]
    }

    /**
     * Add dynamic field.
     */
    def add = {
        def dynamicField = new DynamicField()
        dynamicField.label = params.label
        dynamicField.name = params.name
        dynamicField.type = DynamicFieldType.getById(params.int('type'))
        dynamicField.owner = Member.load(springSecurityService.principal.id)
        dynamicField.status = DynamicFieldStatus.Active

        if (dynamicField.type != DynamicFieldType.Boolean) {
            dynamicField.mandatory = params.boolean('mandatory') ?: false
        }
        if (dynamicField.type == DynamicFieldType.String) {
            dynamicField.maxlength = params.int('maxlength')
        }
        else if (dynamicField.type == DynamicFieldType.Number) {
            dynamicField.min = params.min ? params.min as BigDecimal : null
            dynamicField.max = params.max ? params.max as BigDecimal : null
        }

        def model = [:]
        if (dynamicFieldService.addDynamicField(dynamicField)) {
            if (dynamicField.type == DynamicFieldType.SingleSelect) {
                def selectValues = params.list('selectValue') as TreeSet
                def items = selectValues.collect {
                    new DynamicFieldItem(field: dynamicField, name: it?.trim())
                }
                dynamicFieldService.addDynamicFieldItems(dynamicField, items)
            }
            model.dynamicFieldsLink = g.createLink(controller: 'dynamicField', action: 'dynamicFields')
            model.success = true
        }
        else {
            model.error = true
            MessageUtil.addErrors(request, model, dynamicField.errors);
        }

        render model as JSON
    }

    /**
     * Dynamic field edit form.
     */
    def edit = {
        def dynamicField = dynamicFieldService.getDynamicField(params.long('id'))
        if (!dynamicField || !dynamicField.ownedBy(springSecurityService.principal.id)) {
            response.sendError 404
            return
        }
        def dynamicFieldItems = dynamicFieldService.getDynamicFieldItems(dynamicField)
        [dynamicField: dynamicField, dynamicFieldItems: dynamicFieldItems]
    }

    /**
     * Update dynamic field.
     */
    def update = {
        def memberId = springSecurityService.principal.id
        def dynamicField = dynamicFieldService.getDynamicField(params.long('id'))
        if (!dynamicField || !dynamicField.ownedBy(memberId)) {
            response.sendError 404
            return
        }

        dynamicField.label = params.label
        dynamicField.name = params.name
        dynamicField.type = DynamicFieldType.getById(params.int('type'))
        dynamicField.owner = Member.load(memberId)

        def visible = params.boolean('visible') ?: false
        dynamicField.status = visible ? DynamicFieldStatus.Active : DynamicFieldStatus.Hidden

        if (dynamicField.type != DynamicFieldType.Boolean) {
            dynamicField.mandatory = params.boolean('mandatory') ?: false
        }
        if (dynamicField.type == DynamicFieldType.String) {
            dynamicField.maxlength = params.int('maxlength')
            dynamicField.min = null
            dynamicField.max = null
        }
        else if (dynamicField.type == DynamicFieldType.Number) {
            dynamicField.min = params.min ? params.min as BigDecimal : null
            dynamicField.max = params.max ? params.max as BigDecimal : null
            dynamicField.maxlength = null
        }

        def model = [:]
        if (dynamicFieldService.saveDynamicField(dynamicField)) {
            def items = []
            if (dynamicField.type == DynamicFieldType.SingleSelect) {
                def currentItems = dynamicFieldService.getDynamicFieldItems(dynamicField)

                def selectValueIds = params.list('selectValueIds')
                def selectValueLabels = params.list('selectValueLabels')

                def selectValues = []
                for (int i = 0; i < selectValueIds.size(); i++) {
                    def id = ValueUtil.getLong(selectValueIds[i])
                    selectValues << [id: id, label: selectValueLabels[i]?.trim()]
                }

                selectValues.sort { left, right ->
                    left.label <=> right.label
                }

                items = selectValues.collect { value ->
                    def item = currentItems.find { it.id == value.id }
                    if (item) {
                        item.name = value.label
                    }
                    else {
                        item = new DynamicFieldItem(field: dynamicField, name: value.label)
                    }
                    return item
                }
            }
            dynamicFieldService.updateDynamicFieldItems(dynamicField, items)

            model.dynamicFieldsLink = g.createLink(controller: 'dynamicField', action: 'dynamicFields')
            model.success = true
        }
        else {
            model.error = true
            MessageUtil.addErrors(request, model, dynamicField.errors);
        }

        render model as JSON
    }

    def move = {
        def fieldId = params.long('fieldId')
        def afterFieldId = params.long('afterFieldId')

        def field = dynamicFieldService.getDynamicField(fieldId)
        def afterField = afterFieldId ? dynamicFieldService.getDynamicField(afterFieldId) : null
        def memberId = springSecurityService.principal.id

        def result
        if (!field || !field.ownedBy(memberId)) {
            result = false
        }
        else if (afterField && !afterField.ownedBy(memberId)) {
            result = false
        }
        else {
            def newPosition = (afterField?.sequence ?: 0)
            result = dynamicFieldService.moveDynamicField(field, newPosition)
        }

        render([success: result] as JSON)
    }

    def remove = {
        def fieldId = params.long('fieldId')
        def field = dynamicFieldService.getDynamicField(fieldId)
        def memberId = springSecurityService.principal.id

        def model = [:]
        if (!field || !field.ownedBy(memberId)) {
            model.success = false
        }
        else {
            model.success = dynamicFieldService.deleteDynamicField(field)
            model.dynamicFieldsLink = g.createLink(controller: 'dynamicField', action: 'dynamicFields')
        }

        render(model as JSON)
    }

    def hide = {
        def fieldId = params.long('fieldId')
        def field = dynamicFieldService.getDynamicField(fieldId)
        def memberId = springSecurityService.principal.id

        def model = [:]
        if (!field || !field.ownedBy(memberId)) {
            model.success = false
        }
        else {
            model.success = dynamicFieldService.hideDynamicField(field)
            model.dynamicFieldsLink = g.createLink(controller: 'dynamicField', action: 'dynamicFields')
        }

        render(model as JSON)
    }
}
