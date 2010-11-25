package outbox.subscriber.search

import outbox.ValueUtil
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.search.condition.Concatenation
import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.ValueCondition
import outbox.subscriber.search.condition.ValueConditionType

/**
 * Fetch search conditions from request parameters.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-11-25
 */
class SearchConditionsFetcher {

    DynamicFieldService dynamicFieldService

    Conditions fetch(Map params) {
        def conditions = new Conditions()
        def rows = params.row
        if (rows != null) {
            if (!(rows instanceof Collection)) {
                rows = [rows]
            }

            rows.each { String rowId ->
                def typeId = ValueUtil.integer(params["row[$rowId].type"])
                def type = ConditionType.getById(typeId)
                def condition = null
                switch (type) {
                    case ConditionType.Subscriber:
                        condition = subscriberConditions(params, rowId)
                        break
                    case ConditionType.DynamicField:
                        break
                    case ConditionType.Subscription:
                        break
                }

                if (condition) {
                    conditions.add Concatenation.And, condition
                }
            }
        }

        return conditions
    }

    SubscriberFieldCondition subscriberConditions(Map params, String rowId) {
        def field = params["row[$rowId].field"]
        def comparisonId = ValueUtil.integer(params["row[$rowId].comparison"])
        def comparison = ValueConditionType.getById(comparisonId)

        if (field && comparison && Names.isSubscriberField(field)) {
            def value = null

            if (comparison != ValueConditionType.Empty
                    && comparison != ValueConditionType.Filled) {
                value = params["row[$rowId].value"]
            }

            value = new ValueCondition(value, comparison)
            return new SubscriberFieldCondition(field, value)
        }
        return null
    }
}
