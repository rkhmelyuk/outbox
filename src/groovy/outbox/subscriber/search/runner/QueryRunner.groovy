package outbox.subscriber.search.runner

import outbox.subscriber.search.query.Queries

/**
 * @author Ruslan Khmelyuk
 */
interface QueryRunner {

    void run(Queries queries)

}
