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

        if (!root) {
            root = node
            last = root
            return true
        }

        last.right = node
        last = node
        return true
    }

    /**
     * Check whether criteria tree is empty, ie no node is in.
     * @return true if empty, otherwise false.
     */
    boolean isEmpty() {
        root == null
    }

}
