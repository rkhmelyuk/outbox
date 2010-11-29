package outbox

/**                                                                                                        l
 * @author Ruslan Khmelyuk
 */
class ValueUtil {

    static Integer getInteger(def value, Integer defaultValue = null) {
        if (value instanceof Integer) {
            return value
        }

        Integer result = null
        try {
            result = Integer.valueOf(value)
        }
        catch (Exception e) {
            // skip it
        }

        result != null ? result : defaultValue
    }

    static Long getLong(def value, Long defaultValue = null) {
        if (value instanceof Integer) {
            return value
        }

        Long result = null
        try {
            result = Long.valueOf(value)
        }
        catch (Exception e) {
            // skip it
        }

        result != null ? result : defaultValue
    }

    static BigDecimal getBigDecimal(String string, BigDecimal defaultValue = null) {
        BigDecimal result = null
        try {
            result = new BigDecimal(string)
        }
        catch (Exception e) {
            // skip it
        }

        result != null ? result : defaultValue
    }

    static Date beginDate(Date startDate) {
        startDate ?: new Date(0, 0, 1)
    }

    static Date endDate(Date endDate) {
        endDate ?: new Date(1000, 11, 31)
    }
}
