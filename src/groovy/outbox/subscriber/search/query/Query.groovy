package outbox.subscriber.search.query

import outbox.subscriber.search.Order
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.criteria.CriterionNode

/**
 * Represents single query, that can be built and translated to SQL.
 *
 * @author Ruslan Khmelyuk
 */
class Query {

    int page
    int perPage

    List<String> columns = []
    List<String> tables = []
    List<String> joins = []

    CriteriaTree criteria
    List<Order> orders = []

    boolean distinct = false

    boolean addColumn(String column, String alias = null) {
        if (column) {
            def value = column
            if (alias) {
                value += " as $alias"
            }
            return columns.add(value)
        }
        return false
    }

    boolean addTableColumn(String table, String column, String alias = null) {
        if (column) {
            def value = table + '.' + column
            if (alias) {
                value += " as $alias"
            }
            return columns.add(value)
        }
        return false
    }

    boolean addTable(String table, String alias = null) {
        if (table) {
            def value = table
            if (alias) {
                value += " as $alias"
            }
            return tables.add(value)
        }
        return false
    }

    boolean addJoin(String table, String alias, String condition) {
        if (table && alias && condition) {
            return joins.add(" join $table as $alias on $condition")
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
