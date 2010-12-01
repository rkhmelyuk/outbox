package outbox.subscriber.search

import outbox.subscriber.Subscriber

/**
 * The result of subscribers search.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-11-14
 */
class Subscribers {

    Integer page
    Integer perPage
    long total

    List<Subscriber> list
}
