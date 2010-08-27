package outbox.user

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import outbox.MessageUtil
import outbox.dictionary.Language
import outbox.dictionary.Timezone
import outbox.member.Member

/**
 * View/edit own profile.
 *
 * @author Ruslan Khmelyuk
 */
@Secured('ROLE_USER')
class ProfileController {

    static defaultAction = 'edit'

    SpringSecurityService springSecurityService

    /**
     * Edit user profile page.
     */
    def edit = {
        def principal = springSecurityService.getPrincipal()
        return [member: Member.get(principal.id)]
    }

    /**
     * Save user profile changes.
     */
    def saveProfile = {
        def model = [:]
        def principal = springSecurityService.getPrincipal()
        Member member = Member.get(principal.id)
        if (member) {
            member.firstName = params.firstName
            member.lastName = params.lastName
            member.email = params.email
            member.language = Language.get(params.int('language'))
            member.timezone = Timezone.get(params.int('timezone'))

            if (member.validate()) {
                if (member.save()) {
                    SpringSecurityUtils.reauthenticate(member.username, member.password)
                    model << [success: true]
                }
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
     * Change password page.
     */
    def newPassword = { }

    /**
     * Saves new password for the user.
     */
    def saveNewPassword = {
        def model = [:]
        def errors = [:]
        def principal = springSecurityService.getPrincipal()
        Member member = Member.get(principal.id)
        if (member) {
            if (!newPassword) {
                errors << ['newPassword': MessageUtil.getMessage('new.password.required', null, request) ]
            }
            else {
                String currentPasswordHash = springSecurityService.encodePassword(params.oldPassword)
                if (member.password.equals(currentPasswordHash)) {
                    member.password = springSecurityService.encodePassword(params.newPassword)
                    if (member.save()) {
                        SpringSecurityUtils.reauthenticate(member.username, member.password)
                        model << [success: true]
                    }
                    else {
                        errors << ['newPassword': MessageUtil.getMessage('change.password.error', null, request)]
                    }
                }
                else {
                    errors << ['oldPassword': MessageUtil.getMessage('wrong.old.password', null, request)]
                }
            }
        }

        if (!model.success) {
            model << [error: true]
            model << [errors: errors]
        }

        render(model.encodeAsJSON())
    }
}
