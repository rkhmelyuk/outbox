package outbox.subscriber

import outbox.member.Member

/**
 * Member can define subscriber types.
 * Each subscriber can have 0 or 1 subscriber type.
 * 
 * @author Ruslan Khmelyuk
 */
class SubscriberType {

    Long id
    String name
    Member member

    static mapping = {
        table 'SubscriberType'
        id column: 'SubscriberTypeId'
        columns {
            name column: 'Name'
            member column: 'MemberId'
        }
        member lazy: false
        sort 'name'
        version false
        cache true
    }

    static constraints = {
        name nullable: false, blank: false, maxSize: 200
        member nullable: false
    }
}
