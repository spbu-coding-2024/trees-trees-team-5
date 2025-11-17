package monke.trees.treeInterfaces

interface Tree<K : Comparable<K>, V> : Iterable<Pair<K, V>> {
    fun insert(
        key: K,
        value: V,
    )

    fun delete(key: K): V?

    fun search(key: K): V?
}
