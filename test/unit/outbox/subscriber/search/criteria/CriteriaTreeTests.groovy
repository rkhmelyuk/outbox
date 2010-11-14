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
        def node1 = new CriterionNode()
        def node2 = new CriterionNode()
        def node3 = new CriterionNode()
        assertTrue tree.addNode(node1)
        assertTrue tree.addNode(node2)
        assertTrue tree.addNode(node3)

        assertEquals node1, tree.root
        assertEquals node3, tree.last
        assertEquals node2, node1.right
        assertEquals node3, node2.right
    }

}
