package com.github.monke.trees

import com.github.monke.nodes.BinaryTreeNode
import com.github.monke.trees.treeInterfaces.Search
import com.github.monke.trees.treeInterfaces.Insert
import com.github.monke.trees.treeInterfaces.Delete
import com.github.monke.trees.treeInterfaces.NodeArithmetic

abstract class BinaryTree<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>> :
    Search<K, V, N>,
    Insert<K, V, N>,
    Delete<K, V, N>,
    NodeArithmetic<K, V, N> {

    protected var rootNode: N? = null

    abstract operator fun iterator(): N
    operator fun get(key: K): V? {
        return this.search(key)?.value
    }
}
