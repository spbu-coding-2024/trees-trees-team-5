package com.github.monke.trees.treeInterfaces

import com.github.monke.nodes.BinaryTreeNode
import com.github.monke.trees.BinaryTree

public interface NodeArithmetic<K : Comparable<K>, V, N : BinaryTreeNode<K, V>> {
    operator fun plus(node: N): BinaryTree<K, V, N> {
        TODO("Not yet implemented")
    }

    operator fun minus(node: N): BinaryTree<K, V, N> {
        TODO("Not yet implemented")
    }

    operator fun plusAssign(node: N) {
        TODO("Not yet implemented")
    }

    operator fun minusAssign(node: N) {
        TODO("Not yet implemented")
    }
}
