package com.github.monke.trees

import com.github.monke.nodes.BSTNode


public class BSTree<K : Comparable<K>, V> : BinaryTree<K, V, BSTNode<K, V>, BSTree<K, V>>() {
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
                else -> throw IllegalArgumentException("Node with key ${node.key} is already exist.")
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

    override fun delete(key: K): V? {
        if (rootNode == null) {
            return null
        }

        val node = searchNode(key) ?: throw NoSuchElementException("Node with key $key not found.")

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


    fun copy(): BSTree<K, V> {
        val copyTree = BSTree<K, V>()
        copyTree.rootNode = rootNode?.copy()
        return copyTree
    }
}
