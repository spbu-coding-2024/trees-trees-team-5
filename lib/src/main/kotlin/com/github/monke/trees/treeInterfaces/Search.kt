package com.github.monke.trees.treeInterfaces

import com.github.monke.nodes.BinaryTreeNode

public interface Search<K : Comparable<K>, V, N : BinaryTreeNode<K, V>> {
    fun search(key: K): N?
}
