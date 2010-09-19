package outbox.search

import grails.orm.HibernateCriteriaBuilder

/**
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
class OrderCondition implements Condition {

    Map<String, String> order = [:] as LinkedHashMap<String, String>

    void build(HibernateCriteriaBuilder builder) {
        order.each { first, sort ->
            builder.order(first, sort)
        }
    }
}
