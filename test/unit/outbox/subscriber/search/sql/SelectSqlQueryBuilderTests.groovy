package outbox.subscriber.search.sql

import outbox.subscriber.search.Sort
import outbox.subscriber.search.criteria.ComparisonCriterion
import outbox.subscriber.search.criteria.CriterionNode
import outbox.subscriber.search.criteria.CriterionNodeType
import outbox.subscriber.search.criteria.InSubqueryCriterion
import outbox.subscriber.search.query.Query
import outbox.subscriber.search.query.elems.Column
import outbox.subscriber.search.query.elems.Join
import outbox.subscriber.search.query.elems.Order
import outbox.subscriber.search.query.elems.Table

/**
 * @author Ruslan Khmelyuk
 */
class SelectSqlQueryBuilderTests extends GroovyTestCase {
    
    Query query
    SelectSqlQueryBuilder builder
    
    @Override protected void setUp() {
        super.setUp()
        
        query = new Query()
        builder = new SelectSqlQueryBuilder()
    }

    void testSQL_Simple() {
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p', builder.build(query)
    }

    void testSQL_Join() {
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addJoin(new Join(new Table('T'), 'condition'))

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p join T on (condition)', builder.build(query)
    }

    void testSQL_Distinct() {
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        query.distinct = true

        assertEquals 'select distinct p.FirstName, p.LastName as lastName from Person as p', builder.build(query)
    }

    void testSQL_Order() {
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        query.orders << new Order(new Column(table, 'FirstName'), Sort.Asc)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'order by p.FirstName asc', builder.build(query)
    }

    void testSQL_Orders() {
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        query.orders << new Order(new Column(table, 'FirstName'), Sort.Asc)
        query.orders << new Order(new Column(table, 'LastName'), Sort.Desc)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'order by p.FirstName asc, p.LastName desc', builder.build(query)
    }

    void testSQL_Conditions() {
        def criterion1 = new ComparisonCriterion(left: new Column('', 'FirstName'), right: 'John\'', comparisonOp: ' = ')
        def criterion2 = new ComparisonCriterion(left: new Column('', 'LastName'), right: 'Doe', comparisonOp: ' <> ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion1)
        def leftNode2 = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion2)
        def rightNode = new CriterionNode(type: CriterionNodeType.Or, left: leftNode2)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode, right: rightNode)
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addCriterion(node)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p where 1=1  ' +
                'and (FirstName = \'John\'\'\' or LastName <> \'Doe\')', builder.build(query)
    }

    void testSQL_Injection1() {
        def criterion = new ComparisonCriterion(
                left: new Column('', 'FirstName'),
                right: "Join'", comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addCriterion(node)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'where 1=1  and FirstName = \'Join\'\'\'', builder.build(query)
    }

    void testSQL_Injection2() {
        def criterion = new ComparisonCriterion(
                left: new Column('', 'FirstName'),
                right: "Join\'", comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addCriterion(node)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'where 1=1  and FirstName = \'Join\'\'\'', builder.build(query)
    }

    void testSQL_Injection3() {
        def criterion = new ComparisonCriterion(
                left: new Column('', 'FirstName'),
                right: "Join\''", comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addCriterion(node)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'where 1=1  and FirstName = \'Join\'\'\'\'\'', builder.build(query)
    }

    void testSQL_Number() {
        def criterion = new ComparisonCriterion(
                left: new Column('', 'FirstName'),
                right: 234334, comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addCriterion(node)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'where 1=1  and FirstName = 234334', builder.build(query)
    }

    void testSQL_Long() {
        def criterion = new ComparisonCriterion(
                left: new Column('', 'FirstName'),
                right: 234334l, comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addCriterion(node)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'where 1=1  and FirstName = 234334', builder.build(query)
    }

    void testSQL_BigDecimal() {
        def criterion = new ComparisonCriterion(
                left: new Column('', 'FirstName'),
                right: new BigDecimal(2343.34), comparisonOp: ' = ')
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addCriterion(node)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'where 1=1  and FirstName = 2343.34', builder.build(query)
    }

    void testSQL_Subquery() {
        def criterion = new InSubqueryCriterion(left: new Column('', 'FirstName'), subquery: 'subquery', not: false)
        def leftNode = new CriterionNode(type: CriterionNodeType.Criterion, criterion: criterion)
        def node = new CriterionNode(type: CriterionNodeType.And, left: leftNode)
        def table = new Table('Person', 'p')
        assertTrue query.addTable(table)
        assertTrue query.addColumn(new Column(table, 'FirstName'))
        assertTrue query.addColumn(new Column(table, 'LastName', 'lastName'))
        assertTrue query.addCriterion(node)

        assertEquals 'select p.FirstName, p.LastName as lastName from Person as p ' +
                'where 1=1  and FirstName in (subquery)', builder.build(query)
    }
    
}
