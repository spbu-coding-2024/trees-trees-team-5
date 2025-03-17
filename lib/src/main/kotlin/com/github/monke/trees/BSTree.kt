package com.github.monke.trees

import com.github.monke.nodes.BSTNode



public class BSTree<K : Comparable<K>, V> : BinaryTree<K, V, BSTNode<K, V>>() {

    override fun insert(key: K, value: V) {
        val node = BSTNode(key, value)
        if (rootNode == null) {
            rootNode = node
            return
        }

        var current_node = rootNode
        var parent_node = rootNode
        while (current_node != null) {
            parent_node = current_node
            if (node.key < current_node.key) {
                current_node = current_node.leftChild
            } else if (node.key > current_node.key) {
                current_node = current_node.rightChild
            }
        }
        if (parent_node != null) {
            when {
                node.key > parent_node.key -> parent_node.rightChild = node
                else -> parent_node.leftChild = node
            }
        }


    }

    private fun getMaxSubTree(node: BSTNode<K, V>?): BSTNode<K, V>? {
        if (node == null) return null
        var current_node = node
        while (current_node?.rightChild != null) {
            current_node = current_node.rightChild
        }
        return current_node
    }

    private fun searchParentNode(node: BSTNode<K, V>?): BSTNode<K, V>? {
        if (node == null) return null
        var parrent_node = rootNode
        while (parrent_node != null) {
            when {
                (node.key < parrent_node.key && node != parrent_node.leftChild) -> parrent_node = parrent_node.leftChild
                (node.key > parrent_node.key && node != parrent_node.rightChild) -> parrent_node =
                    parrent_node.rightChild

                (node == parrent_node.leftChild || node == parrent_node.rightChild) -> return parrent_node
            }
        }
        return null
    }

    private fun setChild(parrent_node: BSTNode<K, V>?, node: BSTNode<K, V>?, new_node: BSTNode<K, V>?): Boolean {
        if (parrent_node == null || node == null) {
            return false
        }
        when {
            parrent_node.rightChild == node -> parrent_node.rightChild = new_node
            parrent_node.leftChild == node -> parrent_node.leftChild = new_node
            else -> return false
        }
        return true
    }

    override fun delete(key: K): V? {
        if (rootNode == null) {
            return null
        }

        var node = search(key)

        if (node?.leftChild == null || node?.rightChild == null) {
            val new_node = if (node?.leftChild == null) node?.rightChild else node?.leftChild
            if (node != rootNode) {
                val parrent_node = searchParentNode(node)

                setChild(parrent_node, node, new_node)

            } else {
                rootNode = new_node
            }
            return node?.value

        } else {
            val max_left_subtree_node = getMaxSubTree(node.leftChild) ?: return null
            this.delete(max_left_subtree_node.key)
            val new_node: BSTNode<K, V>? = BSTNode(max_left_subtree_node.key, max_left_subtree_node.value)

            if (new_node != null) {
                new_node.rightChild = node.rightChild
                new_node.leftChild = node.leftChild
                if (node == rootNode)
                    rootNode = new_node
                else {
                    val parrent_node = searchParentNode(node)
                    setChild(parrent_node, node, new_node)
                }
                return node.value
            }

        }

        return null
    }

    override operator fun iterator(): BSTNode<K, V> {
        TODO("Not implemented yet")
    }
}



