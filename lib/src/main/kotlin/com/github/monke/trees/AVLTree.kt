package com.github.monke.trees

import com.github.monke.nodes.AVLNode

public class AVLTree<K : Comparable<K>, V> : BinaryTree<K, V, AVLNode<K, V>>() {
    override fun insert(key: K, value: V) {

    }

    override fun delete(key: K): V? {
        TODO("Not yet implemented")
    }

    override operator fun iterator(): AVLNode<K, V> {
        TODO("Not yet implemented")
    }

    private fun balanceFactor(node: AVLNode<K, V>): Int {
        val rightHeight: Int = node.rightChild?.height ?: 0
        val leftHeight: Int = node.leftChild?.height ?: 0
        return rightHeight - leftHeight
    }
}
