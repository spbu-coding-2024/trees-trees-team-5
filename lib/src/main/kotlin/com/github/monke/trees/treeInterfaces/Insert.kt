package com.github.monke.trees.treeInterfaces

import com.github.monke.nodes.BinaryTreeNode


public interface Insert<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>> {
    fun insert(key: K, value: V)
}
