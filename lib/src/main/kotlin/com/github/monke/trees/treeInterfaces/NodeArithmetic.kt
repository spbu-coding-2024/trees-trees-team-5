package com.github.monke.trees.treeInterfaces

import com.github.monke.nodes.BinaryTreeNode
import com.github.monke.trees.BinaryTree

public interface NodeArithmetic<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>, T : BinaryTree<K, V, N, T>> {
    operator fun plus(tree: T): T

    operator fun minus(tree: T): T
}
