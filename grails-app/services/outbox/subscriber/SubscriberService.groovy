package outbox.subscriber

import org.hibernate.Session
import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil
import outbox.member.Member
import outbox.subscriber.field.DynamicFieldStatus
import outbox.subscriber.field.DynamicFieldValue
import outbox.subscriber.field.DynamicFieldValues

/**
 * This service used to manipulation with subscriber and it's details.
 * NOTE: Operations with subscribers list shouldn't be here!
 *
 * @author Ruslan Khmelyuk
 */
class SubscriberService {

    static transactional = true

    def dynamicFieldService

    /**
     * Gets the subscriber by it's id.
     * @param id subscriber id.
     * @return the found subscriber.
     */
    @Transactional(readOnly = true)
    Subscriber getSubscriber(String id) {
        Subscriber.get id
    }

    /**
     * Save subscriber to database.
     * @param subscriber the subscriber to be saved.
     * @return true if saved successfully, otherwise returns false.
     */
    @Transactional
    boolean saveSubscriber(Subscriber subscriber) {
        ServiceUtil.saveOrRollback subscriber
    }

    /**
     * Enable subscriber and save change to database.
     * @param subscriber the subscriber to enabled.
     * @return true if enabled successfully, otherwise returns false.
     */
    @Transactional
    boolean enableSubscriber(Subscriber subscriber) {
        subscriber.enabled = true
        saveSubscriber subscriber
    }

    /**
     * Disable subscriber and save change to database.
     * @param subscriber the subscriber to disable.
     * @return true if disabled successfully, otherwise returns false. 
     */
    @Transactional
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
    @Transactional
    boolean addSubscriberType(SubscriberType subscriberType) {
        ServiceUtil.saveOrRollback subscriberType
    }

    /**
     * Saves subscriber type.
     *
     * @param subscriberType subscriber type.
     * @return true if saved subscriber type.
     */
    @Transactional
    boolean saveSubscriberType(SubscriberType subscriberType) {
        ServiceUtil.saveOrRollback subscriberType
    }

    /**
     * Deletes subscriber type.
     * @param subscriberType the subscriber type to delete.
     */
    @Transactional
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
    @Transactional
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
     *
     * @param member the member to get subscribers for.
     * @return the list of found free subscribers.
     */
    @Transactional(readOnly = true)
    List<Subscriber> getSubscribersWithoutSubscription(Member member) {
        Subscriber.executeQuery(
                'from Subscriber s where s.member.id = :memberId '
                        + 'and not exists (select 1 from Subscription ss where ss.subscriber.id = s.id)',
                [memberId: member.id]
        )
    }

    /**
     * Gets the number of the subscribers that are not in any subscription list.
     * 
     * @param member the member to get subscribers for.
     * @return the number of found free subscribers.
     */
    @Transactional(readOnly = true)
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

    /**
     * Gets dynamic field values for specified subscriber.
     * @param subscriber the subscriber to get field values for.
     * @return the dynamic field values.
     */
    @Transactional(readOnly = true)
    DynamicFieldValues getSubscriberDynamicFields(Subscriber subscriber) {
        if (subscriber && subscriber.member) {
            def fields = dynamicFieldService.getDynamicFields(subscriber.member)
            def values = dynamicFieldService.getDynamicFieldValues(subscriber)
            return new DynamicFieldValues(fields, values)
        }
        return null
    }

    /**
     * Gets active dynamic field values for specified subscriber.
     * @param subscriber the subscriber to get field values for.
     * @return the active dynamic field values.
     */
    @Transactional(readOnly = true)
    DynamicFieldValues getActiveSubscriberDynamicFields(Subscriber subscriber) {
        if (subscriber && subscriber.member) {
            def fields = dynamicFieldService.getDynamicFields(subscriber.member)
            def values = dynamicFieldService.getDynamicFieldValues(subscriber)

            def activeFields = fields.findAll { it.status == DynamicFieldStatus.Active }
            def activeValues = values.findAll { it.dynamicField.status == DynamicFieldStatus.Active }

            return new DynamicFieldValues(activeFields, activeValues)
        }
        return null
    }

    /**
     * Saves dynamic field values.
     * @param values the dynamic field values.
     * @return true if was saved successfully, otherwise false.
     */
    @Transactional
    boolean saveSubscriberDynamicFields(DynamicFieldValues values) {
        if (values) {
            for (DynamicFieldValue each : values.values) {
                if (!dynamicFieldService.saveDynamicFieldValue(each)) {
                    return false
                }
            }
        }
        return true
    }

}
