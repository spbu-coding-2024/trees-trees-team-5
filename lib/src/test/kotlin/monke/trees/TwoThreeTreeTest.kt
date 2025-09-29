package monke.trees

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
}
