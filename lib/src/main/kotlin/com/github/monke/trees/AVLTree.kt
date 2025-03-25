package com.github.monke.trees

import com.github.monke.nodes.AVLNode
import java.util.Stack
import kotlin.math.max

typealias Path<K, V> = Stack<AVLNode<K, V>>

public class AVLTree<K : Comparable<K>, V> : BinaryTree<K, V, AVLNode<K, V>>() {
    override fun insert(key: K, value: V) {
        val path: Path<K, V>? = rootNode?.searchPath(key)

        val insertedNode: AVLNode<K, V> = AVLNode(key, value)
        if (path.isNullOrEmpty()) {
            rootNode = insertedNode
        } else {
            if (path.peek().key == key) {
                throw IllegalArgumentException("Node with such key already exists")
            } else {
                path.roadToRootNode(key, insertedNode)
            }
        }
    }

    override fun delete(key: K): V? {
        tailrec fun minKeyNode(node: AVLNode<K, V>): AVLNode<K, V> {
            return if (node.leftChild == null) node else minKeyNode(node.leftChild as AVLNode<K, V>)
        }

        fun deleteMinKeyNode(node: AVLNode<K, V>): AVLNode<K, V>? {
            if (node.leftChild == null) {
                return node.rightChild
            }
            node.leftChild = deleteMinKeyNode(node.leftChild as AVLNode<K, V>)
            return rebalanced(node)
        }

        val path: Path<K, V>? = rootNode?.searchPath(key)
        if (path.isNullOrEmpty()) {
            return null
        } else {
            val deletedNode: AVLNode<K, V> = path.pop()
            if (deletedNode.key != key) {
                return null
            }

            val lChild: AVLNode<K, V>? = deletedNode.leftChild
            val rChild: AVLNode<K, V>? = deletedNode.rightChild
            var replacingNode: AVLNode<K, V>?

            if (rChild == null) {
                replacingNode = lChild
            } else {
                replacingNode = minKeyNode(rChild)
                replacingNode.rightChild = deleteMinKeyNode(rChild)
                replacingNode.leftChild = lChild
                replacingNode = rebalanced(replacingNode)
            }

            path.roadToRootNode(key, replacingNode)
            return deletedNode.value
        }
    }

    private fun Path<K, V>.roadToRootNode(key: K, newNode: AVLNode<K, V>?) {
        var parentNode: AVLNode<K, V> = this.peek()
        if (key < parentNode.key) {
            parentNode.leftChild = newNode
        } else if (key > parentNode.key) {
            parentNode.rightChild = newNode
        }

        while (this.size > 1) {
            val currentNode: AVLNode<K, V> = this.pop()
            val newCurrentNode: AVLNode<K, V> = rebalanced(currentNode)
            parentNode = this.peek()
            if (currentNode.key == parentNode.leftChild?.key) {
                parentNode.leftChild = newCurrentNode
            } else {
                parentNode.rightChild = newCurrentNode
            }
        }
        val currentNode: AVLNode<K, V> = this.pop()
        rootNode = rebalanced(currentNode)
    }

    private fun AVLNode<K, V>.searchPath(key: K): Path<K, V> {
        val path: Path<K, V> = Path()
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

    private fun balanceFactor(node: AVLNode<K, V>): Int {
        return (node.rightChild?.height ?: 0) - (node.leftChild?.height ?: 0)
    }

    private fun reheight(node: AVLNode<K, V>) {
        node.height = max(node.rightChild?.height ?: 0, node.leftChild?.height ?: 0) + 1
    }

    private fun rebalanced(node: AVLNode<K, V>): AVLNode<K, V> {
        fun rotatedRight(node: AVLNode<K, V>): AVLNode<K, V> {
            val successor: AVLNode<K, V> = node.leftChild as AVLNode<K, V>
            node.leftChild = successor.rightChild
            successor.rightChild = node
            reheight(node)
            reheight(successor)
            return successor
        }

        fun rotatedLeft(node: AVLNode<K, V>): AVLNode<K, V> {
            val successor: AVLNode<K, V> = node.rightChild as AVLNode<K, V>
            node.rightChild = successor.leftChild
            successor.leftChild = node
            reheight(node)
            reheight(successor)
            return successor
        }

        reheight(node)
        when (balanceFactor(node)) {
            2 -> {
                val rChild: AVLNode<K, V>? = node.rightChild
                if (rChild != null && balanceFactor(rChild) < 0) {
                    node.rightChild = rotatedRight(rChild)
                }
                return rotatedLeft(node)
            }

            -2 -> {
                val lChild: AVLNode<K, V>? = node.leftChild
                if (lChild != null && balanceFactor(lChild) > 0) {
                    node.leftChild = rotatedLeft(lChild)
                }
                return rotatedRight(node)
            }
        }
        return node
    }
}
