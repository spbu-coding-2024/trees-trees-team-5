package com.github.monke.trees.treeInterfaces

import com.github.monke.nodes.BinaryTreeNode
import com.github.monke.trees.BinaryTree

public interface NodeArithmetic<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>, T : BinaryTree<K, V, N, T>> {
    operator fun plus(tree: T): BinaryTree<K, V, N, T> {
        TODO("Not yet implemented")
    }

    operator fun minus(tree: T): BinaryTree<K, V, N, T> {
        TODO("Not yet implemented")
    }

    operator fun plusAssign(tree: T) {
        TODO("Not yet implemented")
    }

    operator fun minusAssign(tree: T) {
        TODO("Not yet implemented")
    }
}
