package com.github.monke.nodes

public abstract class BinaryTreeNode<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>>(
    val key: K,
    val value: V,
) {
    var leftChild: BinaryTreeNode<K, V, N>? = null
    var rightChild: BinaryTreeNode<K, V, N>? = null
}
