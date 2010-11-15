package outbox.subscriber.search.query

import outbox.subscriber.search.Columns
import outbox.subscriber.search.criteria.CriteriaTree

/**
 * The builder of query to search over subscribers.
 * @author Ruslan Khmelyuk
 */
class SubscriberQueryBuilder implements QueryBuilder {

    Query build(CriteriaTree criteria) {
        def query = new Query()

        query.distinct = true

        query.addColumn Columns.SubscriberId
        query.addColumn Columns.Email
        query.addColumn Columns.FirstName
        query.addColumn Columns.LastName
        query.addColumn Columns.GenderId
        query.addColumn Columns.LanguageId
        query.addColumn Columns.TimezoneId
        query.addColumn Columns.MemberId
        query.addColumn Columns.Enabled
        query.addColumn Columns.NamePrefixId
        query.addColumn Columns.SubscriberTypeId
        query.addColumn Columns.CreateDate

        query.addTable 'Subscriber'

        query.criteria = criteria

        return query
    }

}
