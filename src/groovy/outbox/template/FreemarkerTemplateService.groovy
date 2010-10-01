package outbox.template

import freemarker.template.Template

/**
 * @author Ruslan Khmelyuk
 * @since  2010-10-01
 */
class FreemarkerTemplateService {

    /**
     * Renders content using template and context.
     * @param template the template.
     * @param context the context.
     * @return the rendered content.
     */
    String render(String template, Map context) {
        Template freemarkerTemplate = new Template('Dynamic Template', new StringReader(template))

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(16 * 1024) // reserve 16 k
        Writer out = new OutputStreamWriter(outputStream, 'UTF-8')
        template.process(context, out)

        outputStream.toString('UTF-8')
    }
}
