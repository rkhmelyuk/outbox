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

    Integer page
    Integer perPage

    /**
     * List of columns to return by select.
     */
    List<Column> columns = []

    /**
     * List of tables.
     */
    List<Table> tables = []

    /**
     * List of joins.
     */
    List<Join> joins = []

    /**
     * List of orders.
     */
    List<Order> orders = []

    /**
     * The criteria tree with search conditions.
     */
    CriteriaTree criteria

    /**
     * Whether distinct results.
     */
    boolean distinct = false

    /**
     * Add column to query select part.
     *
     * @param column the column to add.
     * @return true if added, otherwise false.
     */
    boolean addColumn(Column column) {
        if (column) {
            return columns.add(column)
        }
        return false
    }

    /**
     * Add table to the query from part.
     *
     * @param table the table to add.
     * @return true if added, otherwise false.
     */
    boolean addTable(Table table) {
        if (table) {
            return tables.add(table)
        }
        return false
    }

    /**
     * Add join to the query.
     *
     * @param join the join to add.
     * @return true if added, otherwise false.
     */
    boolean addJoin(Join join) {
        if (join) {
            return joins.add(join)
        }
        return false
    }

    /**
     * Add order to the query.
     *
     * @param order the order to add.
     * @return true if added, otherwise false.
     */
    boolean addOrder(Order order) {
        if (order) {
            return orders.add(order)
        }
        return false
    }

    /**
     * Add criterion to the query where part.
     *
     * @param criterion the criterion to add.
     * @return true if added, otherwise false.
     */
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
