package monke.trees

import monke.nodes.Entry
import monke.nodes.TwoThreeTreeNode
import monke.trees.treeInterfaces.BTree
/**
 * Implementation of a 2-3 Tree data structure.
 *
 * A 2-3 Tree is a balanced search tree where every internal node
 * can contain either one key (2-node) or two keys (3-node), and
 * accordingly has 2 or 3 children. The tree guarantees logarithmic
 * time complexity for search, insertion, and deletion operations.
 *
 * @param K the type of keys, must implement [Comparable]
 * @param V the type of values stored in the tree
 */
public class TwoThreeTree<K : Comparable<K>, V> : BTree<K, V>{
    /**
     * Current root of the tree, or `null` if the tree is empty.
     */
    protected var root: TwoThreeTreeNode<K, V>? = null
    /**
     * Number of key-value pairs currently stored in the tree.
     */
    var size = 0
        private set
    /**
     * Searches for a value by the given [key].
     *
     * @param key the key to search for
     * @return the value associated with the key, or `null` if not found
     */
    override fun search(key: K) : V?{
        return getRecursive(root, key)
    }
    /**
     * Inserts a new key-value pair into the tree.
     *
     * If the key already exists, its value will be replaced.
     * Splitting is performed automatically when nodes overflow.
     *
     * @param key the key to insert
     * @param value the value to associate with the key
     * @return the value that was inserted
     */
    override fun insert(key: K, value: V): V?{

        if(root == null){
            root = TwoThreeTreeNode(entries = mutableListOf(Entry(key, value)))
            size = 1
            return value
        }

        var node = root

        while(!node!!.isLeaf){
            node = chooseChild(node, key)
        }

        insertEntryInNode(node, Entry(key, value))
        size++

        var current = node
        while(current != null && current.entries.size == 3){
            splitNode(current)
            current = current.parent
        }
        return value
    }
    /**
     * Deletes a key (and its associated value) from the tree.
     *
     * Balancing is performed automatically (borrowing or merging)
     * if a node underflows.
     *
     * @param key the key to delete
     * @return the value that was removed, or `null` if the key was not found
     */
    override fun delete(key: K): V? {
        val node = findNode(root, key) ?: return null

        if(node.isLeaf){
            val oldValue  = removeEntry(node, key)
            fixUnderFlow(node)
            size--
            return oldValue
        }

        val index = node.entries.indexOfFirst { it.key == key }
        val leftChild = node.children[index]
        var replacementNode = leftChild

        while(!replacementNode.isLeaf){
            replacementNode = replacementNode.children.last()
        }

        val replacementEntry = replacementNode.entries.last()

        node.entries[index] = Entry(replacementEntry.key, replacementEntry.value)

        removeEntry(replacementNode, replacementEntry.key)
        fixUnderFlow(replacementNode)
        size--

        return replacementEntry.value
    }

    private fun getRecursive(node: TwoThreeTreeNode<K, V>?, key: K) : V?{
        if(node == null) return null

        node.entries.find { it.key == key }?.let { return it.value }

        return if (node.isLeaf) null else getRecursive(chooseChild(node, key), key)
    }

    private fun chooseChild(node: TwoThreeTreeNode<K, V>, key: K) : TwoThreeTreeNode<K,V> {
        val listOfKey = node.entries
        return when (listOfKey.size){
            1 -> if(key < listOfKey[0].key) node.children[0] else node.children[1]
            2 -> when{
                key < listOfKey[0].key -> node.children[0]
                key > listOfKey[1].key -> node.children[2]
                else -> node.children[1]
            }
            else -> throw IllegalArgumentException("incorrect number of key in node")
        }
    }

    private fun insertEntryInNode(node: TwoThreeTreeNode<K, V>, entry: Entry<K, V>){
        val index = node.entries.indexOfFirst { it.key > entry.key }
        if (index == -1) node.entries.add(entry)
        else node.entries.add(index, entry)
    }

    private fun splitNode(node: TwoThreeTreeNode<K, V>) {
        if (node.entries.size != 3) return

        val leftEntry = node.entries[0]
        val middleEntry = node.entries[1]
        val rightEntry = node.entries[2]

        val leftNode = TwoThreeTreeNode(entries = mutableListOf(leftEntry), parent = node.parent)
        val rightNode = TwoThreeTreeNode(entries = mutableListOf(rightEntry), parent = node.parent)

        if (!node.isLeaf) {
            val mid = node.children.size / 2
            leftNode.children.addAll(node.children.subList(0, mid))
            rightNode.children.addAll(node.children.subList(mid, node.children.size))
            leftNode.children.forEach { it.parent = leftNode }
            rightNode.children.forEach { it.parent = rightNode }
        }

        if (node.parent == null){
            val newRoot = TwoThreeTreeNode(entries = mutableListOf(middleEntry))
            newRoot.children.add(leftNode)
            newRoot.children.add(rightNode)
            leftNode.parent = newRoot
            rightNode.parent = newRoot
            root = newRoot
        } else {
            val parent = node.parent
            val index = parent!!.entries.indexOfFirst { it.key > middleEntry.key }
            if (index == -1) parent.entries.add(middleEntry)
            else parent.entries.add(index, middleEntry)

            val childIndex = parent.children.indexOf(node)
            parent.children.removeAt(childIndex)
            parent.children.add(childIndex, leftNode)
            parent.children.add(childIndex + 1, rightNode)
        }
    }

