package outbox.campaign

import outbox.search.BaseConditionsBuilder

/**
 * The SearchCondition builder for Campaign. 
 *
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
class CampaignConditionsBuilder extends BaseConditionsBuilder {

    @Override protected Object createNode(Object name, Object value) {
        throw new UnsupportedOperationException("Operation $name is not supported.")
    }

    @Override protected Object createNode(Object name, Map attributes) {
        throw new UnsupportedOperationException("Operation $name is not supported.")
    }

    @Override protected Object createNode(Object name, Map attributes, Object value) {
        throw new UnsupportedOperationException("Operation $name is not supported.")
    }
    
}
