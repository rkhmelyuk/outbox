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

        def subscriberAlias = 'S'
        query.addTableColumn subscriberAlias, Columns.SubscriberId
        query.addTableColumn subscriberAlias, Columns.Email
        query.addTableColumn subscriberAlias, Columns.FirstName
        query.addTableColumn subscriberAlias, Columns.LastName
        query.addTableColumn subscriberAlias, Columns.GenderId
        query.addTableColumn subscriberAlias, Columns.LanguageId
        query.addTableColumn subscriberAlias, Columns.TimezoneId
        query.addTableColumn subscriberAlias, Columns.MemberId
        query.addTableColumn subscriberAlias, Columns.Enabled
        query.addTableColumn subscriberAlias, Columns.NamePrefixId
        query.addTableColumn subscriberAlias, Columns.SubscriberTypeId
        query.addTableColumn subscriberAlias, Columns.CreateDate

        query.addTable 'Subscriber', subscriberAlias

        query.criteria = criteria

        return query
    }

}
