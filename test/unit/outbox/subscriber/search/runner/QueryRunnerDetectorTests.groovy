package outbox.subscriber.search.runner

import grails.test.GrailsUnitTestCase
import outbox.subscriber.search.query.Queries

/**
 * @author Ruslan Khmelyuk
 */
class QueryRunnerDetectorTests extends GrailsUnitTestCase {

    void testDetect() {
        def queryRunner = mockFor(QueryRunner)

        def detector = new QueryRunnerDetector()
        detector.singleQueryRunner = queryRunner.createMock()

        assertEquals detector.singleQueryRunner, detector.detect(new Queries())
    }
}
