package outbox

import org.springframework.transaction.NoTransactionException
import org.springframework.transaction.interceptor.TransactionAspectSupport

/**
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
class ServiceUtil {

    /**
     * Save domain object or rollback.
     * @param item the item to be saved.
     * @return true if saved, otherwise false.
     */
    static boolean saveOrRollback(def item) {
        if (!item || !item.validate()) {
            return false
        }

        boolean saved = (item.save(flush: true) != null)
        if (!saved) {
            setRollbackTransaction()
        }
        return saved
    }

    /**
     * Mark current transaction if any as Rollback Only
     */
    static boolean setRollbackTransaction() {
        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
            return true
        }
        catch (NoTransactionException e) {
            // ignore
            return false
        }
    }
}
