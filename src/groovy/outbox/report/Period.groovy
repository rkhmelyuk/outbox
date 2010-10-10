package outbox.report

/**
 * @author Ruslan Khmelyuk
 */
public enum Period {

    Hour('hour'),

    Day('day'),

    Week('week'),

    Month('month'),

    Quarter('quarter'),

    Year('year')

    final String sqlPeriod

    Period(String sqlPeriod) {
        this.sqlPeriod = sqlPeriod
    }
}
