package monke.trees

import monke.trees.treeInterfaces.ArithmetikTree

abstract class BaseMultiwayArithmeticTree<
        K : Comparable<K>,
        V,
        N,
        T : BaseMultiwayArithmeticTree<K, V, N, T>
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

    abstract override fun insert(key: K, value: V)
    abstract override fun delete(key: K): V?

    // итератор для multi-way дерева должен быть реализован в наследнике
    abstract override fun iterator(): Iterator<Pair<K,V>>
}
