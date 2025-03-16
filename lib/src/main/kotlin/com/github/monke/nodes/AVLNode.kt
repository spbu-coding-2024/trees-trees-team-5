package com.github.monke.nodes

public class AVLNode<K : Comparable<K>, V>(
    key: K,
    value: V,
    val height: Int = 1,
) : BinaryTreeNode<K, V>(key, value)
