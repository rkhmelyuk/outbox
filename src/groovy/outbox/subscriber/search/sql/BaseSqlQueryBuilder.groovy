package outbox.subscriber.search.sql

import java.text.DecimalFormat
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.criteria.*

/**
 * Contains common functions.
 * 
 * @author Ruslan Khmelyuk
 */
abstract class BaseSqlQueryBuilder implements SqlQueryBuilder {

    /**
     * Builds SQL for specified criterion node and append to the query.
     *
     * @param query the SQL query string query.
     * @param node the node to build criteria for.
     */
    void buildCriteria(StringBuilder query, CriterionNode node) {
        if (node) {
            if (node.type == CriterionNodeType.Criterion) {
                def criterion = node.criterion
                if (criterion instanceof ComparisonCriterion) {
                    query << comparisonCriterionSQL(criterion)
                }
                else if (criterion instanceof InSubqueryCriterion) {
                    query << inSubqueryCriterionSQL(criterion)
                }
                else if (criterion instanceof InListCriterion) {
                    query << inListCriterionSQL(criterion)
                }
                else if (criterion instanceof SubqueryCriterion) {
                    query << subqueryCriterionSQL(criterion)
                }
            }
            else {
                // show parens only if both items and OR concatenation
                def parens = node.left && node.right && node.type == CriterionNodeType.Or

                if (parens) {
                    query << "("
                }

                if (node.left) {
                    buildCriteria(query, node.left)
                }
                if (node.right) {
                    concatenation(query, node)
                    buildCriteria(query, node.right)
                }

                if (parens) {
                    query << ")"
                }
            }
        }
    }

    private def concatenation(StringBuilder builder, CriterionNode node) {
        if (node.type == CriterionNodeType.And) {
            builder << ' and '
        }
        else if (node.type == CriterionNodeType.Or) {
            builder << ' or '
        }
        else if (node.type == CriterionNodeType.Not) {
            builder << ' not '
        }
    }

    /**
     * Builds simple comparison criterion as SQL string.
     *
     * @param criterion the criterion to build.
     * @return the result as string
     */
    String comparisonCriterionSQL(ComparisonCriterion criterion) {
        def leftValue = prepareValue(criterion.left)
        def rightValue = prepareValue(criterion.right)
        "$leftValue$criterion.comparisonOp$rightValue"
    }

    /**
     * Builds simple in-subquery criterion as SQL string.
     *
     * @param criterion the criterion to build.
     * @return the result as string
     */
    String inSubqueryCriterionSQL(InSubqueryCriterion criterion) {
        def op = criterion.not ? ' not in ' : ' in '
        def leftValue = prepareValue(criterion.left)
        "$leftValue$op($criterion.subquery)"
    }

    /**
     * Builds simple in-list criterion as SQL string.
     *
     * @param criterion the criterion to build.
     * @return the result as string
     */
    String inListCriterionSQL(InListCriterion criterion) {
        def op = criterion.not ? ' not in ' : ' in '
        def list = ''
        criterion.values.eachWithIndex { value, index ->
            if (index != 0) {
                list += ', '
            }
            list += prepareValue(value)
        }
        def leftValue = prepareValue(criterion.left)
        "$leftValue$op($list)"
    }

    /**
     * Builds simple subquery criterion as SQL string.
     *
     * @param criterion the criterion to build.
     * @return the result as string
     */
    String subqueryCriterionSQL(SubqueryCriterion criterion) {
        def subquery = new SelectSqlQueryBuilder().build(criterion.subquery)
        " $criterion.condition.keyword ($subquery) "
    }

    /**
     * Represents value as query part. Also adds anti-sql-inject check and replacements.
     * NOTE: contains PostgreSQL specific sql code.
     *
     * @param value the value to prepare.
     * @return the ready to insert into sql string.
     */
    String prepareValue(def value) {
        if (value instanceof String || value instanceof GString) {
            value = value.toString()
            value = value.replaceAll(/\\?'/, "''")
            value = "'" + value + "'"
        }
        else if (value instanceof BigDecimal) {
            value = new DecimalFormat('#############.#####').format(value)
        }
        else if (value instanceof Boolean) {
            value = value ? 'true' : 'false'
        }
        else if (value instanceof DynamicFieldItem) {
            value = Long.toString(value.id)
        }
        else if (value instanceof Column) {
            value = value.toSQL()
        }
        else if (value == null) {
            value = 'NULL'
        }
        return value
    }
}
