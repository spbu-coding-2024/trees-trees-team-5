package monke.trees

import monke.nodes.BinaryTreeNode
import monke.trees.treeInterfaces.ArithmeticTree
import java.util.LinkedList
import java.util.Queue

abstract class BaseBinaryArithmeticTree<
    K : Comparable<K>,
    V,
    N : BinaryTreeNode<K, V, N>,
    T : BaseBinaryArithmeticTree<K, V, N, T>,
    > : ArithmeticTree<K, V, T> {
    protected var rootNode: N? = null

    override operator fun plus(other: T): T {
        insert(other)
        return this as T
    }

    override operator fun minus(other: T): T {
        delete(other)
        return this as T
    }

    fun insert(other: T) {
        for ((k, v) in other) {
            insert(k, v)
        }
    }

    fun delete(other: T) {
        for ((k, _) in other) {
            delete(k)
        }
    }

    override fun search(key: K): V {
        var curr = rootNode
        while (curr != null) {
            curr =
                when {
                    key < curr.key -> curr.leftChild
                    key > curr.key -> curr.rightChild
                    else -> return curr.value
                }
        }
        throw NoSuchElementException("Node with key $key does not exist yet")
    }

    protected fun searchNode(key: K): N? {
        var current = rootNode
        while (current != null) {
            current =
                when {
                    key < current.key -> current.leftChild
                    key > current.key -> current.rightChild
                    else -> return current
                }
        }
        return null
    }

    abstract override fun insert(
        key: K,
        value: V,
    )

    abstract override fun delete(key: K): V?

    override fun iterator(): Iterator<Pair<K, V>> = BinaryTreeIterator()

    private inner class BinaryTreeIterator : Iterator<Pair<K, V>> {
        private val queue: Queue<N> = LinkedList()

        init {
            rootNode?.let { queue.add(it) }
        }

        override fun hasNext(): Boolean = queue.isNotEmpty()

        override fun next(): Pair<K, V> {
            if (!hasNext()) throw NoSuchElementException()
            val currentNode = queue.poll()
            currentNode.leftChild?.let { queue.add(it) }
            currentNode.rightChild?.let { queue.add(it) }
            return currentNode.key to currentNode.value
        }
    }

    fun getRootNodeInfo(): Pair<K, V>? = rootNode?.let { it.key to it.value }

    operator fun get(key: K): V =
        search(key)
            ?: throw NoSuchElementException("Node with key $key does not exist yet")
}
