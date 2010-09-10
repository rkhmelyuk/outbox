package outbox.subscriber

import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport

/**
 * @author Ruslan Khmelyuk
 */
class SubscribersListService {

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
     * Saves subscribers list to our storage.
     * @param subscribersList the list to be saved.
     * @return {@code true}  if was saved successfully, otherwise  {@code false} .
     */
    @Transactional(readOnly = false)
    boolean saveSubscribersList(SubscribersList subscribersList) {
        saveOrRollback subscribersList
    }

    /**
     * @param id the subscribers list id.
     * @return the found subscribers list.
     */
    @Transactional(readOnly = true)
    SubscribersList getSubscribersList(Long id) {
        SubscribersList.get id
    }

    /**
     * Delete subscribers list. In this case any relationship with subscribers are removed,
     * but subscribers are not removed.
     * @param subscribersList the subscribers list to remove.
     */
    @Transactional(readOnly = false)
    void deleteSubscribersList(SubscribersList subscribersList) {
        if (subscribersList) {
            // 1. cleanup subscribers list relationship
            // 2. delete subscribers list
            subscribersList.delete()
        }
    }
}
