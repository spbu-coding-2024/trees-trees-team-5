package com.github.monke.nodes

public class RBTNode<K : Comparable<K>, V>(
    key: K,
    value: V,
    val parentNode: RBTNode<K, V>? = null,
    val color: Color = Color.RED,
) : BinaryTreeNode<K, V, RBTNode<K,V>>(key, value) {
    enum class Color {
        RED,
        BLACK,
    }
}
