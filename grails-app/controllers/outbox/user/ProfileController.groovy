package outbox.user

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.member.Member
import outbox.dictionary.Language
import outbox.dictionary.Timezone
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import outbox.MessageUtil

/**
 * View/edit own profile.
 * 
 * @author Ruslan Khmelyuk
 */
@Secured('ROLE_USER')
class ProfileController {

    static defaultAction = 'edit'

    SpringSecurityService springSecurityService

    def edit = {
        def principal = springSecurityService.getPrincipal()
        return [member: Member.get(principal.id)]
    }

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
}
