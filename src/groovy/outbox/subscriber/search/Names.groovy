package outbox.subscriber.search

/**
 * Constants with column names.
 * 
 * @author Ruslan Khmelyuk
 */
public interface Names {

    String SubscriberTable = 'Subscriber'
    String SubscriberAlias = 'S'
    String SubscriptionTable = 'Subscription'
    String SubscriptionAlias = 'SS'

    String SubscriberId = 'SubscriberId'
    String FirstName = 'FirstName'
    String LastName = 'LastName'
    String GenderId = 'GenderId'
    String LanguageId = 'LanguageId'
    String TimezoneId = 'TimezoneId'
    String CreateDate = 'CreateDate'
    String MemberId = 'MemberId'
    String Enabled = 'Enabled'
    String NamePrefixId = 'NamePrefixId'
    String SubscriberTypeId = 'SubscriberTypeId'
    String Email = 'Email'


    String StringValue = 'StringValue'
    String NumberValue = 'NumberValue'
    String BooleanValue = 'BooleanValue'
    String DynamicFieldItemId = 'DynamicFieldItemId'

    String RowCount = 'RowCount'

}