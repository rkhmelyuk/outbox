package outbox.tracking

import grails.test.ControllerUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class TrackingControllerTests extends ControllerUnitTestCase {

    void testTrack() {
        def trackingServiceControl = mockFor(TrackingService)
        trackingServiceControl.demand.getTrackingReference { id ->
            assertEquals 'abcd0123', id
            return new TrackingReference(id: '123123', reference: 'test')
        }
        trackingServiceControl.demand.track { RawTrackingInfo raw ->
            assertEquals '123123', raw.reference.id
            assertEquals 'user agent header', raw.userAgentHeader
            assertEquals 'accept language header', raw.acceptLanguageHeader
            assertEquals '127.1.1.1', raw.remoteAddress
            assertEquals 'cube', raw.remoteHost
            assertEquals 'guest', raw.remoteUser
        }
        controller.trackingService = trackingServiceControl.createMock()

        mockRequest.addHeader 'User-Agent', 'user agent header'
        mockRequest.addHeader 'Accept-Language', 'accept language header'
        mockRequest.remoteAddr = '127.1.1.1'
        mockRequest.remoteHost = 'cube'
        mockRequest.remoteUser = 'guest'

        controller.params.id = 'abcd0123'
        
        controller.track()

        assertEquals 'test', controller.redirectArgs.url

        trackingServiceControl.verify()
    }

    void testTrack_Null() {
        def trackingServiceControl = mockFor(TrackingService)
        trackingServiceControl.demand.getTrackingReference { id ->
            assertEquals 'abcd0123', id
            return null
        }
        controller.trackingService = trackingServiceControl.createMock()

        controller.class.metaClass.getGrailsApplication {
            return [config: [outbox: [application: [url: 'test']]]]
        }

        controller.params.id = 'abcd0123'
        controller.track()

        assertEquals 'test', controller.redirectArgs.url

        trackingServiceControl.verify()
    }
}
