package outbox.template.builder

/**
 * NOTE: must go before {@link TemplateEngineFilter} and {@link TemplateImageFilter}
 *
 * @author Ruslan Khmelyuk
 */
class TemplateTrackingHeaderFilter implements TemplateFilter {

    String trackingHeader

    void filter(TemplateFilterContext context) {
        context.template = trackingHeader + context.template 
    }

}
