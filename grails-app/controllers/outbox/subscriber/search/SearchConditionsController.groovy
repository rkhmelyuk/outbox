package outbox.subscriber.search

import outbox.member.Member
import outbox.subscriber.search.condition.ValueConditionType
import outbox.subscription.SubscriptionListConditionsBuilder

/**
 * This controller only responsibility is to build conditions view
 * for the subscribers search.
 *
 * @author Ruslan Khmelyuk
 */
class SearchConditionsController {

    def dynamicFieldService
    def springSecurityService
    def subscriptionListService

    /**
     * Add new conditions row.
     * We add each new row of specified type.
     * This action returns only row code.
     */
    def addRow = {
        def id = params.int('id')
        def conditionType = (id != null
                             ? ConditionType.getById(id)
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
        def fields = [:]
        fields[Names.Email] = message(code: 'searchFields.email')
        fields[Names.FirstName] = message(code: 'searchFields.firstName')
        fields[Names.LastName] = message(code: 'searchFields.lastName')
        fields[Names.GenderId] = message(code: 'searchFields.gender')
        fields[Names.LanguageId] = message(code: 'searchFields.language')
        fields[Names.TimezoneId] = message(code: 'searchFields.timezone')
        fields[Names.SubscriberTypeId] = message(code: 'searchFields.subscriberType')

        def row = params.int('row') ?: 0
        def model = [type: ConditionType.Subscriber.id,
                fields: fields, types: types(),
                comparisons: comparisons(), row: row + 1]
        render template: 'subscriberCondition', model: model
    }

    void renderDynamicFieldRow() {
        def member = Member.load(springSecurityService.principal.id)
        def dynamicFields = dynamicFieldService.getDynamicFields(member)

        def row = params.int('row') ?: 0
        def model = [type: ConditionType.DynamicField.id,
                dynamicFields: dynamicFields,
                comparisons: comparisons(),
                types: types(), row: row + 1]
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

    /**
     * Update current conditions row to show correct fields
     */
    def refreshRow = {

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

    List<Map> selectComparison() {
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
