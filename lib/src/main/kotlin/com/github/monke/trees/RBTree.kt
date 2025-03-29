package com.github.monke.trees

import com.github.monke.nodes.RBTNode
import com.sun.source.tree.ParenthesizedTree
import java.util.*
import javax.naming.ldap.Rdn

public class RBTree<K : Comparable<K>, V> : BinaryTree<K, V, RBTNode<K, V>, RBTree<K, V>>() {
    override fun insert(key: K, value: V) {

        var newNode = RBTNode(key, value, color = RBTNode.Color.RED)

        if (rootNode == null) {
            newNode = RBTNode(key, value, color = RBTNode.Color.BLACK)
            rootNode = newNode
            return
        }

        var currentNode = rootNode
        var parentNode = rootNode
        var grandfatherNode = rootNode

        while(currentNode != null) {

            grandfatherNode = parentNode
            parentNode = currentNode
            currentNode = when {
                currentNode.key < newNode.key -> currentNode.leftChild
                currentNode.key > newNode.key -> currentNode.rightChild
                else -> throw IllegalArgumentException("Node with key: ${newNode.key} is already exists")
            }
        }

        if(parentNode != null) {
            when {
                newNode.key < parentNode.key -> parentNode.leftChild = newNode
                else -> parentNode.rightChild = newNode
            }
        }

        balancingBeforeInsert(grandfatherNode, parentNode, newNode)
    }

    override fun delete(key: K): V? {
        TODO("Not yet implemented")
    }

    private fun balancingBeforeInsert (
        grandfatherNode : RBTNode<K, V>?,
        parentNode: RBTNode<K, V>?,
        newNode : RBTNode<K, V>) {

        val path = searchPath(newNode.key)
        val uncle : RBTNode<K, V>? = getBrotherNode(parentNode, grandfatherNode)
        return when {

            (path.size >= 4 && uncle?.color == RBTNode.Color.RED) -> {
                parentNode?.color = RBTNode.Color.BLACK
                uncle.color = RBTNode.Color.BLACK
                grandfatherNode?.color = RBTNode.Color.RED
            }

            (path.size == 3 && uncle?.color == RBTNode.Color.RED) -> {
                parentNode?.color = RBTNode.Color.BLACK
                uncle.color = RBTNode.Color.BLACK
            }

            (path.size == 3 && uncle == null) -> {
                leftRotate(parentNode!!, newNode)
                parentNode.color = RBTNode.Color.BLACK
                grandfatherNode?.color = RBTNode.Color.RED
                rightRotate(grandfatherNode!!, parentNode)

            }

            else -> throw IllegalArgumentException()
        }
    }
    private fun searchPath(key: K) : Stack<RBTNode<K, V>>{

        val path : Stack<RBTNode<K, V>> = Stack()
        var currentNode = rootNode

        while(currentNode != null){
            path.push(currentNode)
            currentNode = when{
                key < currentNode.key -> currentNode.leftChild
                key > currentNode.key -> currentNode.rightChild
                else -> null
            }
        }
        if(key != path[path.size-1].key){throw IllegalArgumentException("Node with key: $key is not exist")}

        return path
    }


    private fun rightRotate(node : RBTNode<K, V>, child: RBTNode<K, V>){

        val parentNode = getParentNode(node)

        swapNodes(parentNode, node, child)

        val buffer : RBTNode<K, V>? = node.rightChild

        node.rightChild = child
        node.leftChild = child.leftChild
        child.leftChild = child.rightChild
        child.rightChild = buffer
    }

    private fun leftRotate(node : RBTNode<K, V>, child: RBTNode<K, V>){

        val parentNode = getParentNode(node)

        swapNodes(parentNode, node, child)

        val buffer : RBTNode<K, V>? = node.leftChild

        node.leftChild = child
        node.rightChild = child.rightChild
        child.rightChild = child.leftChild
        child.leftChild = buffer
    }
    private fun getBrotherNode(child : RBTNode<K, V>?, parent : RBTNode<K, V>?) : RBTNode<K, V>?{

        return when{
            parent?.leftChild?.key == child?.key -> parent?.rightChild
            parent?.rightChild?.key == child?.key -> parent?.leftChild
            else -> null
        }

    }

    private fun getParentNode(node : RBTNode<K, V>) : RBTNode<K, V> {
        val path = searchPath(node.key)
        val parentNode : RBTNode<K, V> = RBTNode(path[path.size-2].key, path[path.size-2].value)
        return parentNode
    }

    private fun swapNodes(
        parentNode: RBTNode<K, V>,
        node: RBTNode<K, V>,
        child: RBTNode<K, V>)
    {

        val auxiliaryNode : RBTNode<K,V> = RBTNode(node.key, node.value)

        auxiliaryNode.rightChild = child.rightChild
        auxiliaryNode.leftChild = child.leftChild
        parentNode.rightChild = child
        child.rightChild = auxiliaryNode
    }
}





