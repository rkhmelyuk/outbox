package outbox.subscriber.search.runner

import org.apache.log4j.Logger
import org.hibernate.Hibernate
import org.hibernate.SessionFactory
import outbox.subscriber.Subscriber
import outbox.subscriber.search.Names
import outbox.subscriber.search.Subscribers
import outbox.subscriber.search.query.Queries
import outbox.subscriber.search.query.Query
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.sql.CountSqlQueryBuilder
import outbox.subscriber.search.sql.SelectSqlQueryBuilder
import outbox.subscriber.search.criteria.*

/**
 * Build single query and run it.
 * <strong>Can search only by subscriber fields, dynamic fields and subscription.</strong>
 *
 * @author Ruslan Khmelyuk
 */
class SingleQueryRunner implements QueryRunner {

    static final Logger log = Logger.getLogger(SingleQueryRunner)

    SessionFactory sessionFactory

    def countQueryBuilder = new CountSqlQueryBuilder()
    def selectQueryBuilder = new SelectSqlQueryBuilder()

    Subscribers run(Queries queries) {

        final def subscribers = new Subscribers()

        def query = queries.subscriberFieldQuery

        dynamicFieldConditions(query, queries)
        subscriptionConditions(query, queries)

        def session = sessionFactory.currentSession

        def countSql = countQueryBuilder.build(query)
        def countQuery = session.createSQLQuery(countSql)
        countQuery.addScalar(Names.RowCount, Hibernate.LONG)
        subscribers.total = countQuery.uniqueResult()

        def selectSql = selectQueryBuilder.build(query)
        def selectQuery = session.createSQLQuery(selectSql)
        if (query.page && query.perPage) {
            selectQuery.firstResult = (query.page - 1) * query.perPage
            selectQuery.maxResults = query.perPage
        }
        selectQuery.addEntity(Subscriber)
        subscribers.list = selectQuery.list()

        subscribers.page = queries.subscriberFieldQuery.page
        subscribers.perPage = queries.subscriberFieldQuery.perPage

        return subscribers
    }

    void dynamicFieldConditions(Query query, Queries queries) {
        queries.dynamicFieldQueries.each { dynamicFieldQuery ->
            // build subquery, as we us InSubquery criterion
            def subquery = selectQueryBuilder.build(dynamicFieldQuery)
            def subqueryNode = new CriterionNode(type: CriterionNodeType.Criterion,
                    criterion: new InSubqueryCriterion(
                            left: new Column(Names.SubscriberAlias, Names.SubscriberId),
                            subquery: subquery))

            // create criterion node with correct concatenation
            def node = new CriterionNode()
            node.type = dynamicFieldQuery.criteria.root.type
            node.left = subqueryNode

            // add to end query
            query.criteria.addNode(node)
        }
    }

    void subscriptionConditions(Query query, Queries queries) {
        queries.subscriptionQueries.each { subscriptionQuery ->
            def type = subscriptionQuery.criteria.root.type
            // if root type is NOT, than we use Not Exists keyword, otherwise use Exists keyword
            def keyword
            def root = subscriptionQuery.criteria.root
            if (root.left.type == CriterionNodeType.Not) {
                subscriptionQuery.criteria.root = root.left.left
                keyword = SubqueryConditionType.NotExists
            }
            else {
                subscriptionQuery.criteria.root = root.left
                keyword = SubqueryConditionType.Exists
            }

            // add subquery criterion for subscription condition
            def criterion = new SubqueryCriterion(condition: keyword, subquery: subscriptionQuery)
            def subqueryNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)

            // create criterion node
            def node = new CriterionNode()
            node.type = type
            node.left = subqueryNode

            // add to end query
            query.criteria.addNode(node)
        }
    }

}
