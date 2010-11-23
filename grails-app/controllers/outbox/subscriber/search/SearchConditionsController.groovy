package outbox.subscriber.search

import outbox.member.Member
import outbox.subscriber.search.condition.ValueConditionType
import outbox.subscription.SubscriptionListConditionsBuilder
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.Timezone
import outbox.subscriber.field.DynamicFieldType

/**
 * This controller only responsibility is to build conditions view
 * for the subscribers search.
 *
 * @author Ruslan Khmelyuk
 */
class SearchConditionsController {

    def subscriberService
    def dynamicFieldService
    def springSecurityService
    def subscriptionListService

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
        def model = [type: ConditionType.Subscriber.id]
        model.types = types()
        model.comparison = params.comparison
        model.row = params.int('row')
        model.field = params.field

        // -----------------------------
        // fields
        def fields = [:]
        fields[Names.Email] = message(code: 'searchFields.email')
        fields[Names.FirstName] = message(code: 'searchFields.firstName')
        fields[Names.LastName] = message(code: 'searchFields.lastName')
        fields[Names.GenderId] = message(code: 'searchFields.gender')
        fields[Names.LanguageId] = message(code: 'searchFields.language')
        fields[Names.TimezoneId] = message(code: 'searchFields.timezone')
        fields[Names.SubscriberTypeId] = message(code: 'searchFields.subscriberType')

        model.fields = fields

        // -----------------------------
        // value and values if any
        def value, values, comparisonsList
        switch (model.field) {
            case Names.GenderId:
                values = Gender.list()
                value = params.int('value')
                comparisonsList = selectComparisons()
                break
            case Names.LanguageId:
                values = Language.list()
                value = params.int('value')
                comparisonsList = selectComparisons()
                break
            case Names.TimezoneId:
                values = Timezone.list()
                value = params.int('value')
                comparisonsList = selectComparisons()
                break
            case Names.SubscriberTypeId:
                def member = Member.load(springSecurityService.principal.id)
                values = subscriberService.getSubscriberTypes(member)
                value = params.long('value')
                comparisonsList = selectComparisons()
                break
            default:
                values = null
                value = params.value
                comparisonsList = comparisons()
        }

        model['values'] = values
        model.value = value
        model.comparisons = comparisonsList

        render template: 'subscriberCondition', model: model
    }

    void renderDynamicFieldRow() {
        def model = [type: ConditionType.DynamicField.id]
        model.types = types()
        model.comparison = params.comparison
        model.row = params.int('row')

        def field = params.long('field')
        model.field = field

        def member = Member.load(springSecurityService.principal.id)
        def dynamicFields = dynamicFieldService.getDynamicFields(member)
        if (dynamicFields) {
            model.dynamicFields = dynamicFields

            def value, values, comparisonsList
            def dynamicField = dynamicFields.find { it.id == field }
            dynamicField = dynamicField ?: dynamicFields.first()

            if (dynamicField && dynamicField.type == DynamicFieldType.SingleSelect) {
                value = params.long('value')
                comparisonsList = selectComparisons()
                values = dynamicFieldService.getDynamicFieldItems(dynamicField)
            }
            else {
                value = params.value
                values = null
                comparisonsList = comparisons()
            }

            model.value = params.value
            model['values'] = values
            model.comparisons = comparisonsList
        }

        render template: 'dynamicFieldCondition', model: model
    }

    void renderSubscriptionRow() {
        def member = Member.load(springSecurityService.principal.id)
        def conditions = new SubscriptionListConditionsBuilder().build {
            ownedBy member
            archived false
        }
        def subscriptionLists = subscriptionListService.search(conditions)

        def row = params.int('row') ?: 0
        def model = [type: ConditionType.Subscription.id, row: row + 1,
                subscriptionLists: subscriptionLists, types: types()]
        render template: 'subscriptionCondition', model: model
    }

    List<Map> types() {
        ConditionType.collect {
            [key: it.id, value: message(code: it.message)]
        }
    }

    List<Map> comparisons() {
        def types = [
                ValueConditionType.Empty,
                ValueConditionType.Filled,
                ValueConditionType.Equal,
                ValueConditionType.NotEqual,
                ValueConditionType.Like,
                ValueConditionType.Less,
                ValueConditionType.LessOrEqual,
                ValueConditionType.Greater,
                ValueConditionType.GreaterOrEqual]

        types.collectAll { [key: it.id, value: message(code: it.message)] }
    }

    List<Map> selectComparisons() {
        def types = [
                ValueConditionType.Empty,
                ValueConditionType.Filled,
                ValueConditionType.Equal,
                ValueConditionType.NotEqual,
                ValueConditionType.InList,
                ValueConditionType.NotInList]

        types.collectAll { [key: it.id, value: message(code: it.message)] }
    }
}
