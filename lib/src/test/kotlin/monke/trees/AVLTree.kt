package monke.trees

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class AVLTreeTest {
    private fun <K> defaultNodeValue(key: K): String = "value of $key"

    private fun <K : Comparable<K>, V> treeBuilder(valueBuilder: (K) -> V, vararg keys: K): AVLTree<K, V> {
        val tree: AVLTree<K, V> = AVLTree()
        for (key in keys) {
            tree.insert(key, valueBuilder(key))
        }
        return tree
    }

    private fun <K : Comparable<K>, V> areSimilarTrees(first: AVLTree<K, V>, second: AVLTree<K, V>): Boolean {
        val firstTreeList: MutableList<Pair<K, V>> = mutableListOf()
        val secondTreeList: MutableList<Pair<K, V>> = mutableListOf()

        for (node in first) {
            firstTreeList.add(node)
        }

        for (node in second) {
            secondTreeList.add(node)
        }

        return firstTreeList.size == secondTreeList.size && firstTreeList.containsAll(secondTreeList)
    }


    @Nested
    @TestInstance(PER_CLASS)
    inner class EmptyTreeTest {
        private val tree: AVLTree<Int, String> = AVLTree()

        @Test
        fun `height of empty tree`() {
            val expectedHeight = 0
            assertEquals(expectedHeight, tree.getHeight())
        }

        @Test
        fun `get root node info from empty tree`() {
            assertNull(tree.getRootNodeInfo())
        }

        @Test
        fun `search the node in empty tree`() {
            val searchKey = 0
            val exceptionMessage: String? = assertFailsWith<NoSuchElementException> {
                tree.search(searchKey)
            }.message
            assertEquals(exceptionMessage, "Node with key $searchKey does not exist yet")
        }

        @Test
        fun `get the node from empty tree`() {
            val getKey = 0
            val exceptionMessage: String? = assertFailsWith<NoSuchElementException> {
                tree.search(getKey)
            }.message
            assertEquals(exceptionMessage, "Node with key $getKey does not exist yet")
        }

        @Test
        fun `delete the node from empty tree`() {
            val deleteKey = 0
            val exceptionMessage: String? = assertFailsWith<NoSuchElementException> {
                tree.delete(deleteKey)
            }.message
            assertEquals(exceptionMessage, "Node with key $deleteKey does not exist yet")
        }
    }

    @Nested
    inner class InsertTest {
        @Nested
        inner class SingleInsertTest {
            @Test
            fun `insert root node`() {
                val tree: AVLTree<Int, String> = AVLTree()
                val insertKey = 0
                val insertValue = defaultNodeValue(insertKey)
                tree.insert(insertKey, insertValue)
                assertEquals(tree[insertKey], insertValue)
            }

            @Test
            fun `insert child node for root`() {
                val tree: AVLTree<Int, String> = AVLTree()
                val rootKey = 0
                val rootValue = defaultNodeValue(rootKey)
                tree.insert(rootKey, rootValue)
                val oldHeight = 1

                val insertKey = 1
                val insertValue = defaultNodeValue(insertKey)
                tree.insert(insertKey, insertValue)
                val expectedHeight = oldHeight + 1
                assertEquals(tree[insertKey], insertValue)
                assertEquals(expectedHeight, tree.getHeight())
            }
        }
    }

    @Nested
    inner class DeleteTest {
        @Nested
        inner class SingleDeleteTest {
            @Test
            fun `delete root node`() {
                val deleteKey = 0
                val tree: AVLTree<Int, String> = treeBuilder(::defaultNodeValue, deleteKey)
                val oldHeight = 1

                tree.delete(deleteKey)
                val expectedHeight = oldHeight - 1

                val expected: AVLTree<Int, String> = AVLTree()
                assertTrue(areSimilarTrees(expected, tree))
                assertEquals(expectedHeight, tree.getHeight())
            }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class RepetitiveCasesTest {
        private val tree: AVLTree<Int, String> = treeBuilder(::defaultNodeValue, 1, -1, 0)

        @Test
        fun `double insert of similar nodes`() {
            val firstTryKey = 2
            val firstTryValue = defaultNodeValue(firstTryKey)

            val secondTryKey = 2
            val secondTryValue = defaultNodeValue(secondTryKey)

            val exceptionMessage: String? = assertFailsWith<IllegalArgumentException> {
                with(tree) {
                    insert(firstTryKey, firstTryValue)
                    insert(secondTryKey, secondTryValue)
                }
            }.message
            assertEquals(exceptionMessage, "Node with key $secondTryKey already exists")
        }

        @Test
        fun `double delete of same node`() {
            val firstTryKey = 2
            val secondTryKey = 2

            val exceptionMessage: String? = assertFailsWith<NoSuchElementException> {
                with(tree) {
                    delete(firstTryKey)
                    delete(secondTryKey)
                }
            }.message
            assertEquals(exceptionMessage, "Node with key $secondTryKey does not exist yet")
        }
    }
}
