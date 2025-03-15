package com.github.monke.trees

import com.github.monke.nodes.AVLNode

public class AVLTree<K : Comparable<K>, V, N : AVLNode<K, V>> : BinaryTree<K, V, N>() {
    override fun search(key: K): N? {
        TODO("Not yet implemented")
    }

    override fun insert(key: K, value: V) {
        TODO("Not yet implemented")
    }

    override fun delete(key: K): V? {
        TODO("Not yet implemented")
    }

    override operator fun iterator(): N {
        TODO("Not implemented yet")
    }
}
