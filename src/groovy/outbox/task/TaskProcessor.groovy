package outbox.task

/**
 * The interface for task processor.
 *
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
public interface TaskProcessor {
    
    void process(Task task)

}
