package com.github.monke.trees.treeInterfaces

import com.github.monke.nodes.BinaryTreeNode

public interface Insert<K : Comparable<K>, V, N : BinaryTreeNode<K, V>> {
    fun insert(key: K, value: V)
    fun insert(node: N) {
        insert(node.key, node.value)
    }
}
