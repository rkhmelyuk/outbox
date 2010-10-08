package outbox

/**
 * @author Ruslan Khmelyuk
 */
class ValueUtil {

    static Date beginDate(Date startDate) {
        startDate ?: new Date(0, 0, 1)
    }

    static Date endDate(Date endDate) {
        endDate ?: new Date(1000, 11, 31)
    }
}
