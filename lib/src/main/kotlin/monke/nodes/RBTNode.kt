package monke.nodes

public class RBTNode<K : Comparable<K>, V>(
    key: K,
    value: V,
    var parentNode: RBTNode<K, V>? = null,
    var color: Color = Color.RED,
) : BinaryTreeNode<K, V, RBTNode<K, V>>(key, value) {
    enum class Color {
        RED,
        BLACK,
    }
}
