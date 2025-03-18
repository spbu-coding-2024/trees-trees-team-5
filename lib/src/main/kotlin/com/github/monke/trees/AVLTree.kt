package com.github.monke.trees

import com.github.monke.nodes.AVLNode
import java.util.*
import kotlin.math.max

public class AVLTree<K : Comparable<K>, V> : BinaryTree<K, V, AVLNode<K, V>>() {
    override fun insert(key: K, value: V) {
        val path: Stack<AVLNode<K, V>>? = rootNode?.searchPath(key)

        val newNode: AVLNode<K, V> = AVLNode(key, value)
        if (path.isNullOrEmpty()) {
            rootNode = newNode
        } else {
            var parentNode: AVLNode<K, V> = path.peek()
            if (parentNode.key == key) {
                throw IllegalArgumentException("Node with such key already exists")
            } else {
                if (key < parentNode.key) {
                    parentNode.leftChild = newNode
                } else if (key > parentNode.key) {
                    parentNode.rightChild = newNode
                }

                while (path.size > 1) {
                    val currentNode: AVLNode<K, V> = path.pop()
                    val newCurrentNode: AVLNode<K, V>? = currentNode.rebalanced()
                    parentNode = path.peek()
                    if (currentNode.key == parentNode.leftChild?.key) {
                        parentNode.leftChild = newCurrentNode
                    } else {
                        parentNode.rightChild = newCurrentNode
                    }
                }
                val currentNode: AVLNode<K, V> = path.pop()
                rootNode = currentNode.rebalanced()
            }
        }
    }

    override fun delete(key: K): V? {
        val path: Stack<AVLNode<K, V>>? = rootNode?.searchPath(key)
        if (path.isNullOrEmpty() || path.peek().key != key) {
            throw IllegalArgumentException("Node with such key does not exist")
        } else {
            // deletion

            while (path.size > 1) {
                val currentNode: AVLNode<K, V> = path.pop()
                val parentNode: AVLNode<K, V> = path.peek()
                val newCurrentNode: AVLNode<K, V>? = currentNode.rebalanced()
                if (currentNode.key == parentNode.leftChild?.key) {
                    parentNode.leftChild = newCurrentNode
                } else {
                    parentNode.rightChild = newCurrentNode
                }
            }
            val currentNode: AVLNode<K, V> = path.pop()
            rootNode = currentNode.rebalanced()
        }
    }

    private fun AVLNode<K, V>.searchPath(key: K): Stack<AVLNode<K, V>> {
        val path: Stack<AVLNode<K, V>> = Stack()
        var currentNode: AVLNode<K, V>? = this

        while (currentNode != null) {
            path.push(currentNode)
            currentNode = when {
                key < currentNode.key -> currentNode.leftChild
                key > currentNode.key -> currentNode.rightChild
                else -> null
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
