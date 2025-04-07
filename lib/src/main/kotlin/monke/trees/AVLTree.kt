package monke.trees

import monke.nodes.AVLNode
import java.util.Stack
import kotlin.math.max

typealias Path<K, V> = Stack<AVLNode<K, V>>

/**
 * AVL tree implementation
 * @author Kharisov Bogdan
 * @param K generic comparable type for key storage
 * @param V generic type for value storage
 */
public class AVLTree<K : Comparable<K>, V> : BinaryTree<K, V, AVLNode<K, V>, AVLTree<K, V>>() {
    /**
     * Get tree height
     * @return `Int` height of the tree
     */

    fun getHeight(): Int = rootNode.getHeight()

    /**
     * Insert `AVLNode<K,V>` with key and value to the tree, then the tree is being balanced. If key already exists,
     * `IllegalArgumentException` is thrown
     * @param key key of inserted node
     * @param value value, assigned to inserted node
     */
    override fun insert(
        key: K,
        value: V,
    ) {
        val insertedNode: AVLNode<K, V> = AVLNode(key, value)
        val path: Path<K, V>? = rootNode?.searchPath(key)

        if (path == null) {
            rootNode = insertedNode
        } else {
            if (path.peek().key == key) {
                throw IllegalArgumentException("Node with key $key already exists")
            }
            path.roadToRootNode(key, insertedNode)
        }
    }

    /**
     * Delete node from the tree with its key. If there is no node with such key, `NoSuchElementException` is thrown
     * @param key key of deleted node
     * @return value, assigned to deleted node
     */
    override fun delete(key: K): V {
        tailrec fun minKeyNode(node: AVLNode<K, V>): AVLNode<K, V> =
            if (node.leftChild == null) node else minKeyNode(node.leftChild as AVLNode<K, V>)

        fun deleteMinKeyNode(node: AVLNode<K, V>): AVLNode<K, V>? {
            node.leftChild?.let {
                node.leftChild = deleteMinKeyNode(node.leftChild as AVLNode<K, V>)
                return rebalanced(node)
            }
            return node.rightChild
        }

        // delete logic
        val path: Path<K, V> =
            rootNode?.searchPath(key)
                ?: throw NoSuchElementException("Node with key $key does not exist yet")
        val deletedNode: AVLNode<K, V> = path.pop()
        if (deletedNode.key != key) {
            throw NoSuchElementException("Node with key $key does not exist yet")
        }

        val lChild: AVLNode<K, V>? = deletedNode.leftChild
        val rChild: AVLNode<K, V>? = deletedNode.rightChild
        var replacingNode: AVLNode<K, V>?

        if (rChild == null) {
            replacingNode = lChild
        } else {
            replacingNode = minKeyNode(rChild)
            replacingNode.rightChild = deleteMinKeyNode(rChild)
            replacingNode.leftChild = lChild
            replacingNode = rebalanced(replacingNode)
        }

        if (path.isEmpty()) {
            rootNode = replacingNode
        } else {
            path.roadToRootNode(key, replacingNode)
        }

        return deletedNode.value
    }

    private fun AVLNode<K, V>.searchPath(key: K): Path<K, V> {
        val path: Path<K, V> = Path()
        var currentNode: AVLNode<K, V>? = this

        while (currentNode != null) {
            path.push(currentNode)
            currentNode =
                when {
                    key < currentNode.key -> currentNode.leftChild
                    key > currentNode.key -> currentNode.rightChild
                    else -> null
                }
        }

        return path
    }

    private fun Path<K, V>.roadToRootNode(
        key: K,
        newNode: AVLNode<K, V>?,
    ) {
        var parentNode: AVLNode<K, V> = this.peek()
        if (key < parentNode.key) {
            parentNode.leftChild = newNode
        } else if (key > parentNode.key) {
            parentNode.rightChild = newNode
        }

        while (this.size > 1) {
            val currentNode: AVLNode<K, V> = this.pop()
            val newCurrentNode: AVLNode<K, V> = rebalanced(currentNode)
            parentNode = this.peek()
            if (currentNode.key == parentNode.leftChild?.key) {
                parentNode.leftChild = newCurrentNode
            } else {
                parentNode.rightChild = newCurrentNode
            }
        }
        val currentNode: AVLNode<K, V> = this.pop()
        rootNode = rebalanced(currentNode)
    }

    private fun AVLNode<K, V>?.getHeight(): Int = this?.height ?: 0

    private fun AVLNode<K, V>.getBalanceFactor(): Int = this.rightChild.getHeight() - this.leftChild.getHeight()

    private fun reheight(node: AVLNode<K, V>) {
        node.height = max(node.rightChild.getHeight(), node.leftChild.getHeight()) + 1
    }

    private fun rebalanced(node: AVLNode<K, V>): AVLNode<K, V> {
        fun rotated(
            node: AVLNode<K, V>,
            left: Boolean,
        ): AVLNode<K, V> {
            val successor = if (left) node.rightChild as AVLNode<K, V> else node.leftChild as AVLNode<K, V>

            if (left) {
                node.rightChild = successor.leftChild
                successor.leftChild = node
            } else {
                node.leftChild = successor.rightChild
                successor.rightChild = node
            }
            reheight(node)
            reheight(successor)
            return successor
        }

        fun rotatedRight(node: AVLNode<K, V>): AVLNode<K, V> = rotated(node, false)

        fun rotatedLeft(node: AVLNode<K, V>): AVLNode<K, V> = rotated(node, true)

        // balance logic
        val rightHeavy = 2
        val leftHeavy = -2
        reheight(node)
        when (node.getBalanceFactor()) {
            rightHeavy -> {
                val rChild: AVLNode<K, V>? = node.rightChild
                rChild?.let {
                    if (rChild.getBalanceFactor() < 0) {
                        node.rightChild = rotatedRight(rChild)
                    }
                }
                return rotatedLeft(node)
            }

            leftHeavy -> {
                val lChild: AVLNode<K, V>? = node.leftChild
                lChild?.let {
                    if (lChild.getBalanceFactor() > 0) {
                        node.leftChild = rotatedLeft(lChild)
                    }
                }
                return rotatedRight(node)
            }
        }
        return node
    }
}
