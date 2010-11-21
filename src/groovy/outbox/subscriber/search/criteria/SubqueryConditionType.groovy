package outbox.subscriber.search.criteria

/**
 * Subquery condition types.
 * Currently supported only Exists and Not Exists
 *
 * @author Ruslan Khmelyuk
 */
enum SubqueryConditionType {
    Exists('exists'),
    NotExists('not exists')

    final String keyword

    SubqueryConditionType(String keyword) {
        this.keyword = keyword
    }
}
