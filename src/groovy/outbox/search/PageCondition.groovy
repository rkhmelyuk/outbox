package outbox.search

import grails.orm.HibernateCriteriaBuilder

/**
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
class PageCondition implements Condition {

    int page
    int max

    void build(HibernateCriteriaBuilder builder) {
        if (max) {
            builder.criteria.maxResults = max
        }
        if (page && max) {
            builder.criteria.firstResult = (page - 1) * max
        }
    }

}
