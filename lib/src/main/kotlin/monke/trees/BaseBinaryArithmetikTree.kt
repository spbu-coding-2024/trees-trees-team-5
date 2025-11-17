package monke.trees

import monke.nodes.BinaryTreeNode
import monke.trees.treeInterfaces.ArithmetikTree

abstract class BaseBinaryArithmeticTree<
        K : Comparable<K>,
        V,
        N : BinaryTreeNode<K, V, N>,
        T : BaseBinaryArithmeticTree<K, V, N, T>
        > : ArithmetikTree<K, V, T> {

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

    override fun search(key: K): V? {
        var curr = rootNode
        while (curr != null) {
            curr = when {
                key < curr.key -> curr.leftChild
                key > curr.key -> curr.rightChild
                else -> return curr.value
            }
        }
        return null
    }

    protected fun searchNode(key: K): N? {
        var current = rootNode
        while (current != null) {
            current = when {
                key < current.key -> current.leftChild
                key > current.key -> current.rightChild
                else -> return current
            }
        }
        return null
    }

    abstract override fun insert(key: K, value: V)
    abstract override fun delete(key: K): V?

    override fun iterator(): Iterator<Pair<K, V>> =
        InOrderIterator(rootNode)

    class InOrderIterator<K : Comparable<K>, V, N : BinaryTreeNode<K, V, N>>(
        root: N?
    ) : Iterator<Pair<K, V>> {

        private val stack = ArrayDeque<N>()

        init {
            var curr = root
            while (curr != null) {
                stack.addLast(curr)
                curr = curr.leftChild
            }
        }

        override fun hasNext() = stack.isNotEmpty()

        override fun next(): Pair<K, V> {
            val node = stack.removeLast()

            var curr = node.rightChild
            while (curr != null) {
                stack.addLast(curr)
                curr = curr.leftChild
            }

            return node.key to node.value
        }
    }

    fun getRootNodeInfo(): Pair<K, V>? = rootNode?.let { it.key to it.value }

    operator fun get(key: K): V = search(key)
        ?: throw NoSuchElementException("Key $key not found in tree")
}
