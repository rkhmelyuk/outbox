package outbox.subscriber

import outbox.member.Member

/**
 * Represents the list of subscribers.
 * This can be just a list of random subscribers,
 * the list of subscribers by type or by other criteria.
 *
 * @author Ruslan Khmelyuk
 */
class SubscribersList {

    Long id
    String name
    String description
    Integer subscribersNumber
    Date dateCreated

    Member owner

    static mapping = {
        table 'SubscribersList'
        id column: 'SubscribersListId'
        columns {
            name column: 'Name'
            description column: 'Description'
            subscribersNumber column: 'SubscribersNumber'
            owner column: 'MemberId'
            dateCreated column: 'CreateDate'
        }
        version false
        cache true
    }

    static constraints = {
        name nullable: false, blank: false, maxSize: 200
        description nullable: true, blank: true, maxSize: 1000
        subscribersNumber nullable: true
        owner nullable: false
    }
}
