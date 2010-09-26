package outbox.task

/**
 * @author Ruslan Khmelyuk
 */
class TaskService {

    static transactional = false

<<<<<<< HEAD
    TaskProcessor sendCampaignTaskProcessor

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
        Thread.sleep(1000)
        switch (task.name) {
            case 'SendCampaign':
                sendCampaignTaskProcessor.process(task)
                break;
            default:
                log.error("Unexpected task $task");
        }
=======
    boolean enqueueTask(Task task) {
        // TODO - should we store task before forward further?
        false
>>>>>>> cec0eb1... #20: Send Campaign: initial commit.
    }
}
