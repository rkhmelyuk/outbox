package outbox.template.builder

import outbox.AppConstant

/**
 * @author Ruslan Khmelyuk
 */
class TemplateTrackingHeaderFilterTests extends GroovyTestCase {

    TemplateTrackingHeaderFilter templateTrackingHeaderFilter

    void testTrackingHeader() {
        def header = templateTrackingHeaderFilter.trackingHeader
        assertTrue header.contains(AppConstant.OPEN_PING_RESOURCE)
    }

}
