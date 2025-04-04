package monke.trees

import monke.nodes.BinaryTreeNode
import monke.trees.treeInterfaces.Delete
import monke.trees.treeInterfaces.Insert
import monke.trees.treeInterfaces.NodeArithmetic
import monke.trees.treeInterfaces.Search
import java.util.LinkedList
import java.util.Queue

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
     * Get `Pair` with root node key and value
     * @return `Pair<K, V>?` pair of key and value of existing root node, else `null`
     */
    fun getRootNodeInfo(): Pair<K, V>? {
        val root: N? = rootNode
        root?.let {
            return Pair(root.key, root.value)
        }
        return null
    }

    /**
     * Insert all nodes from another tree to this one
     *
     * @param tree tree, which nodes are inserted
     */
    fun insert(tree: T) {
        for (node in tree) {
            val (key, value) = node
            this.insert(key, value)
        }
    }

    /**
     * Search for the value in tree by key
     * @param key the key of node for search in tree
     * @return value if tree contains node with such key, else throws `NoSuchElementException`
     */
    override fun search(key: K): V {
        val resultNode: N = searchNode(key) ?: throw NoSuchElementException("Node with key $key does not exist yet")
        return resultNode.value
    }

    protected fun searchNode(key: K): N? {
        var currentNode: N? = rootNode
        while (currentNode != null) {
            currentNode =
                when {
                    currentNode.key > key -> currentNode.leftChild
                    currentNode.key < key -> currentNode.rightChild
                    else -> return currentNode
                }
        }
        return null
    }

    /**
     * Return iterator with a pair of key and value of every node. Use level-order traversal
     * @return `Iterator<Pair<K,V>>` for each node in the tree by level-order traversal
     */
    operator fun iterator(): Iterator<Pair<K, V>> = BinaryTreeIterator()

    /**
     * Search for the value by key with get operator
     * @param key the key of node for search in tree
     * @return value if tree contains node with such key, else throws `NoSuchElementException`
     */
    operator fun get(key: K): V = this.search(key)

    /**
     * Insert another tree to this one by plus operation
     * @param other tree which nodes are inserted
     * @return this tree
     */
    override fun plus(other: T): T {
        this.insert(other)
        return this as T
    }

    /**
     * Delete nodes of another trees from this one by minus operation
     * @param other tree, which nodes are deleted
     * @return this tree
     */
    override fun minus(other: T): T {
        this.delete(other)
        return this as T
    }

    private inner class BinaryTreeIterator : Iterator<Pair<K, V>> {
        private val queue: Queue<N> = LinkedList()

        init {
            if (rootNode != null) {
                queue.add(rootNode)
            }
        }

        override fun hasNext(): Boolean = queue.isNotEmpty()

        override fun next(): Pair<K, V> {
            if (!hasNext()) throw NoSuchElementException()
            val currentNode = queue.poll()
            currentNode.leftChild?.let { queue.add(it) }
            currentNode.rightChild?.let { queue.add(it) }
            return Pair(currentNode.key, currentNode.value)
        }
    }
}
