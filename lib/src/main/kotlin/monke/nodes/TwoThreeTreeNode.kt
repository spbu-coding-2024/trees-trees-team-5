package monke.nodes

data class Entry<K : Comparable<K>, V>(
    val key: K,
    var value: V
)
public class TwoThreeTreeNode<K : Comparable<K>, V> (
    val entries: MutableList<Entry<K, V>> = mutableListOf(),
    val children: MutableList<TwoThreeTreeNode<K, V>> = mutableListOf(),
    var paretn: TwoThreeTreeNode<K,V>? = null
)
