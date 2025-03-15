package com.github.monke.nodes

public abstract class BinaryTreeNode<K : Comparable<K>, V>(
    val key: K,
    val value: V,
) {
    var leftChild: BinaryTreeNode<K, V>? = null
    var rightChild: BinaryTreeNode<K, V>? = null
}
