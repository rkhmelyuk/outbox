package outbox.subscription

import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionListService {

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
     * Saves subscriptions list to our storage.
     * @param subscriptionList the list to be saved.
     * @return {@code true} if was saved successfully, otherwise  {@code false} .
     */
    @Transactional(readOnly = false)
    boolean saveSubscriptionList(SubscriptionList subscriptionList) {
        saveOrRollback subscriptionList
    }

    /**
     * Gets subscription list by it's id.
     * 
     * @param id the subscriptions list id.
     * @return the found subscriptions list.
     */
    @Transactional(readOnly = true)
    SubscriptionList getSubscriptionList(Long id) {
        SubscriptionList.get id
    }

    /**
     * Gets members subscriptions lists.
     * @param member the member to get owned subscriptions lists.
     * @return the found subscriptions lists.
     */
    @Transactional(readOnly = true)
    List<SubscriptionList> getMemberSubscriptionList(Member member) {
        SubscriptionList.findAllByOwner member
    }

    /**
     * Delete subscriptions list. In this case any relationship with subscribers are removed,
     * but subscribers are not removed.
     * @param subscribersList the subscriptions list to remove.
     */
    @Transactional(readOnly = false)
    void deleteSubscriptionList(SubscriptionList subscribersList) {
        if (subscribersList) {
            // TODO - 1. cleanup subscribers list relationship
            // 2. delete subscribers list
            subscribersList.delete()
        }
    }
}
