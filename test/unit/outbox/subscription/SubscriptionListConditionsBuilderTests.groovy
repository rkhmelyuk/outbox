package outbox.subscription

import outbox.search.ArchivedCondition

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionListConditionsBuilderTests extends GroovyTestCase {

    def condition = new SubscriptionListConditionsBuilder()

    void testArchived() {
        def conditions = condition.build {
            archived true
        }

        assertNotNull conditions
        assertTrue conditions.get(ArchivedCondition).archived
    }

    void testNotUsedInCampaign() {
        def campaign = null
        def conditions = condition.build {
            notUsedInCampaign campaign
        }

        assertNotNull conditions
        assertSame campaign, conditions.get(NotUsedInCampaignCondition).campaign
    }

}
