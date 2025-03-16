package com.github.monke.nodes

public abstract class BinaryTreeNode<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>>(
    val key: K,
    val value: V,
) {
    var leftChild: N? = null
    var rightChild: N? = null
}
