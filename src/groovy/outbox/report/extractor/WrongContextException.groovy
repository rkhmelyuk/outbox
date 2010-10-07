package outbox.report.extractor

import outbox.AppException

/**
 * Extractor context is illegal.
 *
 * @author Ruslan Khmelyuk
 * @since 2010-10-07
 */
class WrongContextException extends AppException {

    public WrongContextException(String message) {
        super(message)
    }

}
