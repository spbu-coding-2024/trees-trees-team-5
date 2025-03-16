package com.github.monke.trees

import com.github.monke.nodes.BSTNode

public class BSTree<K : Comparable<K>, V> : BinaryTree<K, V, BSTNode<K, V>>() {
    override fun insert(key: K, value: V) {
        TODO("Not yet implemented")
    }

    override fun delete(key: K): V? {
        TODO("Not yet implemented")
    }

    override operator fun iterator(): BSTNode<K, V> {
        TODO("Not yet implemented")
    }
}
