package outbox.search

import grails.orm.HibernateCriteriaBuilder
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
class OwnedByCondition implements Condition {

    Member member

    void build(HibernateCriteriaBuilder builder) {
        if (member) {
            builder.eq 'owner', member
        }
    }

}
