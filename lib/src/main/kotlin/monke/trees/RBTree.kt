package com.github.monke.trees

import com.github.monke.nodes.RBTNode

public class RBTree<K : Comparable<K>, V> : BinaryTree<K, V, RBTNode<K, V>, RBTree<K, V>>() {
    override fun insert(key: K, value: V) {
        TODO("Not yet implemented")
    }

    override fun delete(key: K): V? {
        TODO("Not yet implemented")
    }
}
