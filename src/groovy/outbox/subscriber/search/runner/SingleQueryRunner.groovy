package outbox.subscriber.search.runner

import org.apache.log4j.Logger
import org.hibernate.SessionFactory
import outbox.subscriber.Subscriber
import outbox.subscriber.search.Subscribers
import outbox.subscriber.search.query.Queries

/**
 * Build single query and run it.
 * 
 * @author Ruslan Khmelyuk
 */
class SingleQueryRunner implements QueryRunner {

    static final Logger log = Logger.getLogger(SingleQueryRunner)

    SessionFactory sessionFactory

    Subscribers run(Queries queries) {
        def sql = queries.subscriberFieldQuery.toSQL()
        log.info sql

        def session = sessionFactory.currentSession
        def query = session.createSQLQuery(sql)
        query.addEntity(Subscriber)

        def subscribers = new Subscribers()
        subscribers.list = query.list()
        subscribers.total = subscribers.list.size()

        return subscribers
    }


}
