package outbox.member

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import outbox.MessageUtil
import outbox.dictionary.Language
import outbox.dictionary.Timezone

/**
 * View/edit own profile.
 *
 * @author Ruslan Khmelyuk
 */
@Secured('ROLE_USER')
class ProfileController {

    static defaultAction = 'edit'
    static allowedMethods = [saveProfile: 'POST', saveNewPassword: 'POST']

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

            if (member.save()) {
                SpringSecurityUtils.reauthenticate(member.username, member.password)
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
                errors << ['newPassword': message(code:'new.password.required') ]
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
                        errors << ['newPassword': message(code:'change.password.error')]
                    }
                }
                else {
                    errors << ['oldPassword': message(code:'wrong.old.password')]
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
