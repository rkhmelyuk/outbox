package outbox.mail

import outbox.AppException

/**
 * Error to send email. 
 *
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class EmailException extends AppException {

    def EmailException(message) {
        super(message);
    }

    def EmailException(message, cause) {
        super(message, cause);
    }
}
