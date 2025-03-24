package com.github.monke.trees

import com.github.monke.nodes.RBTNode

public class RBTree<K : Comparable<K>, V> : BinaryTree<K, V, RBTNode<K, V>, RBTree<K, V>>() {
    override fun insert(key: K, value: V) {

        val node = RBTNode(key, value)

        if (rootNode == null){
           rootNode = node(RBTNode.Color.BLACK)
        }

        var currentNode = rootNode
        var parentNode = rootNode

        while(currentNode != null) {

            parentNode = currentNode

            currentNode = when {
                currentNode.key < node.key -> currentNode.leftChild
                currentNode.key > node.key -> currentNode.rightChild
                else -> throw IllegalArgumentException("Noda with key: ${node.key} is already exists")
            }
        }

        if(parentNode != null){
            when{
                node.key < parentNode.key -> parentNode.leftChild = node
                else -> parentNode.rightChild = node
            }
        }
        balancinng()
    }

    override fun delete(key: K): V? {
        TODO("Not yet implemented")
    }
}
