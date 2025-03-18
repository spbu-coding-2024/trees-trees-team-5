package com.github.monke.trees

import com.github.monke.nodes.BinaryTreeNode
import com.github.monke.trees.treeInterfaces.Search
import com.github.monke.trees.treeInterfaces.Insert
import com.github.monke.trees.treeInterfaces.Delete
import com.github.monke.trees.treeInterfaces.NodeArithmetic
import java.util.Queue
import java.util.LinkedList

import kotlin.NoSuchElementException

abstract class BinaryTree<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>> :
    Search<K, V, N>,
    Insert<K, V, N>,
    Delete<K, V, N>,
    NodeArithmetic<K, V, N> {

    protected var rootNode: N? = null
    override fun search(key: K): N? {
        var currentNode = rootNode
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
        return this.search(key)?.value
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
