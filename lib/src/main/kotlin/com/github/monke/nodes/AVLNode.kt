package com.github.monke.nodes

public class AVLNode<K : Comparable<K>, V>(
    key: K,
    value: V,
    var height: Int = 1,
) : BinaryTreeNode<K, V, AVLNode<K, V>>(key, value)
