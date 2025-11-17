package monke.trees

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TwoThreeTreeTest {
    @Test
    fun `insert and search single element`() {
        val tree = TwoThreeTree<Int, String>()
        assertNull(tree.search(10))

        tree.insert(10, "a")
        assertEquals("a", tree.search(10))
        assertEquals(1, tree.size)
    }

    @Test
    fun `insert multiple elements and preserve order`() {
        val tree = TwoThreeTree<Int, String>()
        val values = (1..10).shuffled()
        values.forEach { tree.insert(it, "val$it") }

        (1..10).forEach {
            assertEquals("val$it", tree.search(it))
        }
        assertEquals(10, tree.size)
    }

    @Test
    fun `delete leaf element`() {
        val tree = TwoThreeTree<Int, String>()
        tree.insert(1, "one")
        tree.insert(2, "two")
        tree.insert(3, "three")

        val removed = tree.delete(3)
        assertEquals("three", removed)
        assertNull(tree.search(3))
        assertEquals(2, tree.size)
    }

    @Test
    fun `delete internal node element`() {
        val tree = TwoThreeTree<Int, String>()
        (1..5).forEach { tree.insert(it, "val$it") }

        val removed = tree.delete(3)
        assertEquals("val3", removed)
        assertNull(tree.search(3))
        assertEquals(4, tree.size)
    }

    @Test
    fun `insert triggers multiple splits`() {
        val tree = TwoThreeTree<Int, Int>()
        (1..20).forEach { tree.insert(it, it) }
        (1..20).forEach { assertEquals(it, tree[it]) }
        assertEquals(20, tree.size)
    }

    @Test
    fun `delete root node with children`() {
        val tree = TwoThreeTree<Int, String>()
        listOf(10, 5, 15, 3, 7, 12, 17).forEach { tree.insert(it, "val$it") }

        val removed = tree.delete(10)
        assertEquals("val10", removed)
        assertNull(tree.search(10))
        assertEquals(6, tree.size)
    }

    @Test
    fun `delete with borrowing from left sibling`() {
        val tree = TwoThreeTree<Int, Int>()
        listOf(10, 5, 15, 3, 7).forEach { tree.insert(it, it) }

        val removed = tree.delete(3)
        assertEquals(3, removed)
        assertNull(tree.search(3))
        assertEquals(4, tree.size)
    }

    @Test
    fun `delete with borrowing from right sibling`() {
        val tree = TwoThreeTree<Int, Int>()
        listOf(10, 5, 15, 12, 17).forEach { tree.insert(it, it) }

        val removed = tree.delete(17)
        assertEquals(17, removed)
        assertNull(tree.search(17))
        assertEquals(4, tree.size)
    }

    @Test
    fun `delete all elements results in empty tree`() {
        val tree = TwoThreeTree<Int, Int>()
        (1..5).forEach { tree.insert(it, it) }
        (1..5).forEach { tree.delete(it) }

        (1..5).forEach { assertNull(tree.search(it)) }
        assertEquals(0, tree.size)
    }

    @Test
    fun `iterator returns all inserted elements`() {
        val tree = TwoThreeTree<Int, Int>()
        val values = listOf(10, 5, 15, 3, 7)
        values.forEach { tree.insert(it, it) }

        val iterated = tree.map { it.first }
        values.forEach { assertTrue(iterated.contains(it)) }
    }

    @Test
    fun `inserting duplicate key updates value`() {
        val tree = TwoThreeTree<Int, String>()
        tree.insert(1, "a")
        tree.insert(1, "b")
        assertEquals("b", tree[1])
        assertEquals(1, tree.size)
    }

    @Test
    fun `search in empty tree returns null`() {
        val tree = TwoThreeTree<Int, Int>()
        assertNull(tree.search(100))
    }

    @Test
    fun `getRootNodeInfo returns correct root`() {
        val tree = TwoThreeTree<Int, Int>()
        assertNull(tree.getRootNodeInfo())
        tree.insert(5, 50)
        assertEquals(5 to 50, tree.getRootNodeInfo())
    }

    @Test
    fun `delete non-existent key returns null`() {
        val tree = TwoThreeTree<Int, Int>()
        tree.insert(1, 10)
        assertNull(tree.delete(999))
    }

    @Test
    fun `insert large number of nodes`() {
        val tree = TwoThreeTree<Int, Int>()
        (1..100).shuffled().forEach { tree.insert(it, it * 2) }
        (1..100).forEach { assertEquals(it * 2, tree[it]) }
        assertEquals(100, tree.size)
    }

    @Test
    fun `copy empty tree is empty`() {
        val tree = TwoThreeTree<Int, Int>()
        val copy = tree.copy()
        assertEquals(0, copy.size)
        assertNull(copy.getRootNodeInfo())
    }

    @Test
    fun `copy tree maintains independence after modifications`() {
        val tree = TwoThreeTree<Int, String>()
        (1..10).forEach { tree.insert(it, "val$it") }
        val copy = tree.copy()

        copy.delete(5)
        copy.delete(7)

        (1..10).forEach { assertEquals("val$it", tree[it]) }
        assertEquals(10, tree.size)

        assertNull(copy.search(5))
        assertNull(copy.search(7))
        assertEquals(8, copy.size)
    }

    @Test
    fun `insert descending order`() {
        val tree = TwoThreeTree<Int, String>()
        (10 downTo 1).forEach { tree.insert(it, "val$it") }
        (1..10).forEach { assertEquals("val$it", tree[it]) }
        assertEquals(10, tree.size)
    }

    @Test
    fun `delete root with multiple children after splits`() {
        val tree = TwoThreeTree<Int, Int>()
        (1..7).forEach { tree.insert(it, it) }
        val removed = tree.delete(4)
        assertEquals(4, removed)
        assertNull(tree.search(4))
        assertEquals(6, tree.size)
    }

    @Test
    fun `delete all elements results in empty tree after complex operations`() {
        val tree = TwoThreeTree<Int, Int>()
        (1..20).forEach { tree.insert(it, it) }
        (1..20).forEach { tree.delete(it) }

        (1..20).forEach { assertNull(tree.search(it)) }
        assertEquals(0, tree.size)
    }

    @Test
    fun `insert and delete alternating`() {
        val tree = TwoThreeTree<Int, String>()
        (1..10).forEach { tree.insert(it, "val$it") }
        (1..5).forEach { tree.delete(it) }

        val remainingKeys = tree.map { it.first }.sorted()
        assertEquals(listOf(6, 7, 8, 9, 10), remainingKeys)
        assertEquals(5, tree.size)
    }

    @Test
    fun `insert many duplicates updates size correctly`() {
        val tree = TwoThreeTree<Int, String>()
        (1..10).forEach { tree.insert(5, "val$it") }
        assertEquals(1, tree.size)
        assertEquals("val10", tree.search(5))
    }
}
