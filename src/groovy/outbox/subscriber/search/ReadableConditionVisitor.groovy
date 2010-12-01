package outbox.subscriber.search

import outbox.MessageUtil
import outbox.subscriber.search.condition.*

/**
 * Prepares a human readable representation of query.
 * This should help users to ready the query as a simple sentence.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-01
 */
class ReadableConditionVisitor implements ConditionVisitor {

    def subscriberDescription = new StringBuilder()

    void visitSubscriberFieldCondition(SubscriberFieldCondition condition) {
        if (condition.visible) {
            def description  = new StringBuilder()
            description << 'Subscriber field '
            description << "'${subscriberFieldName(condition.field)}'"
            description << " " << valueType(condition.value)
            description << " " << valueName(condition.value)
            subscriberDescription << description.toString().trim()
        }
    }

    void visitDynamicFieldCondition(DynamicFieldCondition condition) {

    }

    void visitSubscriptionCondition(SubscriptionCondition condition) {

    }

    String subscriberFieldName(String field) {
        switch (field) {
            case Names.Email:
                return MessageUtil.getMessage('searchFields.email')
            case Names.FirstName:
                return MessageUtil.getMessage('searchFields.firstName')
            case Names.LastName:
                return MessageUtil.getMessage('searchFields.lastName')
            case Names.GenderId:
                return MessageUtil.getMessage('searchFields.gender')
            case Names.TimezoneId:
                return MessageUtil.getMessage('searchFields.timezone')
            case Names.LanguageId:
                return MessageUtil.getMessage('searchFields.language')
            case Names.SubscriberTypeId:
                return MessageUtil.getMessage('searchFields.subscriberType')
        }
        return ''
    }

    String valueType(ValueCondition value) {
        MessageUtil.getMessage(value.type.descriptionMessage)
    }

    String valueName(ValueCondition value) {
        if (value.type == ValueConditionType.InList ||
                value.type == ValueConditionType.NotInList) {
            return value.value.toString()
        }
        else if (value.type != ValueConditionType.Filled &&
                value.type != ValueConditionType.Empty) {
            return "'$value.value'"
        }
        return ''
    }

}
