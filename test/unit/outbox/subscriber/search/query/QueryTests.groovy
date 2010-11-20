package outbox.subscriber.search.query

import outbox.subscriber.search.Order
import outbox.subscriber.search.Sort
import outbox.subscriber.search.criteria.ComparisonCriterion
import outbox.subscriber.search.criteria.CriterionNode
import outbox.subscriber.search.criteria.CriterionNodeType
import outbox.subscriber.search.criteria.InSubqueryCriterion

/**
 * @author Ruslan Khmelyuk
 */
class QueryTests extends GroovyTestCase {

    Query query

    @Override protected void setUp() {
        super.setUp()

        query = new Query()
    }

    void testFields() {
        query.page = 10
        query.perPage = 15

        assertEquals 10, query.page
        assertEquals 15, query.perPage
    }

    void testSQL_Simple() {
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')

        assertEquals 'select FirstName, LastName as lastName from Person as p', query.toSelectSQL()
    }

    void testSQL_TableColumn() {
        assertTrue query.addTableColumn('p', 'FirstName')
        assertTrue query.addTableColumn('p', 'LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p', query.toSelectSQL()
    }

    void testSQL_Count() {
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')

        assertEquals 'select count(*) as RowCount from Person as p', query.toCountSQL()
    }

    void testSQL_Distinct() {
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        query.distinct = true

        assertEquals 'select distinct FirstName, LastName as lastName from Person as p', query.toSelectSQL()
    }

    void testSQL_DistinctCount() {
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        query.distinct = true

        assertEquals 'select distinct count(*) as RowCount from Person as p', query.toCountSQL()
    }

    void testSQL_Order() {
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        query.orders << new Order(column: 'FirstName', sort: Sort.Asc)

        assertEquals 'select FirstName, LastName as lastName from Person as p order by FirstName asc', query.toSelectSQL()
    }

    void testSQL_Orders() {
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        query.orders << new Order(column: 'FirstName', sort: Sort.Asc)
        query.orders << new Order(column: 'LastName', sort: Sort.Desc)

        assertEquals 'select FirstName, LastName as lastName from Person as p order by FirstName asc, LastName desc', query.toSelectSQL()
    }

    void testSQL_Conditions() {
        def criterion1 = new ComparisonCriterion(left: 'FirstName', right: 'John\'', comparisonOp: ' = ')
        def criterion2 = new ComparisonCriterion(left: 'LastName', right: 'Doe', comparisonOp: ' <> ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion1)
        def leftNode2 = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion2)
        def rightNode = new CriterionNode(type: CriterionNodeType.And, left: leftNode2)
        def node = new CriterionNode(type: CriterionNodeType.Or, left: leftNode, right: rightNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName, LastName as lastName from Person as p where (FirstName = \'John\'\'\' or LastName <> \'Doe\')', query.toSelectSQL()
    }

    void testSQL_Injection1() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: "Join'", comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName, LastName as lastName from Person as p where FirstName = \'Join\'\'\'', query.toSelectSQL()
    }

    void testSQL_Injection2() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: "Join\'", comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName, LastName as lastName from Person as p where FirstName = \'Join\'\'\'', query.toSelectSQL()
    }

    void testSQL_Injection3() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: "Join\''", comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName, LastName as lastName from Person as p where FirstName = \'Join\'\'\'\'\'', query.toSelectSQL()
    }

    void testSQL_Number() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: 234334, comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName, LastName as lastName from Person as p where FirstName = 234334', query.toSelectSQL()
    }

    void testSQL_Long() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: 234334l, comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName, LastName as lastName from Person as p where FirstName = 234334', query.toSelectSQL()
    }

    void testSQL_BigDecimal() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: new BigDecimal(2343.34), comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName, LastName as lastName from Person as p where FirstName = 2343.34', query.toSelectSQL()
    }

    void testSQL_Subquery() {
        def criterion = new InSubqueryCriterion(left: 'FirstName', subquery: 'subquery', not: false)
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName, LastName as lastName from Person as p where FirstName in (subquery)', query.toSelectSQL()
    }
}
