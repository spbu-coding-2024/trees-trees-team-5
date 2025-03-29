package monke.nodes

/**
 * The base abstract binary tree node
 * It contains all necessary data to work with binary tree
 *
 * @param K Universal comparable type for key storage
 * @param V Universal type for value storage
 * @param N Universal type for child node type
 * @param key The key which value will add to the tree
 * @param value The value which assign to key
 */
public abstract class BinaryTreeNode<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>>(
    val key: K,
    val value: V,
) {
    var leftChild: N? = null
    var rightChild: N? = null
}
