package monke.trees.treeInterfaces

interface BTree<K : Comparable<K>, V> {

    fun search(key: K): V?

    fun insert(key: K, value: V): V?

    fun delete(key: K): V?
}
