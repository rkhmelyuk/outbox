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

    String comparisonCriterionSQL(ComparisonCriterion criterion) {
        def leftValue = prepareValue(criterion.left)
        def rightValue = prepareValue(criterion.right)
        "$leftValue$criterion.comparisonOp$rightValue"
    }

    String inSubqueryCriterionSQL(InSubqueryCriterion criterion) {
        def op = criterion.not ? ' not in ' : ' in '
        def leftValue = prepareValue(criterion.left)
        "$leftValue$op($criterion.subquery)"
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
        def leftValue = prepareValue(criterion.left)
        "$leftValue$op($list)"
    }

    String subqueryCriterionSQL(SubqueryCriterion criterion) {
        def subquery = new SelectSqlQueryBuilder().build(criterion.subquery)
        " $criterion.condition.keyword ($subquery) "
    }

    String prepareValue(Serializable value) {
        if (value instanceof String) {
            value = value.replaceAll(/\\?'/, "''")
            value = "'" + value + "'"
        }
        else if (value instanceof BigDecimal) {
            value = new DecimalFormat('#############.#####').format(value)
        }
        else if (value instanceof Boolean) {
            value = value ? 1 : 0
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
