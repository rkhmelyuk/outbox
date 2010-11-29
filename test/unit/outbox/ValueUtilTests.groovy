package outbox

/**
 * @author Ruslan Khmelyuk
 */
class ValueUtilTests extends GroovyTestCase {

    void testBeginDate() {
        def testDate = new Date()
        def date = ValueUtil.beginDate(testDate)
        assertEquals testDate.year, date.year
        assertEquals testDate.month, date.month
        assertEquals testDate.date, date.date
    }

    void testBeginDate_Null() {
        def date = ValueUtil.beginDate(null)
        assertEquals 0, date.year
        assertEquals 0, date.month
        assertEquals 1, date.date
    }

    void testEndDate() {
        def testDate = new Date()
        def date = ValueUtil.endDate(testDate)
        assertEquals testDate.year, date.year
        assertEquals testDate.month, date.month
        assertEquals testDate.date, date.date
    }

    void testEndDate_Null() {
        def date = ValueUtil.endDate(null)
        assertEquals 1000, date.year
        assertEquals 11, date.month
        assertEquals 31, date.date
    }

    void testGetBigDecimal() {
        assertEquals 0, ValueUtil.getBigDecimal('0')
        assertEquals 2.2, ValueUtil.getBigDecimal('2.2')
        assertNull ValueUtil.getBigDecimal('')
        assertNull ValueUtil.getBigDecimal('asf')
    }

    void testLong() {
        assertEquals 0, ValueUtil.getLong('0')
        assertEquals 2, ValueUtil.getLong('2')
        assertNull ValueUtil.getLong('')
        assertNull ValueUtil.getLong('asf')
    }

    void testInteger() {
        assertEquals 0, ValueUtil.getInteger('0')
        assertEquals 2, ValueUtil.getInteger('2')
        assertNull ValueUtil.getInteger('')
        assertNull ValueUtil.getInteger('asf')
    }
}
