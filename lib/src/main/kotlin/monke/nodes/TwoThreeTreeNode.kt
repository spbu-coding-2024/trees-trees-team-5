package monke.nodes

public class TwoThreeTreeNode<K : Comparable<K>, V> (
    key : K,
    value : V
): BinaryTreeNode<K, V, TwoThreeTreeNode<K, V>> (key, value)
