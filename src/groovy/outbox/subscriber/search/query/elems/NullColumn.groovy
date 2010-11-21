package outbox.subscriber.search.query.elems

/**
 * Represents select column that returns NULL value.
 *
 * @author Ruslan Khmelyuk
 */
class NullColumn extends Column {

    NullColumn(String alias = null) {
        super((String) null, 'null', alias)
    }
}
