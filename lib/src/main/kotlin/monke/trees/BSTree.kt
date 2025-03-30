package monke.trees

import monke.nodes.BSTNode

/**
 * The Binary Tree Search
 * @author Oderiy Yaroslav
 * @param K Universal comparable type for key storage
 * @param V Universal type for value storage
 */
public class BSTree<K : Comparable<K>, V> : BinaryTree<K, V, BSTNode<K, V>, BSTree<K, V>>() {
    /**
     * Insert `BSTNode<K,V>` in the tree by key and value
     * If key is exist it will throw an error `IllegalArgumentException`
     *
     * @param key The unique key with which will be added value. Comparable type
     * @param value The value which assign to key.
     */
    override fun insert(key: K, value: V) {
        val node = BSTNode(key, value)
        if (rootNode == null) {
            rootNode = node
            return
        }

        var currentNode = rootNode
        var parentNode = rootNode
        while (currentNode != null) {

            parentNode = currentNode

            currentNode = when {
                node.key < currentNode.key -> currentNode.leftChild
                node.key > currentNode.key -> currentNode.rightChild
                else -> throw IllegalArgumentException("Node with key $key already exists")
            }
        }

        if (parentNode != null) {
            when {
                node.key > parentNode.key -> parentNode.rightChild = node
                else -> parentNode.leftChild = node
            }
        }
    }


    private fun getMaxSubTree(node: BSTNode<K, V>?): BSTNode<K, V>? {
        if (node == null) return null
        var currentNode = node
        while (currentNode?.rightChild != null) {
            currentNode = currentNode.rightChild
        }
        return currentNode
    }

    private fun searchParentNode(node: BSTNode<K, V>?): BSTNode<K, V>? {
        if (node == null) return null
        var parentNode = rootNode
        while (parentNode != null) {
            when {
                (node.key < parentNode.key && node != parentNode.leftChild) -> parentNode = parentNode.leftChild
                (node.key > parentNode.key && node != parentNode.rightChild) -> parentNode =
                    parentNode.rightChild

                (node == parentNode.leftChild || node == parentNode.rightChild) -> return parentNode
            }
        }
        return null
    }

    private fun setChild(parentNode: BSTNode<K, V>?, node: BSTNode<K, V>?, newNode: BSTNode<K, V>?): Boolean {
        if (parentNode == null || node == null) {
            return false
        }
        when {
            parentNode.rightChild == node -> parentNode.rightChild = newNode
            parentNode.leftChild == node -> parentNode.leftChild = newNode
            else -> return false
        }
        return true
    }

    /**
     * Delete node from tree by key
     * @param key The unique key which need to delete. If key doesn't exist it will throw an error NoSuchElementException
     * @return value If key was deleted, else 'null'
     */
    override fun delete(key: K): V? {
        val node = searchNode(key) ?: throw NoSuchElementException("Node with key $key does not exist yet")

        if (node.leftChild == null || node.rightChild == null) {
            val newNode = if (node.leftChild == null) node.rightChild else node.leftChild
            if (node != rootNode) {
                val parentNode = searchParentNode(node)

                setChild(parentNode, node, newNode)

            } else {
                rootNode = newNode
            }
            return node.value

        } else {
            val maxLeftSubtreeNode = getMaxSubTree(node.leftChild) ?: return null
            this.delete(maxLeftSubtreeNode.key)
            val newNode: BSTNode<K, V> = BSTNode(maxLeftSubtreeNode.key, maxLeftSubtreeNode.value)

            newNode.rightChild = node.rightChild
            newNode.leftChild = node.leftChild
            if (node == rootNode)
                rootNode = newNode
            else {
                val parentNode = searchParentNode(node)
                setChild(parentNode, node, newNode)
            }
            return node.value
        }
    }

    /**
     * Copy tree with all nodes (deepcopy)
     * @return `BSTree<K,V>`
     */
    fun copy(): BSTree<K, V> {
        val copyTree = BSTree<K, V>()
        copyTree.rootNode = rootNode?.copy()
        return copyTree
    }
}
