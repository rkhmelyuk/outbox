package outbox.subscriber.search.criteria

/**
 * @author Ruslan Khmelyuk
 */
class CriterionNodeTests extends GroovyTestCase {

    void testFields() {
        def criterion = new ComparisonCriterion()
        def leftNode = new CriterionNode()
        def rightNode = new CriterionNode()

        def node = new CriterionNode()
        node.type = CriterionNodeType.And
        node.criterion = criterion
        node.left = leftNode
        node.right = rightNode

        assertEquals CriterionNodeType.And, node.type
        assertEquals criterion, node.criterion
        assertEquals leftNode, node.left
        assertEquals rightNode, node.right
    }
}
