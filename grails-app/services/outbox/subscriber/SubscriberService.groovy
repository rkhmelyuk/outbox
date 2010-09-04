package outbox.subscriber

import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport

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
     * Add new subscriber to database.
     * @param subscriber the new subscriber.
     * @return true if added successfully, otherwise returns false.
     */
    @Transactional(readOnly = false)
    boolean addSubscriber(Subscriber subscriber) {
        return saveOrRollback(subscriber)
    }

    /**
     * Save subscriber changes to database.
     * @param subscriber the subscriber to be saved.
     * @return true if saved successfully, otherwise returns false.
     */
    @Transactional(readOnly = false)
    boolean saveSubscriber(Subscriber subscriber) {
        return saveOrRollback(subscriber)
    }

    /**
     * Enable subscriber and save change to database.
     * @param subscriber the subscriber to enabled.
     * @return true if enabled successfully, otherwise returns false.
     */
    @Transactional(readOnly = false)
    boolean enableSubscriber(Subscriber subscriber) {
        subscriber.enabled = true
        return saveSubscriber(subscriber)
    }

    /**
     * Disable subscriber and save change to database.
     * @param subscriber the subscriber to disable.
     * @return true if disabled successfully, otherwise returns false. 
     */
    @Transactional(readOnly = false)
    boolean disableSubscriber(Subscriber subscriber) {
        subscriber.enabled = false
        return saveSubscriber(subscriber)
    }

}
