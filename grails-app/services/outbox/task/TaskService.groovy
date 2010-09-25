package outbox.task

/**
 * @author Ruslan Khmelyuk
 */
class TaskService {

    static transactional = false

    boolean enqueueTask(Task task) {
        // TODO - should we store task before forward further?
        false
    }
}
