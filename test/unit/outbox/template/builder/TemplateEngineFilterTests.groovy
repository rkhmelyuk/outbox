package outbox.template.builder

import freemarker.template.Configuration
import grails.test.GrailsUnitTestCase
import outbox.campaign.Campaign
import outbox.campaign.CampaignMessage
import outbox.subscriber.Subscriber
import outbox.template.FreemarkerTemplateService

/**
 * @author Ruslan Khmelyuk
 */
class TemplateEngineFilterTests extends GrailsUnitTestCase {

    void testFilter() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.model.var1 = 'you'
        context.model.var2 = 'surprice'
        context.template = 'Hello ${var1}, look at ${var2}'

        def filter = new TemplateEngineFilter()
        filter.freemarkerTemplateService = new FreemarkerTemplateService()
        filter.filter context

        assertEquals 'Hello you, look at surprice', context.template
    }

    void testFilter_Cyrillic() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.model.var1 = '���'
        context.model.var2 = '�������'
        context.template = '����� ${var1}, ��������� �� ${var2}'

        def filter = new TemplateEngineFilter()
        filter.freemarkerTemplateService = new FreemarkerTemplateService()
        filter.filter context

        assertEquals '����� ���, ��������� �� �������', context.template
    }

    void testFilter_Syntax() {
        def context = new TemplateFilterContext(
                campaign: new Campaign(id: 1),
                message: new CampaignMessage(id: '2'),
                subscriber: new Subscriber(id: '3'))

        context.model.var = 12
        context.template = '<if var == 12>hello</#if>'

        def configuration = new Configuration()
        configuration.strictSyntaxMode = false
        configuration.tagSyntax = Configuration.ANGLE_BRACKET_TAG_SYNTAX

        def templateService = new FreemarkerTemplateService(configuration: configuration)
        def filter = new TemplateEngineFilter(freemarkerTemplateService: templateService)

        filter.filter context

        assertEquals 'hello', context.template
    }
}
