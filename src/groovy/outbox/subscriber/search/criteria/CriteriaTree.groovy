package outbox.subscriber.search.criteria

/**
 * The tree that contains criteria.
 * 
 * @author Ruslan Khmelyuk
 */
class CriteriaTree {

    CriterionNode root
    CriterionNode last

    boolean addNode(CriterionNode node) {
        if (!node) {
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

    boolean isEmpty() {
        root == null
    }

}
