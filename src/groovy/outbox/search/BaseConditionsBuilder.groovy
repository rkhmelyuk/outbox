package outbox.search

import outbox.member.Member

/**
 * The base conditions builder class.
 * Other builders should extend it if need. 
 *
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
class BaseConditionsBuilder extends BuilderSupport {

    SearchConditions conditions = new SearchConditions()

    protected void setParent(Object parent, Object child) {

    }

    protected Object createNode(Object name) {
        if (name == 'build') {
            return conditions
        }

        throw new UnsupportedOperationException("Operation $name is not supported.")
    }

    protected Object createNode(Object name, Object value) {
        conditions
    }

    protected Object createNode(Object name, Map attributes) {
        conditions
    }

    protected Object createNode(Object name, Map attributes, Object value) {
        conditions
    }

    def order(String name, String sort) {
        def condition = conditions.get(OrderCondition, new OrderCondition())
        condition.order.put(name, sort)
        conditions.add condition
    }

    def ownedBy(Member member) {
        def condition = conditions.get(OwnedByCondition, new OwnedByCondition())
        condition.member = member
        conditions.add condition
    }

    def page(int page) {
        def condition = conditions.get(PageCondition, new PageCondition())
        condition.page = page
        conditions.add condition
    }

    def max(int max) {
        def condition = conditions.get(PageCondition, new PageCondition())
        condition.max = max
        conditions.add condition
    }
}
