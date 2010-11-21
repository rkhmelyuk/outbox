package outbox.subscriber.search.query

import outbox.subscriber.search.Columns
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Table

/**
 * The builder of query to search over subscribers.
 * @author Ruslan Khmelyuk
 */
class SubscriberQueryBuilder implements QueryBuilder {

    Query build(CriteriaTree criteria) {
        def query = new Query()

        query.distinct = true

        def subscriberTable = new Table('Subscriber', 'S')

        query.addTable subscriberTable

        query.addColumn new Column(subscriberTable, Columns.SubscriberId)
        query.addColumn new Column(subscriberTable, Columns.Email)
        query.addColumn new Column(subscriberTable, Columns.FirstName)
        query.addColumn new Column(subscriberTable, Columns.LastName)
        query.addColumn new Column(subscriberTable, Columns.GenderId)
        query.addColumn new Column(subscriberTable, Columns.LanguageId)
        query.addColumn new Column(subscriberTable, Columns.TimezoneId)
        query.addColumn new Column(subscriberTable, Columns.MemberId)
        query.addColumn new Column(subscriberTable, Columns.Enabled)
        query.addColumn new Column(subscriberTable, Columns.NamePrefixId)
        query.addColumn new Column(subscriberTable, Columns.SubscriberTypeId)
        query.addColumn new Column(subscriberTable, Columns.CreateDate)

        query.criteria = criteria

        return query
    }

}
