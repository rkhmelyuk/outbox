package outbox.subscriber.search

import grails.plugins.springsecurity.SpringSecurityService
import outbox.ValueUtil
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicFieldType
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

    /**
     * Fetches conditions from the map of parameters.
     * @param params the map of parameters.
     * @return the fetched subscribers search conditions.
     */
    Conditions fetch(Map params) {
        def conditions = new Conditions()
        conditions.page = ValueUtil.getInteger(params.page)
        conditions.perPage = ValueUtil.getInteger(params.perPage)

        def column = params.column
        if (column) {
            def sort = Sort.getByKeyword(params.sort) ?: Sort.Asc
            conditions.orderBy(column, sort)
        }

        def rows = params.row
        if (rows != null) {
            if (rows instanceof String) {
                rows = [rows]
            }

            rows.each { String rowId ->
                def typeId = ValueUtil.getInteger(params["row[$rowId].type"])
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
                        condition = subscriptionCondition(params, rowId)
                        break
                }

                if (condition) {
                    conditions.add Concatenation.And, condition
                }
            }
        }

        return conditions
    }

    /**
     * Fetches subscriber field conditions for specified row.
     * @param params the parameters map.
     * @param rowId the conditions row id.
     * @return the fetched conditions or null if not found or not complete.
     */
    SubscriberFieldCondition subscriberCondition(Map params, String rowId) {
        def field = params["row[$rowId].field"]
        def comparisonId = ValueUtil.getInteger(params["row[$rowId].comparison"])
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

    /**
     * Fetches dynamic field conditions for specified row.
     * @param params the parameters map.
     * @param rowId the conditions row id.
     * @return the fetched conditions or null if not found or not complete.
     */
    DynamicFieldCondition dynamicFieldCondition(Map params, String rowId) {
        def field = ValueUtil.getInteger(params["row[$rowId].field"])
        def comparisonId = ValueUtil.getInteger(params["row[$rowId].comparison"])
        def comparison = ValueConditionType.getById(comparisonId)

        if (field && comparison) {
            def value = null

            def dynamicField = dynamicFieldService.getDynamicField(field)
            def memberId = springSecurityService.principal.id
            if (dynamicField && dynamicField.ownedBy(memberId)) {
                if (comparison != ValueConditionType.Empty
                        && comparison != ValueConditionType.Filled) {
                    value = params["row[$rowId].value"]
                    if (dynamicField.type == DynamicFieldType.Number) {
                        value = ValueUtil.getInteger(value)
                    }
                    else if (dynamicField.type == DynamicFieldType.SingleSelect) {
                        def id = ValueUtil.getLong(value)
                        def item = dynamicFieldService.getDynamicFieldItem(id)
                        if (!item || item.field?.id != dynamicField.id) {
                            // didn't find item or item is not usable for us, so no condition
                            return null
                        }
                        value = item
                    }
                }

                value = new ValueCondition(value, comparison)
                return new DynamicFieldCondition(dynamicField, value)
            }
        }
        return null
    }

    /**
     * Fetches subscription condition for specified row.
     * @param params the parameters map.
     * @param rowId the conditions row id.
     * @return the fetched conditions or null if not found or not complete.
     */
    SubscriptionCondition subscriptionCondition(Map params, String rowId) {
        def subscriptionListId = ValueUtil.getInteger(params["row[$rowId].subscriptionList"])
        if (subscriptionListId && params["row[$rowId].subscriptionType"]) {
            def subscribed = ValueUtil.getInteger(params["row[$rowId].subscriptionType"]) == 1
            return new SubscriptionCondition(subscribed, subscriptionListId)
        }
        return null
    }
}
