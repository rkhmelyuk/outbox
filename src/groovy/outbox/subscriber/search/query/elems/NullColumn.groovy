package outbox.subscriber.search.query.elems

/**
 * @author Ruslan Khmelyuk
 */
class NullColumn extends Column {

    NullColumn(String alias = null) {
        super((String) null, 'null', alias)
    }
}
