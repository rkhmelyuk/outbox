package outbox.subscriber.search.criteria

/**
 * The tree that contains criteria. Tree is organized as binary tree.
 * Node can either be criterion node of concatenation node.
 *
 * @author Ruslan Khmelyuk
 */
class CriteriaTree {

    CriterionNode root
    CriterionNode last

    /**
     * Add new node the the criteria tree.
     * If node is null than is ignored and false returned.
     *
     * @param node the node to add.
     * @return true if added, otherwise false.
     */
    boolean addNode(CriterionNode node) {
        if (node == null) {
            return false
        }

        if (root == null) {
            root = node
            last = root
            return true
        }

        // Place criterion node correctly, so if use search for
        // and A, and B, or C
        // we make next A and (B or C)
        if (node.type != CriterionNodeType.Criterion) {
            if (last.type != CriterionNodeType.Criterion && last.right == null) {
                // if not end criterion, than set as right value and place correct
                // criterion node type
                last.type = node.type
                last.right = node.left
                last = last.right
            }
            else {
                // in the case if last criterion is end Criterion
                last.left = last.clone()
                last.right = node.left
                last.type = node.type
                last.criterion = null
            }
        }
        else {
            last.right = node
            last = last.right
        }
        return true
    }

    /**
     * Check whether criteria tree is empty, ie no node is in.
     * @return true if empty, otherwise false.
     */
    boolean isEmpty() {
        root == null
    }

    String toString() {
        def builder = new StringBuilder()
        printNode(builder, root, 0, ' ')
        return builder.toString()
    }

    private printNode(StringBuilder builder, CriterionNode node, int index, String dot) {
        if (node == null) return

        for (int i = 0; i < index; i++) print dot
        builder << node.type.name()

        if (node.type == CriterionNodeType.Criterion) {
            builder << " $node.criterion"
        }
        else {
            builder << '\n'
            printNode(builder, node.left, index + 1, 'L')
            printNode(builder, node.right, index + 1, 'R')
        }
        builder << '\n'
    }

}
