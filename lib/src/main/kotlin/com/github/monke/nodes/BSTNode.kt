package com.github.monke.nodes

import java.util.Stack

public class BSTNode<K : Comparable<K>, V>(
    key: K,
    value: V,
) : BinaryTreeNode<K, V, BSTNode<K, V>>(key, value) {
    fun copy(): BSTNode<K, V> {
        val stack = Stack<Pair<BSTNode<K, V>, BSTNode<K, V>>>()
        val copyNode = BSTNode(key, value)

        this.leftChild?.let { leftChild ->
            val copiedLeft = BSTNode(leftChild.key, leftChild.value)
            copyNode.leftChild = copiedLeft
            stack.push(leftChild to copiedLeft)
        }

        this.rightChild?.let { rightChild ->
            val copiedRight = BSTNode(rightChild.key, rightChild.value)
            copyNode.rightChild = copiedRight
            stack.push(rightChild to copiedRight)
        }

        while (stack.isNotEmpty()) {
            val (originalNode, copiedNode) = stack.pop()

            originalNode.leftChild?.let { leftChild ->
                val copiedLeft = BSTNode(leftChild.key, leftChild.value)
                copiedNode.leftChild = copiedLeft
                stack.push(leftChild to copiedLeft)
            }

            originalNode.rightChild?.let { rightChild ->
                val copiedRight = BSTNode(rightChild.key, rightChild.value)
                copiedNode.rightChild = copiedRight
                stack.push(rightChild to copiedRight)
            }
        }

        return copyNode
    }
}
