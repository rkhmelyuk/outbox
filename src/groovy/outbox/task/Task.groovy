package outbox.task

/**
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class Task implements Serializable {

    String name
    Integer version

    Map params
}
