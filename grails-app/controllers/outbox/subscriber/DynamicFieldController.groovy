package outbox.subscriber

import grails.plugins.springsecurity.SpringSecurityService
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldController {

    static allowedMethods = [updateDynamicField: 'POST', addDynamicField: 'POST', deleteDynamicField: 'POST']

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
}
