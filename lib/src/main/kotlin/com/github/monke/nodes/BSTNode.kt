package com.github.monke.nodes

public class BSTNode<K : Comparable<K>, V>(
    key: K,
    value: V,
) : BinaryTreeNode<K, V>(key, value)
