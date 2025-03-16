package com.github.monke.trees

import com.github.monke.nodes.AVLNode

public class AVLTree<K : Comparable<K>, V> : BinaryTree<K, V, AVLNode<K,V>>() {

    override fun search(key: K): AVLNode<K, V>? {
        TODO("Not yet implemented")
    }

    override fun insert(key: K, value: V) {
        TODO("Not yet implemented")
    }

    override fun delete(key: K): V? {
        TODO("Not yet implemented")
    }

    override operator fun iterator(): AVLNode<K, V> {
        TODO("Not implemented yet")
    }
}
