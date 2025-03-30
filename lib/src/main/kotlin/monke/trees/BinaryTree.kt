package monke.trees

import monke.nodes.BinaryTreeNode
import monke.trees.treeInterfaces.Search
import monke.trees.treeInterfaces.Insert
import monke.trees.treeInterfaces.Delete
import monke.trees.treeInterfaces.NodeArithmetic
import java.util.Queue
import java.util.LinkedList

import kotlin.NoSuchElementException

/**
 * Abstract base class for all binary tree structs
 * @param K Universal comparable type for key storage
 * @param V Universal type for value storage
 * @param N Universal type for storing `Node<K, V, N>`
 * @param T Universal type for storing `BinaryTree<K,V,N,T>`
 */
abstract class BinaryTree<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>, T : BinaryTree<K, V, N, T>> :
    Search<K, V, N>,
    Insert<K, V, N>,
    Delete<K, V, N, T>,
    NodeArithmetic<K, V, N, T> {
    protected var rootNode: N? = null

    /**
     * Insert all nodes from tree to self
     *
     * @param tree The tree which nodes insert
     */
    fun insert(tree: T) {
        for (i in tree) {
            val (key, value) = i
            this.insert(key, value)
        }
    }

    /**
     * Search value in tree by key
     * @param key The key of node for search in tree
     * @return value if key contains in tree, else `null`
     */
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

    /**
     * Return iterator with pair key value of every node. Using BFS method
     * @return `Iterator<Pair<K,V>>` of all nodes in the tree by bfs method.
     */
    operator fun iterator(): Iterator<Pair<K, V>> {
        return BinaryTreeIterator()
    }

    /**
     * Search value by key with get operator
     * @param key The key of node for search in tree
     * @return value if key contains in tree, else `null`
     */
    operator fun get(key: K): V? {
        return this.search(key)
    }

    /**
     * Insert tree with plus operator
     * @param tree The tree which nodes insert
     * @return this
     */
    override fun plus(tree: T): T {
        this.insert(tree)
        return this as T
    }

    /**
     * Delete nodes by another tree with minus operator
     * @param tree The tree which nodes delete
     * @return this
     */
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
