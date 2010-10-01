package outbox.template.builder

import org.codehaus.groovy.grails.commons.GrailsApplication
import outbox.tracking.TrackingReference

/**
 * Used to build tracking link.
 *
 * @author Ruslan Khmelyuk
 * @since  2010-10-01
 */
class TrackingLinkBuilder {

    GrailsApplication grailsApplication

    String trackingLink(TrackingReference reference) {
        def serverUrl = grailsApplication.config.grails.serverURL
        return "'${serverUrl}/tracking/${reference.id}'"
    }
}
