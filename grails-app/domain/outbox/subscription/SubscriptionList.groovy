package outbox.subscription

import outbox.member.Member

/**
 * Represents the list of subscriptions.
 * This can be just a list of random subscribers,
 * the list of subscribers by type or by other criteria.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriptionList {

    Long id
    String name
    String description
    Integer subscribersNumber
    Date dateCreated

    Member owner

    static mapping = {
        table 'SubscriptionList'
        id column: 'SubscriptionListId'
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
        name nullable: false, blank: false, maxSize: 200, validator: { val, obj ->
            if (SubscriptionList.duplicateName(obj, val)) {
                return 'subscribersList.name.unique'
            }
        }
        description nullable: true, blank: true, maxSize: 1000
        subscribersNumber nullable: true
        owner nullable: false
        dateCreated nullable: true
    }

    /**
     * Check whether name is duplicate. We use member information from subscription list parameter
     * and check name specified as second parameter.
     *
     * @param list the subscriptions list, that should have this name.
     * @param name the new name for the subscriptions list.
     * @return {@code true} if name is duplicate for this member, otherwise returns {@code false}.
     */
    static boolean duplicateName(SubscriptionList list, String name) {
        def found = SubscriptionList.findByOwnerAndName(list.owner, name)
        return (found && !found.id.equals(list.id))
    }

    /**
     * Checks whether this SL belongs to the user with specified id.
     *
     * @param memberId the member id
     * @return {@code true} if belongs, otherwise {@code false}.
     */
    boolean ownedBy(Long memberId) {
        owner?.id == memberId
    }
}
