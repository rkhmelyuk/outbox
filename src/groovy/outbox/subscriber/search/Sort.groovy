package outbox.subscriber.search

/**
 * @author Ruslan Khmelyuk
 */
public enum Sort {

    Asc('asc'),
    Desc('desc')

    final String keyword

    Sort(String keyword) {
        this.keyword = keyword
    }

}