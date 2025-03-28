package com.github.monke.trees

import com.github.monke.nodes.BinaryTreeNode
import com.github.monke.trees.treeInterfaces.Search
import com.github.monke.trees.treeInterfaces.Insert
import com.github.monke.trees.treeInterfaces.Delete
import com.github.monke.trees.treeInterfaces.NodeArithmetic
import java.util.Queue
import java.util.LinkedList

import kotlin.NoSuchElementException

abstract class BinaryTree<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>, T : BinaryTree<K, V, N, T>> :
    Search<K, V, N>,
    Insert<K, V, N>,
    Delete<K, V, N, T>,
    NodeArithmetic<K, V, N, T> {
    protected var rootNode: N? = null

    fun insert(tree: T) {
        for (i in tree) {
            val (key, value) = i
            this.insert(key, value)
        }
    }


    override fun search(key: K): V? {
        return searchNode(key)?.value
    }

    protected fun searchNode(key: K): N? {
        var currentNode: N? = rootNode
        while (currentNode != null) {
            currentNode = when {
                currentNode.key == key -> return currentNode
                currentNode.key < key -> currentNode.rightChild
                else -> currentNode.leftChild
            }
        }
        return null
    }

    operator fun iterator(): Iterator<Pair<K, V>> {
        return BinaryTreeIterator()
    }

    operator fun get(key: K): V? {
        return this.search(key)
    }

    override fun plus(tree: T): T {
        this.insert(tree)
        return this as T
    }

    override fun minus(tree: T): T {
        this.delete(tree)
        return this as T
    }


    private inner class BinaryTreeIterator : Iterator<Pair<K, V>> {
        private val queue: Queue<N> = LinkedList()

        init {
            if (rootNode != null) {
                queue.add(rootNode)
            }
        }

        override fun hasNext(): Boolean {
            return queue.isNotEmpty()
        }

        override fun next(): Pair<K, V> {
            if (!hasNext()) throw NoSuchElementException()
            val currentNode = queue.poll()
            currentNode.leftChild?.let { queue.add(it) }
            currentNode.rightChild?.let { queue.add(it) }
            return Pair(currentNode.key, currentNode.value)
        }
    }
}
