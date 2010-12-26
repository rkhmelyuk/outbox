package outbox.subscriber.search

/**
 * Constants with column names.
 * 
 * @author Ruslan Khmelyuk
 */
public class Names {

    // -----------------------------------------------------------------
    // Table Names and Aliases

    static final String SubscriberTable = 'Subscriber'
    static final String SubscriberAlias = 'S'
    static final String SubscriptionTable = 'Subscription'
    static final String SubscriptionAlias = 'SS'

    static final String DynamicFieldTable = 'DynamicField'
    static final String DynamicFieldAlias = 'DF'
    static final String DynamicFieldValueTable = 'DynamicFieldValue'
    static final String DynamicFieldValueAlias = 'DFV'


    // -----------------------------------------------------------------
    // Column Names

    static final String SubscriberId = 'SubscriberId'
    static final String FirstName = 'FirstName'
    static final String LastName = 'LastName'
    static final String GenderId = 'GenderId'
    static final String LanguageId = 'LanguageId'
    static final String TimezoneId = 'TimezoneId'
    static final String CreateDate = 'CreateDate'
    static final String MemberId = 'MemberId'
    static final String Enabled = 'Enabled'
    static final String NamePrefixId = 'NamePrefixId'
    static final String SubscriberTypeId = 'SubscriberTypeId'
    static final String Email = 'Email'

    static final String SubscriptionListId = 'SubscriptionListId'
    static final String SubscriptionStatusId = 'SubscriptionStatusId'

    static final String StringValue = 'StringValue'
    static final String NumberValue = 'NumberValue'
    static final String BooleanValue = 'BooleanValue'

    static final String DynamicFieldId = 'DynamicFieldId'
    static final String DynamicFieldItemId = 'DynamicFieldItemId'

    static final String RowCount = 'RowCount'

    static boolean isSubscriberField(String name) {
        name && (name == FirstName || name == LastName ||
                name == GenderId || name == LanguageId ||
                name == Email || name == TimezoneId ||
                name == Enabled || name == NamePrefixId ||
                name == SubscriberTypeId)
    }

    /**
     * Whether field contains integer value.
     * @param name the field name.
     * @return true if field contains integer values.
     */
    static boolean isInteger(String name) {
        name && (name == GenderId || name == LanguageId || name == TimezoneId)
    }

    /**
     * Whether field contains long value.
     * @param name the field name.
     * @return true if field contains long values.
     */
    static boolean isLong(String name) {
        name && name == SubscriberTypeId
    }

}