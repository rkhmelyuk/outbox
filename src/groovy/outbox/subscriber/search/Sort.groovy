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

    static Sort getByKeyword(String keyword) {
        if (keyword == 'asc') {
            return Asc
        }
        else if (keyword == 'desc') {
            return Desc
        }
        return null
    }

}