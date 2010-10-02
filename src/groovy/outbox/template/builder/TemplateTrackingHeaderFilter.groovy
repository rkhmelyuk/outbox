package outbox.template.builder

/**
 * NOTE: must go before {@link TemplateEngineFilter} and {@link TemplateImageFilter}
 *
 * @author Ruslan Khmelyuk
 */
class TemplateTrackingHeaderFilter implements TemplateFilter {

    String trackingHeader

    void filter(TemplateFilterContext context) {
        def index = indexOf(context.template, '</body>', '</html>')
        if (index != -1) {
            def builder = new StringBuilder(context.template)
            builder.insert(index, trackingHeader)
            context.template = builder.toString()
        }
        else {
            context.template = trackingHeader + context.template
        }
    }

    private int indexOf(String body, String... tags) {
        for (String tag in tags) {
            def index = body.indexOf(tag)
            if (index != -1) {
                return index
            }
        }
        return -1
    }

}
