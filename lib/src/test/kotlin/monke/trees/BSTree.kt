package monke.trees

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BSTreeTest {
    private fun <K : Comparable<K>, V> compareTree(tree: BSTree<K, V>, expected: List<Pair<K, V>>): Boolean {
        var idx = 0
        for (i in tree) {
            if (expected[idx] != i) return false
            idx++
        }
        return true
    }

    @Test
    fun `Single insert BST test`() {
        val tree = BSTree<Int, String>()
        val value = "Single value"
        tree.insert(1, value)
        assertEquals(value, tree[1])
    }

    @Test
    fun `Multiple insert BST test`() {
        val tree = BSTree<Int, String>()
        val firstValue = "First Value"
        val secondValue = "Second Value"
        val thirdValue = "Third Value"
        val expectedResult = listOf(Pair(1, firstValue), Pair(-1, thirdValue), Pair(2, secondValue))
        tree.insert(1, firstValue)
        tree.insert(2, secondValue)
        tree.insert(-1, thirdValue)

        assertEquals(firstValue, tree[1])
        assertEquals(secondValue, tree[2])
        assertEquals(thirdValue, tree[-1])

        assertEquals(true, compareTree(tree, expectedResult))
    }

    @Test
    fun `Delete only one node BST test`() {
        val tree = BSTree<Int, String>()
        val value = "Test Value"
        tree.insert(1, value)
        assertEquals(value, tree.delete(1))
        assertNull(tree[1])
    }

    @Test
    fun `Delete BST test with two children`() {
        val tree = BSTree<Int, Int>()
        tree.insert(1, 1)
        tree.insert(2, 2)
        tree.insert(-1, -1)
        val expectedResult = listOf(Pair(-1, -1), Pair(2, 2))

        assertEquals(1, tree.delete(1))
        assertEquals(true, compareTree(tree, expectedResult))
    }

    @Test
    fun `Delete BST test only right child`() {
        val tree = BSTree<Int, Int>()
        tree.insert(1, 1)
        tree.insert(2, 2)
        tree.insert(-1, -1)
        tree.insert(3, 3)
        val expectedResult = listOf(Pair(1, 1), Pair(-1, -1), Pair(3, 3))

        assertEquals(2, tree.delete(2))
        assertEquals(true, compareTree(tree, expectedResult))
    }

    @Test
    fun `Delete BST test only left child`() {
        val tree = BSTree<Int, Int>()
        tree.insert(1, 1)
        tree.insert(3, 3)
        tree.insert(-1, -1)
        tree.insert(2, 2)
        val expectedResult = listOf(Pair(1, 1), Pair(-1, -1), Pair(2, 2))

        assertEquals(3, tree.delete(3))
        assertEquals(true, compareTree(tree, expectedResult))
    }

    @Test
    fun `Insert BST test same key`() {
        assertThrows(IllegalArgumentException::class.java) {
            val tree = BSTree<Int, Int>()
            tree.insert(1, 1)
            tree.insert(1, 2)
        }
    }

    @Test
    fun `Delete BST test not exist key`() {
        assertThrows(NoSuchElementException::class.java) {
            val tree = BSTree<Int, Int>()
            tree.delete(1)
        }
    }

    @Test
    fun `Delete root BST test `() {
        val tree = BSTree<Int, Int>()
        tree.insert(1, 1)
        tree.insert(3, 3)
        tree.insert(-1, -1)
        tree.insert(2, 2)
        val expectedResult = listOf(Pair(-1, -1), Pair(3, 3), Pair(2, 2))

        assertEquals(1, tree.delete(1))
        assertEquals(true, compareTree(tree, expectedResult))
    }

    @Test
    fun `Copy BST test`() {
        val tree = BSTree<Int, Int>()
        tree.insert(1, 1)
        val tree2 = tree.copy()
        tree2.insert(2, 2)
        val expectedResultTree1 = listOf(Pair(1, 1))
        val expectedResultTree2 = listOf(Pair(1, 1), Pair(2, 2))

        assertEquals(true, compareTree(tree, expectedResultTree1))
        assertEquals(true, compareTree(tree2, expectedResultTree2))

    }
}