package outbox.subscriber.search

/**
 * The information about ordering.
 * 
 * @author Ruslan Khmelyuk
 */
class FieldOrder {

    final String column
    final Sort sort

    FieldOrder(String column, Sort sort) {
        this.column = column
        this.sort = sort
    }
}
