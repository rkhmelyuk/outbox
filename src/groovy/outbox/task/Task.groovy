package outbox.task

/**
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class Task implements Serializable {

    String name
    Integer version

    Map params
<<<<<<< HEAD

    @Override String toString() {
        "Task[name: $name, version: $version, params: $params"
    }


=======
>>>>>>> cec0eb1... #20: Send Campaign: initial commit.
}
