package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
class ReportUtil {

    /**
     * Gets the best period to show results before two dates,
     * for example, between campaign start date and today.
     * <br/>
     * If end date is null, then current date will be used.
     *
     * @param start the start date, can't be null.
     * @param end the end date.
     * @return the best fit period
     */
    static Period bestPeriod(Date start, Date end = null) {
        end = end ?: new Date()

        int days = end - start
        if (days <= 1) { // up to 1 day
            return Period.Hour
        }
        else if (days <= 3 * 7) { // up to 3 weeks
            return Period.Day
        }
        else if (days < 2 * 30) { // up to 2 month
            return Period.Week
        }
        else if (days < 24 * 30) { // up to 2 years
            return Period.Month
        }
        else {
            return Period.Year
        }
    }
}
