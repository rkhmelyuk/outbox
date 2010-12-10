package outbox.subscriber.search

import java.text.DecimalFormat
import outbox.MessageUtil
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.search.condition.*

/**
 * Prepares a human readable representation of query.
 * This should help users to ready the query as a simple sentence.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-01
 */
class ReadableConditionVisitor implements ConditionVisitor {

    final DynamicFieldService dynamicFieldService

    def subscriberDescriptions = []
    def dynamicFieldDescriptions = []

    ReadableConditionVisitor(DynamicFieldService dynamicFieldService) {
        this.dynamicFieldService = dynamicFieldService
    }

    void visitSubscriberFieldCondition(SubscriberFieldCondition condition) {
        if (condition.visible) {
            def description  = new StringBuilder()
            description << concatenation(condition)
            description << ' Field '
            description << "'${subscriberFieldName(condition.field)}'"
            description << " " << valueType(condition.value)
            description << " " << valueName(condition.value)
            subscriberDescriptions << description.toString().trim()
        }
    }

    void visitDynamicFieldCondition(DynamicFieldCondition condition) {
        if (condition.visible) {
            def description  = new StringBuilder()
            description << concatenation(condition)
            description << ' Field '
            description << "'${condition.field.label}'"
            description << " " << valueType(condition.value)
            description << " " << valueName(condition.value)
            dynamicFieldDescriptions << description.toString().trim()
        }
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
            def values = value.value.collect {prepareValue(it)}
            return values.toString()
        }
        else if (value.type != ValueConditionType.Filled &&
                value.type != ValueConditionType.Empty) {
            return "'${prepareValue(value.value)}'"
        }
        return ''
    }

    String prepareValue(def value) {
        if (value instanceof BigDecimal) {
            value = new DecimalFormat('#############.#####').format(value)
        }
        else if (value instanceof Boolean) {
            value = value ? 'True' : 'False'
        }
        else if (value instanceof DynamicFieldItem) {
            value = value.name
        }
        else if (value == null) {
            value = ''
        }
        return value
    }

    String concatenation(Condition condition) {
        MessageUtil.getMessage(condition.concatenation.messageCode)
    }

    String getReadableString() {
        def result = new StringBuilder()
        subscriberDescriptions.each { record ->
            result << record
            result << ' '
        }
        dynamicFieldDescriptions.each { record ->
            result << record
            result << ' '
        }

        def string = result.toString().trim()
        def strings = string.split(' ', 2)
        if (strings.size() > 1) {
            return strings[1]
        }
        return string
    }

}
