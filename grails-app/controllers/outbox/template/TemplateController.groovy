package outbox.template

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import outbox.MessageUtil
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
@Secured(['ROLE_CLIENT'])
class TemplateController {

    private static final int ITEMS_PER_PAGE = 9

    def defaultAction = 'list'
    def allowedMethods = [add: 'POST', update: 'POST', templatesPage: 'GET']

    TemplateService templateService
    SpringSecurityService springSecurityService

    /**
     * Gets the page with list of first 10 templates. Next templates should
     * be loaded with {@code templatesPage()} action.
     */
    def list = {
        def member = Member.load(springSecurityService.principal.id)
        def templates = templateService.getMemberTemplates(member, 1, ITEMS_PER_PAGE)
        
        [templates: templates, nextPage: (templates.size() == ITEMS_PER_PAGE ? 2 : null)]
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
        def templates = templateService.getMemberTemplates(member, page, ITEMS_PER_PAGE)

        def model = [:]

        model.content = g.render(template: 'templateListRecords', collection: templates, var: 'template')
        if (templates && templates.size() == ITEMS_PER_PAGE) {
            model.nextPage = g.createLink(action: 'templatesPage', params: [page: page + 1])
        }

        render model as JSON
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
            def campaignId = params.long('campaign')
            if (campaignId) {
                model.redirectTo = g.createLink(controller: 'campaign', action: 'show', id: campaignId,
                        params: [page: 'template', template: template.id])
            }
            else {
                model.redirectTo = g.createLink(controller: 'template', action: 'show', id: template.id)
            }
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
        def model = [:]
        def template = templateService.getTemplate(params.long('id'))
        if (template && template.ownedBy(springSecurityService.principal.id)) {
            template.name = params.name
            template.description = params.description
            template.templateBody = params.templateBody

            if (templateService.saveTemplate(template)) {
                model.success = true
            }
            else {
                MessageUtil.addErrors(request, model, template.errors);
            }
        }
        
        if (!model.success) {
            model.error = true
        }

        render model as JSON
    }
}
