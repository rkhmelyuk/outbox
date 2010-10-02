package outbox.template.builder

/**
 * @author Ruslan Khmelyuk
 * @since  2010-10-02
 */
class TemplateFilterChain implements TemplateFilter {

    List<TemplateFilter> filters

    void add(TemplateFilter filter) {
        if (filter) {
            if (filters == null) {
                filters = []
            }
            filters << filter
        }
    }

    public void filter(TemplateFilterContext context) {
        if (filters) {
            filters.each { it.filter context }
        }
    }
}
