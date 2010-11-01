package outbox.subscriber

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.member.Member
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.field.DynamicFieldType

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class DynamicFieldController {

    static allowedMethods = [add: 'POST', update: 'POST', delete: 'POST']

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
        dynamicField.mandatory = params.boolean('mandatory') ?: false

        if (dynamicField.type == DynamicFieldType.String) {
            dynamicField.maxlength = params.int('maxlength')
        }
        else if (dynamicField.type == DynamicFieldType.Number) {
            dynamicField.min = params.int('min')
            dynamicField.max = params.int('max')
        }

        def model = [:]
        if (dynamicFieldService.addDynamicField(dynamicField)) {
            if (dynamicField.type == DynamicFieldType.SingleSelect) {
                def selectValues = params.list('singleSelect') as TreeSet
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
        dynamicField.mandatory = params.boolean('mandatory') ?: false

        if (dynamicField.type == DynamicFieldType.String) {
            dynamicField.maxlength = params.int('maxlength')
            dynamicField.min = null
            dynamicField.max = null
        }
        else if (dynamicField.type == DynamicFieldType.Number) {
            dynamicField.min = params.int('min')
            dynamicField.max = params.int('max')
            dynamicField.maxlength = null
        }

        def model = [:]
        if (dynamicFieldService.saveDynamicField(dynamicField)) {
            def items = []
            if (dynamicField.type == DynamicFieldType.SingleSelect) {
                def currentItems = dynamicFieldService.getDynamicFieldItems(dynamicField)
                def selectValues = params.list('selectValue') as TreeSet
                items = selectValues.collect { name ->
                    def item = currentItems.find { it.name == name }
                    item ?: new DynamicFieldItem(field: dynamicField, name: name)
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

    def reOrderField = {
        def fieldId = params.long('fieldId')
        def afterFieldId = params.long('afterFieldId')

        def field = dynamicFieldService.getDynamicField(fieldId)
        def afterField = afterFieldId ? dynamicFieldService.getDynamicField(afterFieldId) : null

        def memberId = springSecurityService.principal.id
        if (!field || !field.ownedBy(memberId)) {
            response.sendError 404
            return
        }
        if (afterField && !afterField.ownedBy(memberId)) {
            response.sendError 404
            return
        }

        def newPosition = (afterField?.sequence ?: 0)
        def result = dynamicFieldService.moveDynamicField(field, newPosition)

        render([success: result] as JSON)
    }
}
