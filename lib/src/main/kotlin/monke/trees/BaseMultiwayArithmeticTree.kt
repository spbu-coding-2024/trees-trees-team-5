package monke.trees

import monke.nodes.TwoThreeTreeNode
import monke.trees.treeInterfaces.ArithmeticTree

abstract class BaseMultiwayArithmeticTree<
    K : Comparable<K>,
    V,
    N : TwoThreeTreeNode<K, V>,
    T : BaseMultiwayArithmeticTree<K, V, N, T>,
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

    abstract override fun insert(
        key: K,
        value: V,
    )

    abstract override fun delete(key: K): V?

    abstract override fun iterator(): Iterator<Pair<K, V>>

    abstract fun createInstance(): T

    operator fun get(key: K): V =
        search(key)
            ?: throw NoSuchElementException("Node with key $key does not exist yet")

    fun getRootNodeInfo(): Pair<K, V>? = rootNode?.entries?.firstOrNull()?.let { it.key to it.value }

    fun copy(): T {
        val newTree = createInstance()

        fun deepInsert(node: TwoThreeTreeNode<K, V>?) {
            if (node == null) return
            for (entry in node.entries) {
                newTree.insert(entry.key, entry.value)
            }
            for (child in node.children) {
                deepInsert(child)
            }
        }

        deepInsert(rootNode)
        return newTree
    }
}
