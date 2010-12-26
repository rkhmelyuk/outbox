package outbox.subscriber.search.sql

import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.search.query.Query
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Table
import outbox.subscriber.search.criteria.*

/**
 * Testing common functions.
 * 
 * @author Ruslan Khmelyuk
 */
class BaseSqlQueryBuilderTests extends GroovyTestCase {

    void testBuildCriteria_None() {
        def builder = new CountSqlQueryBuilder()

        def string = new StringBuilder()
        builder.buildCriteria string, null
        assertTrue string.length() == 0
    }

    void testPrepareValue() {
        def builder = new CountSqlQueryBuilder()

        def val = "test"
        assertEquals "'test'", builder.prepareValue("$val")
        assertEquals "'test'", builder.prepareValue('test')
        assertEquals "'tes''t'", builder.prepareValue('tes\'t')
        assertEquals '1023', builder.prepareValue(1023)
        assertEquals '1023.12', builder.prepareValue(1023.1200)
        assertEquals 'T.C', builder.prepareValue(new Column('T', 'C'))
        assertEquals 'NULL', builder.prepareValue(null)
        assertEquals '23', builder.prepareValue(new DynamicFieldItem(id: 23))
        assertEquals '12323', builder.prepareValue(new DynamicFieldItem(id: 12323))
    }

    void testComparisonCriterionSQL() {
        def criterion = new ComparisonCriterion()
        criterion.left = new Column((String) null, 'left')
        criterion.right = 'right'
        criterion.comparisonOp = '='

        def builder = new CountSqlQueryBuilder()
        def result = builder.comparisonCriterionSQL(criterion)

        assertEquals "left='right'", result
    }

    void testLikeComparisonCriterionSQL() {
        def criterion = new ComparisonCriterion()
        criterion.left = new Column((String) null, 'left')
        criterion.right = '%right%'
        criterion.comparisonOp = ' like '

        def builder = new CountSqlQueryBuilder()
        def result = builder.comparisonCriterionSQL(criterion)

        assertEquals "left like '%right%'", result
    }

    void testInSubqueryCriterionSQL() {
        def criterion = new InSubqueryCriterion()
        criterion.left = new Column((String) null, 'left')
        criterion.subquery = 'subquery'
        criterion.not = false

        def builder = new CountSqlQueryBuilder()
        def result = builder.inSubqueryCriterionSQL(criterion)

        assertEquals "left in (subquery)", result
    }

    void testNotInSubqueryCriterionSQL() {
        def criterion = new InSubqueryCriterion()
        criterion.left = new Column((String) null, 'left')
        criterion.subquery = 'subquery'
        criterion.not = true

        def builder = new CountSqlQueryBuilder()
        def result = builder.inSubqueryCriterionSQL(criterion)

        assertEquals "left not in (subquery)", result
    }

    void testInListCriterionSQL() {
        def criterion = new InListCriterion()
        criterion.left = new Column((String) null, 'left')
        criterion.values = [10, 20, 30]
        criterion.not = false

        def builder = new CountSqlQueryBuilder()
        def result = builder.inListCriterionSQL(criterion)

        assertEquals "left in (10, 20, 30)", result
    }

    void testNotInListCriterionSQL() {
        def criterion = new InListCriterion()
        criterion.left = new Column((String) null, 'left')
        criterion.values = ['10', '20', '30']
        criterion.not = true

        def builder = new CountSqlQueryBuilder()
        def result = builder.inListCriterionSQL(criterion)

        assertEquals "left not in ('10', '20', '30')", result
    }

    void testSubqueryCriterionSQL() {
        def subquery = new Query()
        subquery.addColumn(new Column('table', 'column'))
        subquery.addTable(new Table('table'))

        def criterion = new SubqueryCriterion()
        criterion.subquery = subquery
        criterion.condition = SubqueryConditionType.Exists

        def builder = new CountSqlQueryBuilder()
        def result = builder.subqueryCriterionSQL(criterion)

        assertEquals " exists (select table.column from table) ", result
    }
}