    private fun findNode(node : TwoThreeTreeNode<K, V>?, key : K): TwoThreeTreeNode<K, V>?{
        var currentNode = node

        while(currentNode != null){

            currentNode.entries.find { it.key == key }?.let { return currentNode }

            if(currentNode.isLeaf) return null

            currentNode = chooseChild(currentNode, key)
        }
        return null
    }

    private fun removeEntry(node : TwoThreeTreeNode<K, V>, key : K): V?{
        val index = node.entries.indexOfFirst { it.key == key }

        if(index != -1){
            val entry = node.entries.removeAt(index)
            return entry.value
        } else return null
    }

    private fun fixUnderFlow(node : TwoThreeTreeNode<K, V>){
        if(node.entries.size >= 1) return

        val parent = node.parent ?: run {
            if(node.entries.isEmpty() && node.children.isNotEmpty()){
                root = node.children.first()
                root?.parent = null
            }
            return
        }

        val index = parent.children.indexOf(node)
        val leftSibling = if(index > 0) parent.children[index - 1] else null
        val rightSibling = if(index < parent.children.size - 1) parent.children[index+1] else null

        if(leftSibling != null && leftSibling.entries.size > 1){
            borrowFromLeft(node, leftSibling, parent, index)
            return
        }
        if(rightSibling != null && rightSibling.entries.size > 1){
            borrowFromRight(node, rightSibling, parent, index)
            return
        }
        if(leftSibling != null){
            mergeWithLeft(node, leftSibling,parent, index)
        }
        else if(rightSibling != null){
            mergeWithRight(node, rightSibling, parent, index)
        }
    }

    private fun borrowFromLeft(
        node: TwoThreeTreeNode<K, V>,
        leftSibling: TwoThreeTreeNode<K, V>,
        parent: TwoThreeTreeNode<K, V>,
        index: Int
    ){
        val borrowEntry = leftSibling.entries.removeAt(leftSibling.entries.lastIndex)
        val parentEntry = parent.entries[index - 1]

        node.entries.add(0, parentEntry)
        parent.entries[index - 1] = borrowEntry

        if(leftSibling.children.isNotEmpty()){
            val child = leftSibling.children.removeAt(leftSibling.children.lastIndex)
            node.children.add(0, child)
            child.parent = node
        }
    }

    private fun borrowFromRight(
        node: TwoThreeTreeNode<K, V>,
        rightSibling: TwoThreeTreeNode<K,V>,
        parent: TwoThreeTreeNode<K, V>,
        index: Int
    ){
        val borrowEntry = rightSibling.entries.removeAt(0)
        val parentEntry = parent.entries[index]

        node.entries.add(parentEntry)
        parent.entries[index] = borrowEntry

        if(rightSibling.children.isNotEmpty()){
            val child = rightSibling.children.removeAt(0)
            node.children.add(child)
            child.parent = node
        }
    }

    private fun mergeWithLeft(
        node: TwoThreeTreeNode<K, V>,
        leftSibling: TwoThreeTreeNode<K, V>,
        parent: TwoThreeTreeNode<K, V>,
        index: Int
    ) {
        leftSibling.entries.add(parent.entries[index - 1])
        leftSibling.entries.addAll(node.entries)
        leftSibling.children.addAll(node.children)
        node.children.forEach { it.parent = leftSibling }

        parent.entries.removeAt(index - 1)
        parent.children.removeAt(index)

        fixUnderFlow(parent)
    }

    private fun mergeWithRight(
        node: TwoThreeTreeNode<K, V>,
        rightSibling: TwoThreeTreeNode<K,V>,
        parent: TwoThreeTreeNode<K, V>,
        index: Int
    ){
        node.entries.add(parent.entries[index])
        node.entries.addAll(rightSibling.entries)
        node.children.addAll(rightSibling.children)
        rightSibling.children.forEach { it.parent = node }

        parent.entries.removeAt(index)
        parent.children.removeAt(index + 1)

        fixUnderFlow(parent)
    }
}