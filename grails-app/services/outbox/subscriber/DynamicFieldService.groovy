package outbox.subscriber

import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil
import outbox.member.Member
import outbox.subscriber.field.*

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
     * Deletes dynamic field.
     * @param dynamicField the dynamic field to delete.
     * @return true if dynamic field was deleted, otherwise false.
     */
    @Transactional
    boolean deleteDynamicField(DynamicField dynamicField) {
        if (dynamicField) {
            deleteDynamicFieldItems dynamicField
            dynamicField.delete flush: true
            return true
        }
        return false
    }

    /**
     * Hides dynamic field.
     * @param dynamicField the dynamic field to hide.
     * @return true if dynamic field was hide, otherwise false.
     */
    @Transactional
    boolean hideDynamicField(DynamicField dynamicField) {
        if (dynamicField) {
            dynamicField.status = DynamicFieldStatus.Hidden
            return saveDynamicField(dynamicField)
        }
        return false
    }

    /**               `
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
    @Transactional(readOnly = true)
    DynamicField getDynamicField(Long id) {
        DynamicField.get id
    }

    /**
     * Adds dynamic field items. Only add items if dynamic field type is enumeration.
     *
     * @param field the dynamic field.
     * @param items the list of items to add.
     * @return true if all were added successfully, otherwise false.
     */
    @Transactional
    boolean addDynamicFieldItems(DynamicField field, List<DynamicFieldItem> items) {
        if (field.type == DynamicFieldType.SingleSelect) {
            for(DynamicFieldItem item in items) {
                item.field = field
                if (!saveDynamicFieldItem(item)) {
                    return false
                }
            }
            return true
        }
        return false
    }

    /**
     * Saved single dynamic field item.
     * @param item the dynamic field item to save.
     * @return true if saved, otherwise false.
     */
    @Transactional
    boolean saveDynamicFieldItem(DynamicFieldItem item) {
        ServiceUtil.saveOrRollback item
    }

    /**
     * Gets the list of new items for specified dynamic field. If any item is no within this list, then
     * it will be removed, if any new item in list, than will be added.
     *
     * @param field the dynamic field.
     * @param items the list of new items.
     * @return true if dynamic field items were updated, otherwise false.
     */
    @Transactional
    boolean updateDynamicFieldItems(DynamicField field, List<DynamicFieldItem> items) {
        if (!field) return false

        if (!items) {
            deleteDynamicFieldItems(field)
            return true
        }

        def currentItems = getDynamicFieldItems(field)
        for (DynamicFieldItem item : items) {
            def found = currentItems.find { it.id == item.id } != null
            if (item.id == null || found) {
                item.field = field
                if (!saveDynamicFieldItem(item)) {
                    return false
                }
            }
        }
        for (DynamicFieldItem item : currentItems) {
            def notFound = items.find({ it.id != null && it.id == item.id }) == null
            if (notFound) item.delete()
        }

        return true
    }

    private void deleteDynamicFieldItems(DynamicField field) {
        DynamicFieldItem.executeUpdate('delete DynamicFieldItem where field = :field', [field: field])
    }

    /**
     * Gets the dynamic field item by it's id.
     * @param id the dynamic field item id.
     * @return the found dynamic field item or null.
     */
    @Transactional(readOnly = true)
    DynamicFieldItem getDynamicFieldItem(Long id) {
        DynamicFieldItem.get id
    }

    /**
     * Gets the list of dynamic field items, if dynamic field type is enumeration.
     *
     * @param dynamicField the dynamic field.
     * @return the list with found dynamic field items if any or empty list otherwise.
     */
    @Transactional(readOnly = true)
    List<DynamicFieldItem> getDynamicFieldItems(DynamicField dynamicField) {
        if (dynamicField && dynamicField.type == DynamicFieldType.SingleSelect) {
            return DynamicFieldItem.findAllByField(dynamicField)
        }
        return []
    }

    /**
     * Moves specified dynamic field to new position.
     * @param dynamicField the dynamic field.
     * @param newPosition the new position of dynamic field.
     * @return true if position was changed or wasn't need to change, otherwise false.
     */
    @Transactional
    boolean moveDynamicField(DynamicField dynamicField, int newPosition) {
        if (dynamicField) {
            def position = dynamicField.sequence
            if (newPosition == position) {
                return true
            }

            if (newPosition > position) {
                DynamicField.executeUpdate('update DynamicField set sequence = sequence - 1 ' +
                        'where owner = :owner and sequence > :position and sequence <= :newPosition',
                        [owner: dynamicField.owner, position: position, newPosition: newPosition])
            }
            else if (newPosition < position) {
                newPosition++
                DynamicField.executeUpdate('update DynamicField set sequence = sequence + 1 ' +
                        'where owner = :owner and sequence >= :newPosition and sequence < :position',
                        [owner: dynamicField.owner, position: position, newPosition: newPosition])
            }

            dynamicField.sequence = newPosition
            return saveDynamicField(dynamicField)
        }
        return false
    }

    /**
     * Saves dynamic field value.
     * @param value the dynamic field value.
     * @return true if dynamic field value saved, otherwise false.
     */
    @Transactional
    boolean saveDynamicFieldValue(DynamicFieldValue value) {
        ServiceUtil.saveOrRollback(value)
    }

    /**
     * Gets dynamic field value by id.
     * @param id the dynamic field value id.
     * @return the found dynamic field value.
     */
    @Transactional(readOnly = true)
    DynamicFieldValue getDynamicFieldValue(Long id) {
        DynamicFieldValue.get(id)
    }

    /**
     * Gets dynamic field values for subscriber.
     * @param subscriber the subscriber
     * @return the list with subscriber dynamic field values.
     */
    @Transactional(readOnly = true)
    List<DynamicFieldValue> getDynamicFieldValues(Subscriber subscriber) {
        if (subscriber && subscriber.id) {
            return DynamicFieldValue.findAllBySubscriber(subscriber)
        }
        return []
    }
}
