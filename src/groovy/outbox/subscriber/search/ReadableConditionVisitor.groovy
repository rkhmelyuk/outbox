package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import java.text.DecimalFormat
import outbox.MessageUtil
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.Timezone
import outbox.subscriber.SubscriberService
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

    SubscriberService subscriberService
    SpringSecurityService springSecurityService

    def subscriberDescriptions = []
    def dynamicFieldDescriptions = []
    def subscriptionDescriptions = []

    ReadableConditionVisitor(SubscriberService subscriberService,
                             SpringSecurityService springSecurityService) {
        this.subscriberService = subscriberService
        this.springSecurityService = springSecurityService
    }

    void visitSubscriberFieldCondition(SubscriberFieldCondition condition) {
        if (condition.visible) {
            def description  = new StringBuilder()
            description << concatenation(condition)
            description << ' '
            description << MessageUtil.getMessage('readable.field')
            description << ' '
            description << "'${subscriberFieldName(condition.field)}'"
            description << ' '
            description << valueType(condition.value)
            description << ' '
            description << valueName(condition.field, condition.value)
            subscriberDescriptions << description.toString().trim()
        }
    }

    void visitDynamicFieldCondition(DynamicFieldCondition condition) {
        if (condition.visible) {
            def description  = new StringBuilder()
            description << concatenation(condition)
            description << ' '
            description << MessageUtil.getMessage('readable.field')
            description << ' '
            description << "'${condition.field.label}'"
            description << " " << valueType(condition.value)
            description << " " << valueName(null, condition.value)
            dynamicFieldDescriptions << description.toString().trim()
        }
    }

    void visitSubscriptionCondition(SubscriptionCondition condition) {
        if (condition.visible) {
            def description  = new StringBuilder()
            description << concatenation(condition)
            if (condition.subscribedTo) {
                description << ' Subscribed to List '
            }
            else {
                description << ' Not Subscribed to List '
            }
            description << "'$condition.subscriptionList.name'"
            subscriptionDescriptions << description.toString().trim()
        }
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
        def type = prepareType(value)
        MessageUtil.getMessage(type.descriptionMessage)
    }

    String valueName(String fieldName, ValueCondition value) {
        def type = prepareType(value)
        if (type == ValueConditionType.InList || type == ValueConditionType.NotInList) {
            def values = value.value.collect {prepareValue(fieldName, it)}
            return values.toString()
        }
        else if (type != ValueConditionType.Filled && type != ValueConditionType.Empty) {
            return "'${prepareValue(fieldName, value.value)}'"
        }
        return ''
    }

    /**
     * Prepares the type of value condition to return correct one.
     * For example, if user searches for value equal to empty string, than it returns Empty type
     * and if user searches for value not equal to empty string, than it returns Filled type.
     *
     * @param value the value condition.
     * @return the correct value condition type.
     */
    private ValueConditionType prepareType(ValueCondition value) {
        def type = value.type
        def emptyValue = (value.value instanceof String && value.value.toString().empty)

        if (emptyValue && type == ValueConditionType.Equal) {
            type = ValueConditionType.Empty
        }
        else if (emptyValue && type == ValueConditionType.NotEqual) {
            type = ValueConditionType.Filled
        }

        return type
    }

    /**
     * Prepares the value to be understood to user and returns as string.
     * @param value the value to prepare.
     * @return the prepared value.
     */
    String prepareValue(String fieldName, def value) {
        if (fieldName != null) {
            def item = null
            switch (fieldName) {
                case Names.GenderId:
                    item = Gender.get(value)
                    break
                case Names.TimezoneId:
                    item = Timezone.get(value)
                    break
                case Names.LanguageId:
                    item = Language.get(value)
                    break
                case Names.SubscriberTypeId:
                    def memberId = springSecurityService.principal.id
                    println memberId
                    item = subscriberService.getMemberSubscriberType(memberId, value)
                    break
            }
            if (item) {
                return item ? item.name : ''
            }
        }

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
        subscriptionDescriptions.each { record ->
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
