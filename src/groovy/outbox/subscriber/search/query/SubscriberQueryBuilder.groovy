package outbox.subscriber.search.query

import outbox.subscriber.search.criteria.CriteriaTree

/**
 * The builder of query to search over subscribers.
 * @author Ruslan Khmelyuk
 */
class SubscriberQueryBuilder implements QueryBuilder {

    Query build(CriteriaTree criteria) {
        if (criteria.empty) {
            return null
        }

        def query = new Query()

        query.distinct = true

        query.addColumn 'SubscriberId'
        query.addColumn 'FirstName'
        query.addColumn 'LastName'
        query.addColumn 'GenderId'
        query.addColumn 'LanguageId'
        query.addColumn 'TimezoneId'
        query.addColumn 'MemberId'
        query.addColumn 'Enabled'
        query.addColumn 'NamePrefixId'
        query.addColumn 'SubscriberTypeId'
        query.addColumn 'CreateDate'

        query.addTable 'Subscriber'

        query.criteria = criteria

        return query
    }

}
