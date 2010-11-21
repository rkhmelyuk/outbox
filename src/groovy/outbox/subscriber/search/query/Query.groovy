package outbox.subscriber.search.query

import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.criteria.CriterionNode
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Join
import outbox.subscriber.search.query.elems.Order
import outbox.subscriber.search.query.elems.Table

/**
 * Represents single query, that can be built and translated to SQL.
 *
 * @author Ruslan Khmelyuk
 */
class Query {

    int page
    int perPage

    List<Column> columns = []
    List<Table> tables = []
    List<Join> joins = []
    List<Order> orders = []

    CriteriaTree criteria

    boolean distinct = false

    boolean addColumn(Column column) {
        if (column) {
            return columns.add(column)
        }
        return false
    }

    boolean addTable(Table table) {
        if (table) {
            return tables.add(table)
        }
        return false
    }

    boolean addJoin(Join join) {
        if (join) {
            return joins.add(join)
        }
        return false
    }

    boolean addOrder(Order order) {
        if (order) {
            return orders.add(order)
        }
        return false
    }

    boolean addCriterion(CriterionNode criterion) {
        if (criterion) {
            if (!criteria) {
                criteria = new CriteriaTree()
            }
            return criteria.addNode(criterion)
        }
        return false
    }

}
