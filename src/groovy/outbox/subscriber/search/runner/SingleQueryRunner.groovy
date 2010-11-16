package outbox.subscriber.search.runner

import org.apache.log4j.Logger
import org.hibernate.Hibernate
import org.hibernate.SessionFactory
import outbox.subscriber.Subscriber
import outbox.subscriber.search.Columns
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
            def subquery = queries.dynamicFieldQuery.toSelectSQL()
            def subqueryNode = new CriterionNode(type: CriterionNodeType.Criterion,
                    criterion: new InSubqueryCriterion(left: Columns.SubscriberId, subquery: subquery))

            def node = new CriterionNode()
            node.type = CriterionNodeType.And
            node.left = subqueryNode

            subscriberQuery.criteria.addNode(node)
        }

        def session = sessionFactory.currentSession

        def subscribers = new Subscribers()

        def countSql = subscriberQuery.toCountSQL()
        def countQuery = session.createSQLQuery(countSql)
        countQuery.addScalar('RowCount', Hibernate.LONG)
        subscribers.total = countQuery.uniqueResult()

        def selectSql = subscriberQuery.toSelectSQL()
        println selectSql
        def selectQuery = session.createSQLQuery(selectSql)
        if (subscriberQuery.page && subscriberQuery.perPage) {
            selectQuery.firstResult = (subscriberQuery.page - 1) * subscriberQuery.perPage
            selectQuery.maxResults = subscriberQuery.perPage
        }
        selectQuery.addEntity(Subscriber)
        subscribers.list = selectQuery.list()

        return subscribers
    }


}
