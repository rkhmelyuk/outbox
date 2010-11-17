package outbox.subscriber.search.criteria

/**
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
