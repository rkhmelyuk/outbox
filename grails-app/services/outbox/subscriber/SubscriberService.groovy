package outbox.subscriber

import org.hibernate.Session
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import outbox.member.Member

/**
 * This service used to manipulation with subscriber and it's details.
 * NOTE: Operations with subscribers list shouldn't be here!
 *
 * @author Ruslan Khmelyuk
 */
class SubscriberService {

    static transactional = true

    @Transactional(readOnly = false)
    private boolean saveOrRollback(Object item) {
        if (!item) {
            return false
        }

        boolean saved = (item.save(flush: true) != null)
        if (!saved) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
        }
        return saved
    }

    /**
     * Gets the subscriber by it's id.
     * @param id subscriber id.
     * @return the found subscriber.
     */
    Subscriber getSubscriber(String id) {
        Subscriber.get id
    }

    /**
     * Save subscriber to database.
     * @param subscriber the subscriber to be saved.
     * @return true if saved successfully, otherwise returns false.
     */
    @Transactional(readOnly = false)
    boolean saveSubscriber(Subscriber subscriber) {
        saveOrRollback subscriber
    }

    /**
     * Enable subscriber and save change to database.
     * @param subscriber the subscriber to enabled.
     * @return true if enabled successfully, otherwise returns false.
     */
    @Transactional(readOnly = false)
    boolean enableSubscriber(Subscriber subscriber) {
        subscriber.enabled = true
        saveSubscriber subscriber
    }

    /**
     * Disable subscriber and save change to database.
     * @param subscriber the subscriber to disable.
     * @return true if disabled successfully, otherwise returns false. 
     */
    @Transactional(readOnly = false)
    boolean disableSubscriber(Subscriber subscriber) {
        subscriber.enabled = false
        saveSubscriber subscriber
    }

    /**
     * Gets member subscriber types.
     * @param member member subscriber types.
     * @return the list with found subscriber types.
     */
    @Transactional(readOnly = true)
    List<SubscriberType> getSubscriberTypes(Member member) {
        SubscriberType.findAllByMember(member)
    }

    /**
     * Adds subscriber type.
     *
     * @param subscriberType subscriber type.
     * @return true if added subscriber type.
     */
    @Transactional(readOnly = false)
    boolean addSubscriberType(SubscriberType subscriberType) {
        saveOrRollback subscriberType
    }

    /**
     * Saves subscriber type.
     *
     * @param subscriberType subscriber type.
     * @return true if saved subscriber type.
     */
    @Transactional(readOnly = false)
    boolean saveSubscriberType(SubscriberType subscriberType) {
        saveOrRollback subscriberType
    }

    /**
     * Deletes subscriber type.
     * @param subscriberType the subscriber type to delete.
     */
    @Transactional(readOnly = false)
    void deleteSubscriberType(SubscriberType subscriberType) {
        if (subscriberType) {
            // 1. remove type from subscribers
            cleanupSubscriberTypes(subscriberType)
            // 2. remove subscriber type
            subscriberType.delete(flush: true)
        }
    }

    /**
     * Update subscribers to not used specified subscriber type.
     * @param subscriberType the subscriber type to not be used by subscribers.
     */
    @Transactional(readOnly = false)
    private void cleanupSubscriberTypes(SubscriberType subscriberType) {
        Subscriber.executeUpdate(
                'update Subscriber s set s.subscriberType = null where s.subscriberType.id = :subscriberTypeId',
                [subscriberTypeId: subscriberType.id])
    }

    /**
     * Gets member's subscriber type id.
     * @param memberId the member id.
     * @param subscriberTypeId the subscriber type id.
     * @return the found subscriber type for member.
     */
    @Transactional(readOnly = true)
    SubscriberType getMemberSubscriberType(Long memberId, Long subscriberTypeId) {
        SubscriberType.findByIdAndMember(subscriberTypeId, Member.load(memberId))
    }

    /**
     * Gets the list of subscribers that are not in any subscription list.
     * @param member the member to get subscribers for.
     * @return the list of found free subscribers.
     */
    @Transactional(readOnly = false)
    List<Subscriber> getSubscribersWithoutSubscription(Member member) {
        Subscriber.executeQuery(
                'from Subscriber s where s.member.id = :memberId '
                        + 'and not exists (select 1 from Subscription ss where ss.subscriber.id = s.id)',
                [memberId: member.id]
        )
    }

    /**
     * Gets the number of the subscribers that are not in any subscription list.
     * @param member the member to get subscribers for.
     * @return the number of found free subscribers.
     */
    @Transactional(readOnly = false)
    int getSubscribersWithoutSubscriptionCount(final Member member) {
        def firstResult = 0
        Subscriber.withSession { Session session ->
            firstResult =  session.createQuery(
                    'select count(s) from Subscriber s where s.member.id = :memberId '
                        + 'and not exists (select 1 from Subscription ss where ss.subscriber.id = s.id)')
            .setLong('memberId', member.id).uniqueResult()
        }

        return firstResult
    }
}
