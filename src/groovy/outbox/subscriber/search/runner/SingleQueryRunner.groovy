package outbox.subscriber.search.runner

import org.apache.log4j.Logger
import org.hibernate.SessionFactory
import outbox.subscriber.Subscriber
import outbox.subscriber.search.Subscribers
import outbox.subscriber.search.criteria.CriterionNode
import outbox.subscriber.search.criteria.CriterionNodeType
import outbox.subscriber.search.criteria.InSubqueryCriterion
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

        def subscriberQuery = queries.subscriberFieldQuery

        if (queries.dynamicFieldQuery) {
            def subquery = queries.dynamicFieldQuery.toSQL()
            def subqueryNode = new CriterionNode(type: CriterionNodeType.Criterion,
                    criterion: new InSubqueryCriterion(left: 'SubscriberId', subquery: subquery))

            def node = new CriterionNode()
            node.type = CriterionNodeType.And
            node.left = subqueryNode

            subscriberQuery.criteria.addNode(node)
        }

        def session = sessionFactory.currentSession

        def sql = subscriberQuery.toSQL()
        println sql

        def query = session.createSQLQuery(sql)
        if (subscriberQuery.page && subscriberQuery.perPage) {
            query.firstResult = (subscriberQuery.page - 1) * subscriberQuery.perPage
            query.maxResults = subscriberQuery.perPage
        }
        query.addEntity(Subscriber)

        def subscribers = new Subscribers()
        subscribers.list = query.list()
        subscribers.total = subscribers.list.size()

        return subscribers
    }


}
