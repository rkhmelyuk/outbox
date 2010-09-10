package outbox.subscriber

import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import outbox.member.Member

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
     * Gets subscribers list by it's id.
     * 
     * @param id the subscribers list id.
     * @return the found subscribers list.
     */
    @Transactional(readOnly = true)
    SubscribersList getSubscribersList(Long id) {
        SubscribersList.get id
    }

    /**
     * Gets members subscribers lists.
     * @param member the member to get owned subscribers lists.
     * @return the found subscribers lists.
     */
    @Transactional(readOnly = true)
    List<SubscribersList> getMemberSubscribersList(Member member) {
        SubscribersList.findAllByOwner member
    }

    /**
     * Delete subscribers list. In this case any relationship with subscribers are removed,
     * but subscribers are not removed.
     * @param subscribersList the subscribers list to remove.
     */
    @Transactional(readOnly = false)
    void deleteSubscribersList(SubscribersList subscribersList) {
        if (subscribersList) {
            // TODO - 1. cleanup subscribers list relationship
            // 2. delete subscribers list
            subscribersList.delete()
        }
    }
}
