package outbox.subscription

import outbox.search.ArchivedCondition
import outbox.search.BaseConditionsBuilder

/**
 * Search conditions builder for {@link SubscriptionList}.
 *
 * @author Ruslan Khmelyuk
 */
class SubscriptionListConditionsBuilder extends BaseConditionsBuilder {

    def archived(boolean archived) {
        def condition = conditions.get(ArchivedCondition, new ArchivedCondition())
        condition.archived = archived
        conditions.add condition
    }
}
