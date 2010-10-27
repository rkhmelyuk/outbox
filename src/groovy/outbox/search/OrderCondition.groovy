package outbox.search

import grails.orm.HibernateCriteriaBuilder

/**
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
class OrderCondition implements Condition {

    Map<String, String> order = [:] as LinkedHashMap<String, String>

    boolean isConditionFilter() {
        false
    }

    void put(String field, String sort) {
        if (field && sort) {
            order.put(field, sort)
        }
    }

    void build(HibernateCriteriaBuilder builder) {
        order.each { field, sort ->
            if (field && sort) {
                builder.order(field, sort)
            }
        }
    }
}
