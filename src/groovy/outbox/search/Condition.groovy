package outbox.search

import grails.orm.HibernateCriteriaBuilder

/**
 * Search conditions interface.
 *
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
public interface Condition {

    /**
     * Whether this condition used to filter by some condition
     * @return true if filter
     */
    boolean isConditionFilter()

    /**
     * Builds own part of the criteria using specified builder.
     * @param builder the builder used to build criteria.
     */
    void build(HibernateCriteriaBuilder builder)

}
