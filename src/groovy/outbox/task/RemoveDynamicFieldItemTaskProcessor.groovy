package outbox.task

import org.apache.log4j.Logger
import outbox.subscriber.DynamicFieldService
import outbox.subscriber.field.DynamicFieldItem

/**
 * Process task for removing dynamic field item.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-11-09
 */
class RemoveDynamicFieldItemTaskProcessor implements TaskProcessor {

    static final Logger log = Logger.getLogger(RemoveDynamicFieldItemTaskProcessor)

    DynamicFieldService dynamicFieldService

    void process(Task task) {
        def dynamicFieldItemId = task?.params?.dynamicFieldItemId
        def dynamicFieldItem = dynamicFieldService.getDynamicFieldItem(dynamicFieldItemId)

        if (dynamicFieldItem == null || !dynamicFieldItem.removed) {
            log.warn "Will not remove $dynamicFieldItem"
            return
        }

        // in single transaction
        DynamicFieldItem.withSession {
            // remove dynamic field item
            dynamicFieldService.deleteRemovedDynamicFieldItem(dynamicFieldItem)

            log.info "Successfully removed $dynamicFieldItem"
        }
    }

}
