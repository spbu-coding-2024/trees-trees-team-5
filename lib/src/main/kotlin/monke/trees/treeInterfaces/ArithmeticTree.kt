package monke.trees.treeInterfaces

interface ArithmeticTree<K : Comparable<K>, V, T> : Tree<K, V> {
    operator fun plus(other: T): T

    operator fun minus(other: T): T
}
