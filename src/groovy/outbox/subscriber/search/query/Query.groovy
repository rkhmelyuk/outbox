package outbox.subscriber.search.query

import java.text.DecimalFormat
import outbox.subscriber.search.Order
import outbox.subscriber.search.criteria.*

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

    String toSelectSQL() {
        def builder = new StringBuilder()
        builder << 'select '

        if (distinct) {
            builder << 'distinct '
        }

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

        if (criteria && criteria.root) {
            // include criteria
            builder << ' where '
            buildCriteria(builder, criteria.root)
        }

        orders.eachWithIndex { order, index ->
            if (index != 0) {
                builder << ', '
            }
            else {
                builder << ' order by '
            }
            builder << "$order.column $order.sort.keyword"
        }

        return builder.toString()
    }

    String toCountSQL() {
        def builder = new StringBuilder()
        builder << 'select '

        if (distinct) {
            builder << 'distinct '
        }

        builder << 'count(*) as RowCount'

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

        if (criteria && criteria.root) {
            // include criteria
            builder << ' where '
            buildCriteria(builder, criteria.root)
        }

        return builder.toString()
    }


    void buildCriteria(StringBuilder builder, CriterionNode node) {
        if (node) {
            if (node.type == CriterionNodeType.Criterion) {
                def criterion = node.criterion
                if (criterion instanceof ComparisonCriterion) {
                    builder << comparisonCriterionSQL(criterion)
                }
                else if (criterion instanceof InSubqueryCriterion) {
                    builder << inSubqueryCriterionSQL(criterion)
                }
                else if (criterion instanceof InListCriterion) {
                    builder << inListCriterionSQL(criterion)
                }
            }
            else {
                def parens = node.left && node.right
                if (parens) {
                    builder << '('
                }
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
                if (parens) {
                    builder << ')'
                }
            }
        }
    }

    String comparisonCriterionSQL(ComparisonCriterion criterion) {
        def value = prepareValue(criterion.right)
        "$criterion.left$criterion.comparisonOp$value"
    }

    String inSubqueryCriterionSQL(InSubqueryCriterion criterion) {
        def op = criterion.not ? ' not in ' : ' in '
        "$criterion.left$op($criterion.subquery)"
    }

    String inListCriterionSQL(InListCriterion criterion) {
        def op = criterion.not ? ' not in ' : ' in '
        def list = ''
        criterion.values.eachWithIndex { value, index ->
            if (index != 0) {
                list += ', '
            }
            list += prepareValue(value)
        }
        "$criterion.left$op($list)"
    }

    private String prepareValue(Serializable value) {
        if (value instanceof String) {
            value = value.replaceAll(/\\?'/, "''")
            value = "'" + value + "'"
        }
        else if (value instanceof BigDecimal) {
            value = new DecimalFormat('#############.#####').format(value)
        }
        else if (value == null) {
            value = ' NULL '
        }
        return value
    }
}
