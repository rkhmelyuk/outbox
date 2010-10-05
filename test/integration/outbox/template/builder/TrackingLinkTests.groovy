package outbox.template.builder

import grails.test.GrailsUnitTestCase
import org.codehaus.groovy.grails.commons.GrailsApplication
import outbox.tracking.TrackingReference

/**
 * @author Ruslan Khmelyuk
 * @since 2010-10-02
 */
class TrackingLinkTests extends GrailsUnitTestCase {

    GrailsApplication grailsApplication

    void testTrackingLink() {
        def trackingLinkBuilder = new TrackingLinkBuilder()
        trackingLinkBuilder.grailsApplication = grailsApplication
        
        def ref = new TrackingReference()
        ref.id = 'abcdef'
        assertEquals 'abcdef', ref.id
        assertEquals "http://localhost:8080/outbox/tracking/abcdef",
                trackingLinkBuilder.trackingLink(ref)
    }
}
