package outbox.task

/**
 * @author Ruslan Khmelyuk
 */
class TaskService {

    static transactional = false

    TaskProcessor sendCampaignTaskProcessor
    TaskProcessor removeDynamicFieldTaskProcessor

    boolean enqueueTask(Task task) {
        if (!task) {
            return false
        }

        try {
            Thread.start { handler(task) }
            log.info("Enqueued task $task")
            return true
        }
        catch (Exception e) {
            log.info("Failed to enqueue task $task", e)
            return false
        }
    }

    void handler(Task task) {
        switch (task.name) {
            case 'SendCampaign':
                sendCampaignTaskProcessor.process(task)
                break;
            case 'RemoveDynamicField':
                removeDynamicFieldTaskProcessor.process(task)
                break;
            default:
                log.error("Unexpected task $task");
        }
    }
}
