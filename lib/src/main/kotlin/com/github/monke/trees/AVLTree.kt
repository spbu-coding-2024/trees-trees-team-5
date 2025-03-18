package com.github.monke.trees

import com.github.monke.nodes.AVLNode
import java.util.Stack
import kotlin.math.max

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
                var parentNode: AVLNode<K, V> = path.peek()
                if (key < parentNode.key) {
                    parentNode.leftChild = newNode
                } else if (key > parentNode.key) {
                    parentNode.rightChild = newNode
                }

                while (path.size > 1) {
                    val currentNode: AVLNode<K, V> = path.pop()
                    parentNode = path.peek()
                    if (currentNode.key == parentNode.leftChild?.key) {
                        parentNode.leftChild = currentNode.rebalanced()
                    } else {
                        parentNode.rightChild = currentNode.rebalanced()
                    }
                }
                val currentNode: AVLNode<K, V> = path.pop()
                rootNode = currentNode.rebalanced()
            }
        }
    }

    override fun delete(key: K): V? {
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

    private fun AVLNode<K, V>.rebalanced(): AVLNode<K, V>? {
        fun AVLNode<K, V>.rotatedRight(): AVLNode<K, V>? {
            val lChild: AVLNode<K, V>? = this.leftChild
            this.leftChild = lChild?.rightChild
            lChild?.rightChild = this
            this.reheight()
            lChild?.reheight()
            return lChild
        }

        fun AVLNode<K, V>.rotatedLeft(): AVLNode<K, V>? {
            val rChild: AVLNode<K, V>? = this.rightChild
            this.rightChild = rChild?.leftChild
            rChild?.leftChild = this
            this.reheight()
            rChild?.reheight()
            return rChild
        }

        this.reheight()
        when (this.balanceFactor) {
            2 -> {
                val rChild: AVLNode<K, V>? = this.rightChild
                if (rChild != null && rChild.balanceFactor < 0) {
                    this.rightChild = rChild.rotatedRight()
                }
                return this.rotatedLeft()
            }

            -2 -> {
                val lChild: AVLNode<K, V>? = this.leftChild
                if (lChild != null && lChild.balanceFactor > 0) {
                    this.leftChild = lChild.rotatedLeft()
                }
                return this.rotatedRight()
            }
        }
        return this
    }

    private val AVLNode<K, V>?.balanceFactor: Int
        get() = (this?.rightChild?.height ?: 0) - (this?.leftChild?.height ?: 0)

    private fun AVLNode<K, V>.reheight() {
        this.height = max(this.rightChild?.height ?: 0, this.leftChild?.height ?: 0) + 1
    }
}
