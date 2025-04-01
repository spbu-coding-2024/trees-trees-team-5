package monke.nodes
/**
 * Red-Black Tree Search Node is used for RBT
 * It contains all necessary data to work with RBT
 *
 * @param K Universal comparable type for key storage
 * @param V Universal type for value storage
 * @param key The key which value will add to the tree
 * @param value The value which assign to key
 * @param parentNode parent of the current node
 * @param color node color
 */
public class RBTNode<K : Comparable<K>, V>(
    key: K,
    value: V,
    var parentNode: RBTNode<K, V>? = null,
    var color: Color = Color.RED,
) : BinaryTreeNode<K, V, RBTNode<K, V>>(key, value) {
    enum class Color {
        RED,
        BLACK,
    }
}
