package monke.trees

import monke.nodes.RBTNode
import java.util.Stack

/**
 * RB tree implementation
 * @author Tarasov Andrey
 * @param K generic comparable type for key storage
 * @param V generic type for value storage
 */

public class RBTree<K : Comparable<K>, V>
    : BaseBinaryArithmeticTree<K, V, RBTNode<K, V>, RBTree<K, V>>() {
    /**
     * Insert `RBTNode<K,V>` in the tree by key and value
     * If key is exist it will throw an error `IllegalArgumentException`
     *
     * @param key The unique key with which will be added value. Comparable type
     * @param value The value which assign to key.
     */
    override fun insert(
        key: K,
        value: V,
    ) {
        if (rootNode == null) {
            rootNode = RBTNode(key, value, color = RBTNode.Color.BLACK)
            return
        }

        val newNode = RBTNode(key, value, color = RBTNode.Color.RED)
        var currentNode = rootNode
        var parentNode = rootNode
        var grandfatherNode = rootNode

        while (currentNode != null) {
            grandfatherNode = parentNode
            parentNode = currentNode
            currentNode =
                when {
                    currentNode.key < newNode.key -> currentNode.leftChild
                    currentNode.key > newNode.key -> currentNode.rightChild
                    else -> throw IllegalArgumentException("Node with key: $key already exists")
                }
        }

        when {
            newNode.key < parentNode!!.key -> parentNode.leftChild = newNode
            else -> parentNode.rightChild = newNode
        }
        balancingBeforeInsert(grandfatherNode, parentNode, newNode)
    }

    /**
     * Delete node from tree by key
     * @param key The unique key which need to delete. If key doesn't exist it will throw an error NoSuchElementException
     * @return value If key was deleted
     */
    override fun delete(key: K): V? {
        val nodeToDelete = searchNode(key) ?: throw NoSuchElementException("Node with key $key does not exist yet")
        val nodesChild = getChild(nodeToDelete)
        val nodesValue = nodeToDelete.value

        when {
            (nodeToDelete.color == RBTNode.Color.RED) -> {
                when {
                    (nodesChild.size == 0) -> {
                        val parent = getParentNode(nodeToDelete)
                        if (parent?.rightChild == nodeToDelete) {
                            parent.rightChild = null
                        } else {
                            parent?.leftChild = null
                        }
                        return nodesValue
                    }

                    // (nodesChild.size == 1) невозможен

                    (nodesChild.size == 2) -> {
                        val maxInLeftSubTree: RBTNode<K, V> = getMaxInLeftSubTree(nodeToDelete.leftChild!!)
                        swapNodesForDelete(nodeToDelete, maxInLeftSubTree)
                        delete(nodeToDelete.key)
                    }
                }
            }

            (nodeToDelete.color == RBTNode.Color.BLACK) -> {
                when {
                    (nodesChild.size == 0) -> {
                        val parentNode = getParentNode(nodeToDelete)
                        val uncleNode = getBrotherNode(nodeToDelete, parentNode)

                        if (parentNode?.rightChild == nodeToDelete) {
                            parentNode.rightChild = null
                        } else {
                            parentNode?.leftChild = null
                        }
                        when {
                            (
                                parentNode?.color == RBTNode.Color.RED &&
                                    uncleNode?.color == RBTNode.Color.BLACK &&
                                    uncleNode.leftChild?.color == RBTNode.Color.BLACK &&
                                    uncleNode.rightChild?.color == RBTNode.Color.BLACK
                                ) -> {
                                parentNode.color = RBTNode.Color.BLACK
                                uncleNode.color = RBTNode.Color.RED
                            }

                            (
                                parentNode?.color == RBTNode.Color.RED &&
                                    uncleNode?.color == RBTNode.Color.BLACK &&
                                    uncleNode.leftChild?.color == RBTNode.Color.RED
                                ) -> {
                                uncleNode.leftChild?.color = RBTNode.Color.BLACK
                                uncleNode.color = RBTNode.Color.RED
                                parentNode.color = RBTNode.Color.BLACK
                                if (parentNode.leftChild == uncleNode) {
                                    rightRotate(parentNode, uncleNode)
                                } else {
                                    leftRotate(parentNode, uncleNode)
                                }
                                return nodesValue
                            }

                            (
                                parentNode?.color == RBTNode.Color.BLACK &&
                                    uncleNode?.color == RBTNode.Color.RED &&
                                    uncleNode.rightChild?.leftChild?.color == RBTNode.Color.BLACK &&
                                    uncleNode.rightChild?.rightChild?.color == RBTNode.Color.BLACK
                                ) -> {
                                uncleNode.rightChild?.color = RBTNode.Color.RED
                                uncleNode.color = RBTNode.Color.BLACK
                                if (parentNode.leftChild == uncleNode) {
                                    rightRotate(parentNode, uncleNode)
                                } else {
                                    leftRotate(parentNode, uncleNode)
                                }
                                return nodesValue
                            }

                            (
                                parentNode?.color == RBTNode.Color.BLACK &&
                                    uncleNode?.color == RBTNode.Color.RED &&
                                    uncleNode.rightChild?.leftChild?.color == RBTNode.Color.RED
                                ) -> {
                                uncleNode.rightChild?.leftChild?.color = RBTNode.Color.BLACK
                                if (parentNode.leftChild == uncleNode) {
                                    leftRotate(uncleNode, uncleNode.rightChild!!)
                                    rightRotate(parentNode, uncleNode)
                                } else {
                                    rightRotate(uncleNode, uncleNode.leftChild!!)
                                    leftRotate(parentNode, uncleNode)
                                }

                                return nodesValue
                            }

                            (
                                parentNode?.color == RBTNode.Color.BLACK &&
                                    uncleNode?.color == RBTNode.Color.BLACK &&
                                    uncleNode.rightChild?.color == RBTNode.Color.BLACK &&
                                    uncleNode.leftChild?.color == RBTNode.Color.BLACK
                                ) -> {
                                uncleNode.color = RBTNode.Color.RED
                                balancingBeforeDelete(parentNode)
                                return nodesValue
                            }
                        }
                    }

                    (nodesChild.size == 1) -> {
                        val child: RBTNode<K, V> = getChild(nodeToDelete)[0]
                        val parent = swapNodesForDelete(nodeToDelete, child)
                        if (parent?.leftChild != null) {
                            parent.leftChild = null
                        } else {
                            parent?.rightChild = null
                        }
                        return nodesValue
                    }

                    (nodesChild.size == 2) -> {
                        val maxInLeftSubTree: RBTNode<K, V> = getMaxInLeftSubTree(nodeToDelete.leftChild!!)
                        swapNodesForDelete(nodeToDelete, maxInLeftSubTree)
                        delete(nodeToDelete.key)
                    }
                }
            }
        }
        return nodesValue
    }

    private fun balancingBeforeInsert(
        grandfatherNode: RBTNode<K, V>?,
        parentNode: RBTNode<K, V>?,
        newNode: RBTNode<K, V>,
    ) {
        val path = searchPath(newNode.key)
        val uncle: RBTNode<K, V>? = getBrotherNode(parentNode, grandfatherNode)
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
            else -> {}
        }
    }

    private fun balancingBeforeDelete(nodeForBalancing: RBTNode<K, V>) {
        val parentNode = getParentNode(nodeForBalancing)
        val uncleNode = getBrotherNode(nodeForBalancing, parentNode)

        when {
            (
                parentNode?.color == RBTNode.Color.RED &&
                    uncleNode?.color == RBTNode.Color.BLACK &&
                    uncleNode.leftChild?.color == RBTNode.Color.BLACK &&
                    uncleNode.rightChild?.color == RBTNode.Color.BLACK
                ) -> {
                parentNode.color = RBTNode.Color.BLACK
                uncleNode.color = RBTNode.Color.RED
            }

            (
                parentNode?.color == RBTNode.Color.RED &&
                    uncleNode?.color == RBTNode.Color.BLACK &&
                    uncleNode.leftChild?.color == RBTNode.Color.RED
                ) -> {
                uncleNode.leftChild?.color = RBTNode.Color.BLACK
                uncleNode.color = RBTNode.Color.RED
                parentNode.color = RBTNode.Color.BLACK
                if (parentNode.leftChild == uncleNode) {
                    rightRotate(parentNode, uncleNode)
                } else {
                    leftRotate(parentNode, uncleNode)
                }
            }

            (
                parentNode?.color == RBTNode.Color.BLACK &&
                    uncleNode?.color == RBTNode.Color.RED &&
                    uncleNode.rightChild?.leftChild?.color == RBTNode.Color.BLACK &&
                    uncleNode.rightChild?.rightChild?.color == RBTNode.Color.BLACK
                ) -> {
                uncleNode.rightChild?.color = RBTNode.Color.RED
                uncleNode.color = RBTNode.Color.BLACK
                if (parentNode.leftChild == uncleNode) {
                    rightRotate(parentNode, uncleNode)
                } else {
                    leftRotate(parentNode, uncleNode)
                }
            }

            (
                parentNode?.color == RBTNode.Color.BLACK &&
                    uncleNode?.color == RBTNode.Color.RED &&
                    uncleNode.rightChild?.leftChild?.color == RBTNode.Color.RED
                ) -> {
                uncleNode.rightChild?.leftChild?.color = RBTNode.Color.BLACK
                if (parentNode.leftChild == uncleNode) {
                    leftRotate(uncleNode, uncleNode.rightChild!!)
                    rightRotate(parentNode, uncleNode)
                } else {
                    rightRotate(uncleNode, uncleNode.leftChild!!)
                    leftRotate(parentNode, uncleNode)
                }
            }

            (
                parentNode?.color == RBTNode.Color.BLACK &&
                    uncleNode?.color == RBTNode.Color.BLACK &&
                    uncleNode.rightChild?.color == RBTNode.Color.BLACK &&
                    uncleNode.leftChild?.color == RBTNode.Color.BLACK
                ) -> {
                uncleNode.color = RBTNode.Color.RED
                balancingBeforeDelete(parentNode)
            }
        }
    }

    private fun searchPath(key: K): Stack<RBTNode<K, V>> {
        val path: Stack<RBTNode<K, V>> = Stack()
        var currentNode = rootNode

        while (currentNode != null) {
            path.push(currentNode)
            currentNode =
                when {
                    key < currentNode.key -> currentNode.leftChild
                    key > currentNode.key -> currentNode.rightChild
                    else -> null
                }
        }
        if (key != path[path.size - 1].key) {
            throw IllegalArgumentException("Node with key: $key is not exist")
        }

        return path
    }

    private fun rightRotate(
        node: RBTNode<K, V>,
        child: RBTNode<K, V>,
    ) {
        val parentNode = getParentNode(node)

        swapParentAndChild(parentNode, node, child)

        val buffer: RBTNode<K, V>? = node.rightChild

        node.rightChild = child
        node.leftChild = child.leftChild
        child.leftChild = child.rightChild
        child.rightChild = buffer
    }

    private fun leftRotate(
        node: RBTNode<K, V>,
        child: RBTNode<K, V>,
    ) {
        val parentNode = getParentNode(node)

        swapParentAndChild(parentNode, node, child)

        val buffer: RBTNode<K, V>? = node.leftChild

        node.leftChild = child
        node.rightChild = child.rightChild
        child.rightChild = child.leftChild
        child.leftChild = buffer
    }

    private fun getBrotherNode(
        child: RBTNode<K, V>?,
        parent: RBTNode<K, V>?,
    ): RBTNode<K, V>? =
        when {
            parent?.leftChild?.key == child?.key -> parent?.rightChild
            parent?.rightChild?.key == child?.key -> parent?.leftChild
            else -> null
        }

    private fun getParentNode(node: RBTNode<K, V>): RBTNode<K, V>? {
        val path = searchPath(node.key)
        if (path.size == 1) {
            return null
        }
        val parentNode: RBTNode<K, V> = RBTNode(path[path.size - 2].key, path[path.size - 2].value)
        return parentNode
    }

    private fun getChild(node: RBTNode<K, V>?): Stack<RBTNode<K, V>> {
        val setChild: Stack<RBTNode<K, V>> = Stack()

        if (node?.leftChild != null) {
            setChild.push(node.leftChild)
        }
        if (node?.rightChild != null) {
            setChild.push(node.rightChild)
        }

        return setChild
    }

    private fun swapParentAndChild(
        parentNode: RBTNode<K, V>?,
        node: RBTNode<K, V>,
        child: RBTNode<K, V>,
    ) {
        val auxiliaryNode: RBTNode<K, V> = RBTNode(node.key, node.value)

        auxiliaryNode.rightChild = child.rightChild
        auxiliaryNode.leftChild = child.leftChild
        parentNode?.rightChild = child
        child.rightChild = auxiliaryNode
    }

    private fun getMaxInLeftSubTree(node: RBTNode<K, V>): RBTNode<K, V> {
        var currentNode: RBTNode<K, V> = node

        while (currentNode.rightChild != null) {
            currentNode = currentNode.rightChild!!
        }
        return currentNode
    }

    private fun swapNodesForDelete(
        node1: RBTNode<K, V>,
        node2: RBTNode<K, V>,
    ): RBTNode<K, V>? {
        val auxiliaryNode: RBTNode<K, V> = RBTNode(node1.key, node1.value, color = node2.color)
        val color1 = node1.color
        val parent1: RBTNode<K, V>? = getParentNode(node1)
        val parent2: RBTNode<K, V>? = getParentNode(node2)
        val nodes1Child = getChild(node1)
        val nodes2Child = getChild(node2)

        if (parent1?.rightChild?.key == node1.key) {
            parent1.rightChild = node2
        } else {
            parent1?.leftChild = node2
        }
        if (nodes1Child.size == 2) {
            node2.leftChild = nodes1Child[0]
            node2.rightChild = nodes1Child[1]
        } else {
            node2.leftChild = nodes1Child[0]
        }
        node2.color = color1

        if (parent2?.rightChild?.key == node2.key) {
            parent2.rightChild = auxiliaryNode
        } else {
            parent2?.leftChild = auxiliaryNode
        }
        if (nodes2Child.size != 0) {
            auxiliaryNode.leftChild = nodes2Child[0]
        }

        return parent2
    }
}
