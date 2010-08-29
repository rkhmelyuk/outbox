package outbox.member

import grails.plugins.springsecurity.Secured
import outbox.MessageUtil
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import outbox.dictionary.Timezone
import outbox.dictionary.Language

/**
 * @author Ruslan Khmelyuk
 */
class MemberController {

    static defaultAction = 'list'
    static allowedMethods = [save: 'POST', update: 'POST']

    def springSecurityService

    /**
     * Gets members list.
     */
    @Secured('ROLE_SYSADMIN')
    def list = { MemberSearchCondition condition ->
        
        def criteriaCondition = { builder ->
            if (condition.username) {
                builder.ilike 'username', "%${condition.username}%"
            }
            if (condition.fullName) {
                builder.or {
                    condition.fullName.split(' ').each { part ->
                        part = part.trim()
                        if (part) {
                            builder.ilike 'firstName', "%${part}%"
                            builder.ilike 'lastName', "%${part}%"
                        }
                    }
                }
            }
            if (condition.statusEnabled) {
                builder.eq 'enabled', true
            }
            if (condition.statusAccountExpired) {
                builder.eq 'accountExpired', true
            }
            if (condition.statusAccountLocked) {
                builder.eq 'accountLocked', true
            }
            if (condition.statusPasswordExpired) {
                builder.eq 'passwordExpired', true
            }
        }

        def totalCriteria = Member.createCriteria()
        def total = totalCriteria.count {
            criteriaCondition(totalCriteria)
        }

        def listCriteria = Member.createCriteria()
        def members = listCriteria.list {
            criteriaCondition(listCriteria)
            
            order 'lastName', 'asc'
            order 'firstName', 'asc'

            maxResults condition.itemsPerPage
            firstResult condition.itemsPerPage * (condition.page - 1)
        }

        [total: total, members: members, condition: condition]
    }

    /**
     * Edit specified member.
     */
    @Secured('ROLE_SYSADMIN')
    def edit = {
        def id = params.long('id')
        [member: Member.get(id)]
    }

    /**
     * Update member information.
     */
    @Secured('ROLE_SYSADMIN')
    def update = {
        def model = [:]
        Member member = Member.get(params.long('id'))
        if (member) {
            member.firstName = params.firstName
            member.lastName = params.lastName
            member.email = params.email
            member.language = Language.get(params.int('language'))
            member.timezone = Timezone.get(params.int('timezone'))

            if (params.password || params.passwordConfirmation) {
                if (params.password.equals(params.passwordConfirmation)) {
                    member.password = springSecurityService.encodePassword(params.password)
                }
                else {
                    member.errors.rejectValue('passwordConfirmation', 'wrong.password.confirmation')
                }
            }

            if (member.save()) {
                model << [success: true]
            }
            if (!model.success) {
                MessageUtil.addErrors(request, model, member.errors);
            }
        }

        if (!model.success) {
            model << [error: true]
        }

        render(model.encodeAsJSON())
    }

    /**
     * Create new member.
     */
    def create = {
        
    }

    /**
     * Save new member to storage.
     */
    def save = {

    }

    /**
     * Search conditions
     */
    class MemberSearchCondition {
        int page = 1
        int itemsPerPage = 10

        String fullName
        String username
        boolean statusEnabled
        boolean statusAccountLocked
        boolean statusPasswordExpired
        boolean statusAccountExpired
    }
}
