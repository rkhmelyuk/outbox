package outbox.template.builder

import outbox.template.FreemarkerTemplateService

/**
 * Use template engine to prepare the template.
 *
 * @author Ruslan Khmelyuk
 * @since  2010-10-01
 */
class TemplateEngineFilter implements TemplateFilter {

    FreemarkerTemplateService freemarkerTemplateService

    void filter(TemplateFilterContext context) {
        def template = freemarkerTemplateService.render(context.template, context.model)
        context.template = template
    }

}
