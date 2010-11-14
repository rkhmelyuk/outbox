package outbox.subscriber.search.Query

import outbox.subscriber.search.criteria.ComparisonCriterion
import outbox.subscriber.search.criteria.CriterionNode
import outbox.subscriber.search.criteria.CriterionNodeType
import outbox.subscriber.search.query.Query

/**
 * @author Ruslan Khmelyuk
 */
class QueryTests extends GroovyTestCase {

    Query query

    @Override protected void setUp() {
        super.setUp()

        query = new Query()
    }

    void testSQL_Simple() {
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')

        assertEquals 'select FirstName as FirstName, LastName as lastName from Person as p', query.toSQL()
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

        assertEquals 'select FirstName as FirstName, LastName as lastName from Person as p where FirstName = \'John\'\'\' or LastName <> \'Doe\'', query.toSQL()
    }

    void testSQL_Number() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: 234334, comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName as FirstName, LastName as lastName from Person as p where FirstName = 234334', query.toSQL()
    }

    void testSQL_Long() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: 234334l, comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName as FirstName, LastName as lastName from Person as p where FirstName = 234334', query.toSQL()
    }

    void testSQL_BigDecimal() {
        def criterion = new ComparisonCriterion(left: 'FirstName', right: new BigDecimal(2343.34), comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        assertTrue query.addColumn('FirstName')
        assertTrue query.addColumn('LastName', 'lastName')
        assertTrue query.addTable('Person', 'p')
        assertTrue query.addCriterion(node)

        assertEquals 'select FirstName as FirstName, LastName as lastName from Person as p where FirstName = 2343.34', query.toSQL()
    }
}
