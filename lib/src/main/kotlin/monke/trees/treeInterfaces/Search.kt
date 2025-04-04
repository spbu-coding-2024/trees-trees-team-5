package monke.trees.treeInterfaces

import monke.nodes.BinaryTreeNode

public interface Search<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>> {
    fun search(key: K): V?
}
