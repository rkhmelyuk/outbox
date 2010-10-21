package outbox.subscription

import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil
import outbox.search.SearchConditions
import outbox.search.SearchResult

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionListService {

    static transactional = true

    /**
     * Saves subscriptions list to our storage.
     * @param subscriptionList the list to be saved.
     * @return {@code true} if was saved successfully, otherwise {@code false}  .
     */
    @Transactional
    boolean saveSubscriptionList(SubscriptionList subscriptionList) {
        ServiceUtil.saveOrRollback subscriptionList
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
     * Delete subscriptions list. In this case any relationship with subscribers are removed,
     * but subscribers are not removed.
     * @param subscriptionList the subscriptions list to remove.
     */
    @Transactional
    boolean deleteSubscriptionList(SubscriptionList subscriptionList) {
        if (subscriptionList) {
            try {
                deleteSubscriptions(subscriptionList)
                subscriptionList.delete(flush: true)
                return true
            }
            catch (Exception e) {
                return false
            }
        }
        return false
    }

    /**
     * Archive subscription list.
     * @param subscriptionList the subscription list to be archived.
     * @return true if was archived successfully, otherwise false.
     */
    @Transactional
    boolean archiveSubscriptionList(SubscriptionList subscriptionList) {
        if (subscriptionList) {
            subscriptionList.archived = true
            return saveSubscriptionList(subscriptionList)
        }
        return false
    }

    /**
     * Restore subscription list.
     * @param subscriptionList the subscription list to be restored.
     * @return true if was restored successfully, otherwise false.
     */
    @Transactional
    boolean restoreSubscriptionList(SubscriptionList subscriptionList) {
        if (subscriptionList) {
            subscriptionList.archived = false
            return saveSubscriptionList(subscriptionList)
        }
        return false
    }

    /**
     * Delete subscriptions for specified Subscription List.
     * @param subscriptionList the subscription list to remove subscriptions for.
     */
    private void deleteSubscriptions(SubscriptionList subscriptionList) {
        Subscription.executeUpdate(
                'delete from Subscription where subscriptionList = :subscriptionList',
                [subscriptionList: subscriptionList])
    }

    /**
     * Adds subscription.
     *
     * @param subscription the subscription to be added.
     * @return {@code true} if added, otherwise {@code false}.
     */
    @Transactional
    boolean addSubscription(Subscription subscription) {
        if (ServiceUtil.saveOrRollback(subscription)) {
            def subscriptionList = subscription.subscriptionList
            subscriptionList.subscribersNumber = Subscription.countBySubscriptionList(subscriptionList)
            return saveSubscriptionList(subscriptionList)
        }
        return false
    }

    /**
     * Gets the list of subscriptions for specified subscription list.
     *
     * @param subscriptionList the subscription list.
     * @return the list of found subscriptions.
     */
    @Transactional(readOnly = true)
    List<Subscription> getSubscriptionsForList(SubscriptionList subscriptionList) {
        Subscription.findAllBySubscriptionList subscriptionList
    }

    /**
     * Search Subscription lists by specified conditions.
     * @param conditions the search conditions.
     * @return the search results.
     */
    @Transactional(readOnly = true)
    SearchResult search(SearchConditions conditions) {
        conditions.search(SubscriptionList)
    }
}
