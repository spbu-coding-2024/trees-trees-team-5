package monke.nodes

import java.util.Stack

/**
 * Binary Tree Search Node is used for BST
 * It contains all necessary data to work with BST
 *
 * @param K Universal comparable type for key storage
 * @param V Universal type for value storage
 * @param key The key which value will add to the tree
 * @param value The value which assign to key
 */
public class BSTNode<K : Comparable<K>, V>(
    key: K,
    value: V,
) : BinaryTreeNode<K, V, BSTNode<K, V>>(key, value) {

    @Suppress("UNCHECKED_CAST")
    private fun <V> deepCopyValue(value: V): V {
        return when (value) {
            null -> null as V

            is String, is Int, is Float, is Double,
            is Boolean, is Long, is Short, is Byte,
            is Char -> value
            is List<*> -> value.toList() as V
            is Set<*> -> value.toSet() as V
            is Map<*, *> -> value.toMap() as V

            is Cloneable -> try {
                val method = value.javaClass.getMethod("clone")
                method.isAccessible = true
                method.invoke(value) as V
            } catch (e: Exception) {
                value
            }

            else -> value
        }
    }
    /**
     * Copy method to node, which copy node and all it children.
     * @return `BSTNode<K,V>`
     */
    fun copy(): BSTNode<K, V> {
        val stack = Stack<Pair<BSTNode<K, V>, BSTNode<K, V>>>()
        val copyNode = BSTNode(key, deepCopyValue(value))

        this.leftChild?.let { leftChild ->
            val copiedLeft = BSTNode(leftChild.key, deepCopyValue(leftChild.value))
            copyNode.leftChild = copiedLeft
            stack.push(leftChild to copiedLeft)
        }

        this.rightChild?.let { rightChild ->
            val copiedRight = BSTNode(rightChild.key, deepCopyValue(rightChild.value))
            copyNode.rightChild = copiedRight
            stack.push(rightChild to copiedRight)
        }

        while (stack.isNotEmpty()) {
            val (originalNode, copiedNode) = stack.pop()

            originalNode.leftChild?.let { leftChild ->
                val copiedLeft = BSTNode(leftChild.key, deepCopyValue(leftChild.value))
                copiedNode.leftChild = copiedLeft
                stack.push(leftChild to copiedLeft)
            }

            originalNode.rightChild?.let { rightChild ->
                val copiedRight = BSTNode(rightChild.key, deepCopyValue(rightChild.value))
                copiedNode.rightChild = copiedRight
                stack.push(rightChild to copiedRight)
            }
        }

        return copyNode
    }
}
