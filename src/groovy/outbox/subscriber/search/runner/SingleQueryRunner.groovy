package outbox.subscriber.search.runner

import org.apache.log4j.Logger
import org.hibernate.Hibernate
import org.hibernate.SessionFactory
import outbox.subscriber.Subscriber
import outbox.subscriber.search.Columns
import outbox.subscriber.search.Subscribers
import outbox.subscriber.search.query.Queries
import outbox.subscriber.search.criteria.*

/**
 * Build single query and run it.
 * 
 * @author Ruslan Khmelyuk
 */
class SingleQueryRunner implements QueryRunner {

    static final Logger log = Logger.getLogger(SingleQueryRunner)

    SessionFactory sessionFactory

    Subscribers run(Queries queries) {

        final def subscribers = new Subscribers()

        def subscriberQuery = queries.subscriberFieldQuery

        dynamicFieldConditions(queries)
        subscriptionConditions(queries)

        def session = sessionFactory.currentSession

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

    void dynamicFieldConditions(Queries queries) {
        queries.dynamicFieldQueries.each { dynamicFieldQuery ->
            def subquery = dynamicFieldQuery.toSelectSQL()
            def subqueryNode = new CriterionNode(type: CriterionNodeType.Criterion,
                    criterion: new InSubqueryCriterion(left: Columns.SubscriberId, subquery: subquery))

            def node = new CriterionNode()
            node.type = CriterionNodeType.And
            node.left = subqueryNode

            queries.subscriberFieldQuery.criteria.addNode(node)
        }
    }

    void subscriptionConditions(Queries queries) {
        queries.subscriptionQueries.each { subscriptionQuery ->
            println subscriptionQuery

            def keyword
            def root = subscriptionQuery.criteria.root
            if (root.type == CriterionNodeType.Not) {
                subscriptionQuery.criteria.root = root.left
                keyword = SubqueryConditionType.NotExists
            }
            else {
                keyword = SubqueryConditionType.Exists
            }

            def criterion = new SubqueryCriterion(condition: keyword, subquery: subscriptionQuery)
            def subqueryNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)

            def node = new CriterionNode()
            node.type = CriterionNodeType.And
            node.left = subqueryNode

            queries.subscriberFieldQuery.criteria.addNode(node)
        }
    }

}
