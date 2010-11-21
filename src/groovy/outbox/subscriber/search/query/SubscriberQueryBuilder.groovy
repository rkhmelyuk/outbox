package outbox.subscriber.search.query

import outbox.subscriber.search.Names
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Table

/**
 * The builder of query to search over subscribers.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriberQueryBuilder implements QueryBuilder {

    Query build(CriteriaTree criteria) {
        def query = new Query()

        query.distinct = true

        def subscriberTable = new Table(Names.SubscriberTable, Names.SubscriberAlias)

        query.addTable subscriberTable

        query.addColumn new Column(subscriberTable, Names.SubscriberId)
        query.addColumn new Column(subscriberTable, Names.Email)
        query.addColumn new Column(subscriberTable, Names.FirstName)
        query.addColumn new Column(subscriberTable, Names.LastName)
        query.addColumn new Column(subscriberTable, Names.GenderId)
        query.addColumn new Column(subscriberTable, Names.LanguageId)
        query.addColumn new Column(subscriberTable, Names.TimezoneId)
        query.addColumn new Column(subscriberTable, Names.MemberId)
        query.addColumn new Column(subscriberTable, Names.Enabled)
        query.addColumn new Column(subscriberTable, Names.NamePrefixId)
        query.addColumn new Column(subscriberTable, Names.SubscriberTypeId)
        query.addColumn new Column(subscriberTable, Names.CreateDate)

        query.criteria = criteria

        return query
    }

}
