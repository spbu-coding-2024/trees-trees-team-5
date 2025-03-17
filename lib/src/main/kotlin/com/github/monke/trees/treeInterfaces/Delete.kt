package com.github.monke.trees.treeInterfaces

import com.github.monke.nodes.BinaryTreeNode

public interface Delete<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>> {
    fun delete(key: K): V?
    fun delete(node: N): Boolean {
        return delete(node.key) != null
    }
}
