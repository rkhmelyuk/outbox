package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import outbox.ValueUtil
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.search.condition.*

/**
 * Fetch search conditions from request parameters.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-11-25
 */
class SearchConditionsFetcher {

    DynamicFieldService dynamicFieldService
    SpringSecurityService springSecurityService

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
                        condition = subscriberCondition(params, rowId)
                        break
                    case ConditionType.DynamicField:
                        condition = dynamicFieldCondition(params, rowId)
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

    SubscriberFieldCondition subscriberCondition(Map params, String rowId) {
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

    DynamicFieldCondition dynamicFieldCondition(Map params, String rowId) {
        def field = ValueUtil.integer(params["row[$rowId].field"])
        def comparisonId = ValueUtil.integer(params["row[$rowId].comparison"])
        def comparison = ValueConditionType.getById(comparisonId)

        if (field && comparison) {
            def value = null

            def dynamicField = dynamicFieldService.getDynamicField(field)
            def memberId = springSecurityService.principal.id
            if (dynamicField && dynamicField.ownedBy(memberId)) {
                if (comparison != ValueConditionType.Empty
                        && comparison != ValueConditionType.Filled) {
                    value = params["row[$rowId].value"]
                }

                value = new ValueCondition(value, comparison)
                return new DynamicFieldCondition(dynamicField, value)
            }
        }
        return null
    }
}
