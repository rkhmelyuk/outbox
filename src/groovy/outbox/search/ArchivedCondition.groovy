package outbox.search

import grails.orm.HibernateCriteriaBuilder

/**
 * Search condition for items with archived flat set/clear.
 *
 * @author Ruslan Khmelyuk
 */
class ArchivedCondition implements Condition {

    boolean archived
    boolean set

    void setArchived(boolean archived) {
        this.archived = archived
        this.set = true
    }

    void build(HibernateCriteriaBuilder builder) {
        if (set) {
            builder.eq 'archived', archived
        }
    }

}
