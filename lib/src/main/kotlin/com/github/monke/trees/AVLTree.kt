package com.github.monke.trees

import com.github.monke.nodes.AVLNode
import java.util.Stack

public class AVLTree<K : Comparable<K>, V> : BinaryTree<K, V, AVLNode<K, V>>() {
    override fun insert(key: K, value: V) {
        val path: Stack<AVLNode<K, V>>? = searchPath(key)

        if (path == null) {
            throw IllegalArgumentException("Node with such key already exists")
        } else {
            val newNode: AVLNode<K, V> = AVLNode(key, value)
            if (path.isEmpty()) {
                rootNode = newNode
            } else {
                val parentNode: AVLNode<K, V> = path.peek()
                if (key < parentNode.key) {
                    parentNode.leftChild = newNode
                } else if (key > parentNode.key) {
                    parentNode.rightChild = newNode
                }

                while (path.isNotEmpty()) {
                    val pathNode: AVLNode<K, V> = path.pop()
                    pathNode.rebalance()
                }
            }
        }
    }

    override fun delete(key: K): V? {
        TODO("Not yet implemented")
    }

    override operator fun iterator(): AVLNode<K, V> {
        TODO("Not yet implemented")
    }

    private fun searchPath(key: K): Stack<AVLNode<K, V>>? {
        var currentNode: AVLNode<K, V>? = rootNode
        val path: Stack<AVLNode<K, V>> = Stack()

        while (currentNode != null) {
            path.push(currentNode)

            currentNode = when {
                key < currentNode.key -> currentNode.leftChild
                key > currentNode.key -> currentNode.rightChild
                else -> return null
            }
        }

        return path
    }

    private fun AVLNode<K, V>.rebalance() {
        TODO("Not yet implemented")
    }

    private fun AVLNode<K, V>.balanceFactor(): Int {
        val rightHeight: Int = this.rightChild?.height ?: 0
        val leftHeight: Int = this.leftChild?.height ?: 0
        return rightHeight - leftHeight
    }
}

fun main() {
    val tree: AVLTree<Int, Int> = AVLTree()
    tree.insert(1, 2)
    tree.insert(2, 3)
    tree.insert(0, 2)
}
