package monke.trees.treeInterfaces

import monke.nodes.BinaryTreeNode


public interface Insert<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>> {
    fun insert(key: K, value: V)
}
