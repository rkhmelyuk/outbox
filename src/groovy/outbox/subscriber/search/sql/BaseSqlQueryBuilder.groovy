package outbox.subscriber.search.sql

import java.text.DecimalFormat
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.criteria.*

/**
 * Contains common functions.
 * 
 * @author Ruslan Khmelyuk
 */
abstract class BaseSqlQueryBuilder implements SqlQueryBuilder {

    /**
     * Builds SQL for specified criterion node and append to the builder.
     *
     * @param builder the SQL query string builder.
     * @param node the node to build criteria for.
     */
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
                else if (criterion instanceof SubqueryCriterion) {
                    builder << subqueryCriterionSQL(criterion)
                }
            }
            else {
                def parens = node.left && node.right
                if (parens) {
                    builder << '('
                }
                if (node.left) {
                    if (node.type == CriterionNodeType.Not) {
                        builder << ' not '
                    }
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
        if (value instanceof String) {
            value = value.replaceAll(/\\?'/, "''")
            value = "'" + value + "'"
        }
        else if (value instanceof BigDecimal) {
            value = new DecimalFormat('#############.#####').format(value)
        }
        else if (value instanceof Boolean) {
            value = value ? 'true' : 'false'
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
