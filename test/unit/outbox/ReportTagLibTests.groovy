package outbox

import grails.test.TagLibUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class ReportTagLibTests extends TagLibUnitTestCase {

    @Override protected void setUp() {
        super.setUp()

        tagLib.class.metaClass.message = { map ->
            return map.code
        }
    }

    void testOpenedNotOpened() {
        def attrs = [container: 'container', opened: 10, notOpened: 20]
        def result = tagLib.openedNotOpened(attrs).toString()
        assertEquals 'Report.openedNotOpened(\'container\', [["opened",10],["notOpened",20]]);', result
    }

    void testOpensClicks_WithData() {
        def date = new Date(110, 9, 14, 10, 0)
        def opens = [[date: date-2, opens: 10], [date: date-1, opens: 20]]
        def clicks = [[date: date-3, clicks: 20], [date: date-1, clicks: 40]]
        def attrs = [container: 'container', opens: opens, clicks: clicks]
        def result = tagLib.opensClicks(attrs).toString()
        assertEquals 'Report.opensClicks(\'container\', ["_","11 Mon, 10:00","12 Tue, 10:00","13 Wed, 10:00"], ' +
                '[{"name":"opens","data":[0,0,10,20]},{"name":"clicks","data":[0,20,0,40]}]);', result
    }

    void testOpensClicks_WithoutData() {
        def opens = []
        def clicks = []
        def attrs = [container: 'container', opens: opens, clicks: clicks]
        def result = tagLib.opensClicks(attrs).toString()
        assertEquals 'Report.opensClicks(\'container\', ["_"], ' +
                '[{"name":"opens","data":[0]},{"name":"clicks","data":[0]}]);', result
    }
}
