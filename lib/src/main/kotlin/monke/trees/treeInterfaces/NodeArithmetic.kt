package monke.trees.treeInterfaces

import monke.nodes.BinaryTreeNode
import monke.trees.BinaryTree

public interface NodeArithmetic<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>, T : BinaryTree<K, V, N, T>> {
    operator fun plus(other: T): T?
    operator fun minus(other: T): T?
}
