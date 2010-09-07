package outbox.subscriber

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
        Subscriber.get(id)
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
    List<SubscriberType> getMemberSubscriberTypes(Member member) {
        SubscriberType.findAllByMember(member)
    }

    /**
     * Adds subscriber type.
     *
     * @param subscriberType subscriber type.
     * @return true if added subscriber type.
     */
    boolean addSubscriberType(SubscriberType subscriberType) {
        saveOrRollback subscriberType
    }

    /**
     * Saves subscriber type.
     *
     * @param subscriberType subscriber type.
     * @return true if saved subscriber type.
     */
    boolean saveSubscriberType(SubscriberType subscriberType) {
        saveOrRollback subscriberType
    }
}
