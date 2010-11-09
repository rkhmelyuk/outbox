package outbox.task

import org.apache.log4j.Logger
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldStatus

/**
 * Process task for removing dynamic fields.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-11-09
 */
class RemoveDynamicFieldTaskProcessor implements TaskProcessor {

    static final Logger log = Logger.getLogger(RemoveDynamicFieldTaskProcessor)

    DynamicFieldService dynamicFieldService

    void process(Task task) {
        def dynamicFieldId = task?.params?.dynamicFieldId
        def dynamicField = dynamicFieldService.getDynamicField(dynamicFieldId)

        if (dynamicField == null || dynamicField.status != DynamicFieldStatus.Removed)  {
            log.warn "Will not remove $dynamicField"
            return
        }

        // in single transaction
        DynamicField.withSession {
            DynamicField.withTransaction {
                // remove dynamic field values
                dynamicFieldService.deleteDynamicFieldValues(dynamicField)
                // remove dynamic field items
                dynamicFieldService.deleteDynamicFieldItems(dynamicField)
                // remove dynamic field
                dynamicFieldService.deleteRemovedDynamicField(dynamicField)

                log.info "Successfully removed $dynamicField"
            }
        }
    }

}
