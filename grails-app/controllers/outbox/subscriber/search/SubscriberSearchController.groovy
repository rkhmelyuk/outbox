package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import outbox.ValueUtil
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.Timezone
import outbox.member.Member
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.SubscriberService
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldType
import outbox.subscription.SubscriptionListConditionsBuilder
import outbox.subscription.SubscriptionListService
import outbox.subscriber.search.condition.*

/**
 * This controller only responsibility is to build conditions view
 * for the subscribers search.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriberSearchController {

    SubscriberService subscriberService
    DynamicFieldService dynamicFieldService
    SpringSecurityService springSecurityService
    SubscriptionListService subscriptionListService
    SubscriberSearchService subscriberSearchService
    SearchConditionsFetcher searchConditionsFetcher

    // --------------------------------------------------------------------
    // Conditions
    // --------------------------------------------------------------------

    def renderConditions = {
        Conditions conditions = request.getAttribute('conditions')
        if (conditions && !conditions.empty) {
            conditions.conditions.eachWithIndex { condition, index ->
                if (condition.visible) {
                    if (condition instanceof SubscriberFieldCondition) {
                        renderSubscriberRow(
                                index + 1, condition.field,
                                condition.value.type.id,
                                condition.value.value,
                                condition.concatenation.id)
                    }
                    else if (condition instanceof DynamicFieldCondition) {
                        renderDynamicFieldRow(
                                index + 1, condition.field.id,
                                condition.value.type.id,
                                condition.value.value,
                                condition.concatenation.id)
                    }
                    else if (condition instanceof SubscriptionCondition) {
                        renderSubscriptionRow(index + 1,
                                condition.subscribedTo ? 1 : 0,
                                condition.subscriptionList.id,
                                condition.concatenation.id)
                    }
                }
            }
        }
        render text: ''
    }

    /**
     * Add new conditions row.
     * We add each new row of specified type.
     * This action returns only row code.
     */
    def renderRow = {
        def type = params.int('type')
        def conditionType = (type != null
        ? ConditionType.getById(type)
        : ConditionType.Subscriber)

        if (conditionType == ConditionType.Subscriber) {
            renderSubscriberRow()
        }
        else if (conditionType == ConditionType.DynamicField) {
            renderDynamicFieldRow()
        }
        else if (conditionType == ConditionType.Subscription) {
            renderSubscriptionRow()
        }
        else {
            response.sendError 404
        }
    }

    void renderSubscriberRow() {
        def row = params.int('row')
        def field = params.field
        def value = params.value
        def comparison = params.int('comparison')
        def concatenation = params.int('concatenation')

        renderSubscriberRow(row, field, comparison, value, concatenation)
    }

    void renderSubscriberRow(row, field, comparison, value, concatenation) {
        def model = [type: ConditionType.Subscriber.id]
        model.row = row
        model.types = types()
        model.field = field
        model.comparison = comparison
        model.concatenation = concatenation
        model.concatenations = concatenations()

        // -----------------------------
        // fields

        def member = Member.load(springSecurityService.principal.id)
        def subscriberTypes = subscriberService.getSubscriberTypes(member)

        def fields = [:]
        fields[Names.Email] = message(code: 'searchFields.email')
        fields[Names.FirstName] = message(code: 'searchFields.firstName')
        fields[Names.LastName] = message(code: 'searchFields.lastName')
        fields[Names.GenderId] = message(code: 'searchFields.gender')
        fields[Names.LanguageId] = message(code: 'searchFields.language')
        fields[Names.TimezoneId] = message(code: 'searchFields.timezone')
        if (!subscriberTypes.empty) {
            fields[Names.SubscriberTypeId] = message(code: 'searchFields.subscriberType')
        }

        model.fields = fields

        // -----------------------------
        // value and values if any
        def values, comparisonsList
        switch (field) {
            case Names.GenderId:
                values = Gender.list()
                value = ValueUtil.getInteger(value)
                break
            case Names.LanguageId:
                values = Language.list()
                value = ValueUtil.getInteger(value)
                break
            case Names.TimezoneId:
                values = Timezone.list()
                value = ValueUtil.getInteger(value)
                break
            case Names.SubscriberTypeId:
                values = subscriberTypes
                value = ValueUtil.getLong(value)
                break
            default:
                values = null
        }
        if (Names.isStringSubscriberField(field)) {
            comparisonsList = stringComparisons()
        }
        else if (Names.isSelectSubscriberField(field)) {
            comparisonsList = selectComparisons()
        }
        else {
            comparisonsList = stringComparisons()
        }

        model['values'] = values
        model.value = value
        model.comparisons = comparisonsList
        model.showValue = showValue(comparison)

        render template: 'subscriberCondition', model: model
    }

    void renderDynamicFieldRow() {
        def value = params.value
        def row = params.int('row')
        def field = params.long('field')
        def comparison = params.int('comparison')
        def concatenation = params.int('concatenation')

        renderDynamicFieldRow(row, field, comparison, value, concatenation)
    }

    void renderDynamicFieldRow(row, field, comparison, value, concatenation) {
        def model = [type: ConditionType.DynamicField.id]
        model.row = row
        model.field = field
        model.types = types()
        model.comparison = comparison
        model.concatenation = concatenation
        model.concatenations = concatenations()

        def member = Member.load(springSecurityService.principal.id)
        def dynamicFields = dynamicFieldService.getDynamicFields(member)
        if (dynamicFields) {
            model.dynamicFields = dynamicFields

            def values, comparisonsList
            def dynamicField = dynamicFields.find { it.id == field }
            dynamicField = dynamicField ?: dynamicFields.first()

            if (dynamicField && dynamicField.type == DynamicFieldType.SingleSelect) {
                value = value?.id
                comparisonsList = selectComparisons()
                values = dynamicFieldService.getDynamicFieldItems(dynamicField)
            }
            else if (dynamicField && dynamicField.type == DynamicFieldType.Number) {
                values = null
                comparisonsList = numberComparisons()
            }
            else if (dynamicField && dynamicField.type == DynamicFieldType.Boolean) {
                values = null
                comparisonsList = booleanComparisons()
            }
            else {
                values = null
                comparisonsList = stringComparisons()
            }

            model.dynamicField = dynamicField
            model.value = value
            model['values'] = values
            model.comparisons = comparisonsList
            model.showValue = showValue(comparison)
            model.valueType = getValueType(dynamicField)
        }

        render template: 'dynamicFieldCondition', model: model
    }

    private def getValueType(DynamicField dynamicField) {
        if (dynamicField) {
            switch (dynamicField.type) {
                case DynamicFieldType.Boolean:
                    return 'boolean'
                case DynamicFieldType.Number:
                    return 'number'
                case DynamicFieldType.SingleSelect:
                    return 'select'
                default:
                    return 'string'
            }
        }
        return null
    }

    void renderSubscriptionRow() {
        def row = params.int('row')
        def value = params.subscriptionList
        def subscriptionType = params.int('subscriptionType')
        def concatenation = params.int('concatenation')

        renderSubscriptionRow(row, subscriptionType, value, concatenation)
    }

    void renderSubscriptionRow(row, subscriptionType, value, concatenation) {
        def member = Member.load(springSecurityService.principal.id)
        def conditions = new SubscriptionListConditionsBuilder().build {
            ownedBy member
            archived false
            order 'name', 'asc'
        }
        def subscriptionLists = subscriptionListService.search(conditions).list

        def model = [:]

        model.row = row
        model.types = types()
        model.subscriptionList = value
        model.concatenations = concatenations()
        model.subscriptionType = subscriptionType
        model.type = ConditionType.Subscription.id
        model.subscriptionLists = subscriptionLists
        model.subscriptionTypes = subscriptionComparisons()

        render template: 'subscriptionCondition', model: model
    }

    List<Map> types() {
        ConditionType.collect { [key: it.id, value: message(code: it.message)] }
    }

    List<Map> stringComparisons() {
        def types = [
                ValueConditionType.Empty,
                ValueConditionType.Filled,
                ValueConditionType.Equal,
                ValueConditionType.NotEqual,
                ValueConditionType.Like]

        types.collect { [key: it.id, value: g.message(code: it.message)] }
    }

    List<Map> booleanComparisons() {
        def types = [
                ValueConditionType.Empty,
                ValueConditionType.Filled,
                ValueConditionType.Equal,
                ValueConditionType.NotEqual]

        types.collect { [key: it.id, value: g.message(code: it.message)] }
    }

    List<Map> numberComparisons() {
        def types = [
                ValueConditionType.Empty,
                ValueConditionType.Filled,
                ValueConditionType.Equal,
                ValueConditionType.NotEqual,
                ValueConditionType.Less,
                ValueConditionType.LessOrEqual,
                ValueConditionType.Greater,
                ValueConditionType.GreaterOrEqual]

        types.collect { [key: it.id, value: g.message(code: it.message)] }
    }

    List<Map> selectComparisons() {
        def types = [
                ValueConditionType.Empty,
                ValueConditionType.Filled,
                ValueConditionType.Equal,
                ValueConditionType.NotEqual]

        types.collect { [key: it.id, value: g.message(code: it.message)] }
    }

    List<Map> concatenations() {
        def concatenations = [Concatenation.And, Concatenation.Or]
        concatenations.collect { [key: it.id, value: g.message(code: it.messageCode)] }
    }

    Map subscriptionComparisons() {
        return [1: message(code: 'subscribed.subscriptionList'),
                0: message(code: 'notSubscribed.subscriptionList')]
    }

    boolean showValue(def comparisonId) {
        def comparison = ValueConditionType.getById(comparisonId)
        (comparison != null
                && comparison != ValueConditionType.Empty
                && comparison != ValueConditionType.Filled)
    }

    // -------------------------------------------------------------------------
    // Search
    // -------------------------------------------------------------------------

    def searchResults = {
        def conditions = searchConditionsFetcher.fetch(params)
        conditions.page = conditions.page ?: 1
        conditions.perPage = conditions.perPage ?: 10

        def model = [conditions: conditions]
        if (!conditions.empty) {
            def memberId = springSecurityService.principal.id
            def ownershipCondition = new SubscriberFieldCondition('MemberId', ValueCondition.equal(memberId))
            ownershipCondition.visible = false
            ownershipCondition.readOnly = true
            conditions.addFirst Concatenation.And, ownershipCondition

            model.subscribers = subscriberSearchService.search(conditions)
            model.readableConditions = subscriberSearchService.describe(conditions)
        }

        render template: 'searchResult', model: model
    }
}
