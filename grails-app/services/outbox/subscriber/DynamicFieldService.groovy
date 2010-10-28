package outbox.subscriber

import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil
import outbox.member.Member
import outbox.subscriber.field.DynamicField

/**
 * Service to work with dynamic fields and it's values.
 *
 * @author Ruslan Khmelyuk
 */
class DynamicFieldService {

    static transactional = true

    /**
     * Add dynamic field.
     * If sequence is not set (ie 0), then correct sequence will be found and set.
     * @param dynamicField the dynamic field to add.
     * @return true if dynamic field was added, otherwise false.
     */
    @Transactional
    boolean addDynamicField(DynamicField dynamicField) {
        if (dynamicField) {
            if (dynamicField.sequence == 0) {
                def last = DynamicField.findByOwner(dynamicField.owner, [sort: 'sequence', order: 'desc'])
                dynamicField.sequence = (last?.sequence ?: 0) + 1
            }
            return ServiceUtil.saveOrRollback(dynamicField)
        }
        return false
    }

    /**
     * Saves dynamic field.
     * @param dynamicField the dynamic field to save.
     * @return true if dynamic field was saved, otherwise false.
     */
    @Transactional
    boolean saveDynamicField(DynamicField dynamicField) {
        ServiceUtil.saveOrRollback dynamicField
    }

    /**
     * Gets the list of dynamic fields for member.
     * @param dynamicField the dynamic field to save.
     * @return true if dynamic field was saved, otherwise false.
     */
    @Transactional(readOnly = true)
    List<DynamicField> getDynamicFields(Member member) {
        DynamicField.findAllByOwner member
    }

    /**
     * Gets dynamic field by id.
     * @param id the dynamic field id.
     * @return the found dynamic field.
     */
    @Transactional
    DynamicField getDynamicField(Long id) {
        DynamicField.get id
    }
}
