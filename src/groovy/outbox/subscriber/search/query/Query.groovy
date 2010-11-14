package outbox.subscriber.search.query

import java.text.DecimalFormat
import outbox.subscriber.search.criteria.ComparisonCriterion
import outbox.subscriber.search.criteria.CriteriaTree
import outbox.subscriber.search.criteria.CriterionNode
import outbox.subscriber.search.criteria.CriterionNodeType

/**
 * Represents single query, that can be built and translated to SQL.
 *
 * @author Ruslan Khmelyuk
 */
class Query {

    List<String> columns = []
    List<String> tables = []
    List<String> joins = []

    CriteriaTree criteria

    boolean addColumn(String column, String alias = null) {
        if (column) {
            return columns.add("$column as ${alias ?: column}")
        }
        return false
    }

    boolean addTable(String table, String alias = null) {
        if (table) {
            return tables.add("$table as ${alias ?: table}")
        }
        return false
    }

    boolean addJoin(String table, String alias, String condition) {
        if (table && condition) {
            return joins.add(" join $table as ${alias ?: table} on $condition")
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

    String toSQL() {
        def builder = new StringBuilder()
        builder << 'select '

        columns.eachWithIndex { column, index ->
            if (index != 0) {
                builder << ', '
            }
            builder << column
        }

        builder << ' from '
        tables.eachWithIndex { table, index ->
            if (index != 0) {
                builder << ', '
            }
            builder << table
        }

        joins.eachWithIndex { join, index ->
            if (index != 0) {
                builder << ', '
            }
            builder << join
        }

        if (criteria) {
            // include criteria
            builder << ' where '
            buildCriteria(builder, criteria.root)
        }

        return builder.toString()
    }

    void buildCriteria(StringBuilder builder, CriterionNode node) {
        if (node.type == CriterionNodeType.Criterion) {
            def criterion = node.criterion
            if (criterion instanceof ComparisonCriterion) {
                builder << comparisonCriterionSQL(criterion)
            }
        }
        else {
            if (node.left) {
                buildCriteria(builder, node.left)
            }
            if (node.right) {
                if (node.type == CriterionNodeType.And) {
                    builder << ' and '
                }
                else if (node.type == CriterionNodeType.Or) {
                    builder << ' or '
                }
                buildCriteria(builder, node.right)
            }
        }
    }

    String comparisonCriterionSQL(ComparisonCriterion criterion) {
        def value = criterion.right
        if (value instanceof String) {
            value = value.replaceAll(/\\?'/, "''")
            value = "'" + value + "'"
        }
        else if (value instanceof BigDecimal) {
            value = new DecimalFormat('#############.#####').format(value)
        }
        "$criterion.left$criterion.comparisonOp$value"
    }
}