package monke.trees.treeInterfaces

import monke.nodes.BinaryTreeNode
import monke.trees.BinaryTree

public interface Delete<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>, T : BinaryTree<K, V, N, T>> {
    fun delete(key: K): V?

    fun delete(tree: T): Boolean {
        for (i in tree) {
            val (key, _) = i
            delete(key) ?: throw NoSuchElementException("Node $key not found")
        }
        return true
    }
}
