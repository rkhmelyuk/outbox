package outbox;

/**
 * The base application exception.
 *
 * @author Ruslan Khmelyuk
 * @since 2010-08-22
 */
public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
