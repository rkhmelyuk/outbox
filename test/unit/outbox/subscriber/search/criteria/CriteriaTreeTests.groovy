package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class CriteriaTreeTests extends GroovyTestCase {

    void testAddNode() {
        def tree = new CriteriaTree()
        def node = new CriterionNode()
        assertTrue tree.addNode(node)

        assertEquals node, tree.root
        assertEquals node, tree.last
    }

    void testAddNode_Null() {
        def tree = new CriteriaTree()
        assertFalse tree.addNode(null)

        assertNull tree.root
        assertNull tree.last
    }

    void testAddNodes() {
        def tree = new CriteriaTree()
        def node1 = new CriterionNode(type: CriterionNodeType.And)
        def node2 = new CriterionNode(type: CriterionNodeType.Criterion)
        assertTrue tree.addNode(node1)
        assertTrue tree.addNode(node2)

        assertEquals node1, tree.root
        assertEquals node2, tree.last
        assertEquals node2, node1.right
    }

}
