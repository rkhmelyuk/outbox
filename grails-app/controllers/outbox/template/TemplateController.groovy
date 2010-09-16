package outbox.template

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class TemplateController {

    def defaultAction = ''
    def allowedMethods = [add: 'POST', update: 'POST', templatesPage: 'GET']

    TemplateService templateService
    SpringSecurityService springSecurityService

    /**
     * Gets the page with list of first 10 templates. Next templates should
     * be loaded with {@code templatesPage()} action.
     */
    def list = {
        def member = Member.load(springSecurityService.principal.id)
        def templates = templateService.getMemberTemplates(member, 1, 10)
        
        [templates: templates]
    }

    /**
     * Gets the page of templates.
     */
    def templatesPage = {
        def page = params.int('page')
        if (page == null || page <= 0) {
            page = 1
        }

        def member = Member.load(springSecurityService.principal.id)
        def templates = templateService.getMemberTemplates(member, page, 10)

        [templates: templates]
    }

    def show = {
        def template = templateService.getTemplate(params.long('id'))
        if (!template) {
            response.sendError 404
            return
        }
        if (!template.ownedBy(springSecurityService.principal.id)) {
            response.sendError 403
            return
        }

        [template: template]
    }

    def create = {
        [template : new Template()]
    }

    def add = {
        def template = new Template()
        template.name = params.name
        template.description = params.description
        template.templateBody = params.templateBody
        template.owner = Member.load(springSecurityService.principal.id)

        def model = [:]
        if (templateService.addTemplate(template)) {
            model.success = true
            model.redirectTo = g.createLink(controller: 'template')
        }
        else {
            model.error = true
            MessageUtil.addErrors(request, model, template.errors);
        }

        render model as JSON
    }

    def edit = {
        def template = templateService.getTemplate(params.long('id'))
        if (!template) {
            response.sendError 404
            return
        }
        if (!template.ownedBy(springSecurityService.principal.id)) {
            response.sendError 403
            return
        }
        
        [template: template]
    }

    def update = {

    }
}
