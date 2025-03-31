package monke.trees

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class AVLTreeTest {
    private fun <K> defaultNodeValue(key: K): String = "value of $key"

    private fun <K : Comparable<K>, V> treeBuilder(
        valueBuilder: (K) -> V,
        vararg keys: K,
    ): AVLTree<K, V> {
        val tree: AVLTree<K, V> = AVLTree()
        for (key in keys) {
            tree.insert(key, valueBuilder(key))
        }
        return tree
    }

    private fun <K : Comparable<K>, V> areSimilarTrees(
        first: AVLTree<K, V>,
        second: AVLTree<K, V>,
    ): Boolean {
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
    @TestInstance(PER_METHOD)
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
            val exceptionMessage: String? =
                assertFailsWith<NoSuchElementException> {
                    tree.search(searchKey)
                }.message
            assertEquals(exceptionMessage, "Node with key $searchKey does not exist yet")
        }

        @Test
        fun `get the node from empty tree`() {
            val getKey = 0
            val exceptionMessage: String? =
                assertFailsWith<NoSuchElementException> {
                    tree[getKey]
                }.message
            assertEquals(exceptionMessage, "Node with key $getKey does not exist yet")
        }

        @Test
        fun `delete the node from empty tree`() {
            val deleteKey = 0
            val exceptionMessage: String? =
                assertFailsWith<NoSuchElementException> {
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
                assertNull(tree.getRootNodeInfo())

                val insertKey = 0
                val insertValue = defaultNodeValue(insertKey)

                tree.insert(insertKey, insertValue)
                val expectedRootNodeInfo: Pair<Int, String> = Pair(insertKey, insertValue)

                assertNotNull(tree.getRootNodeInfo())
                assertEquals(expectedRootNodeInfo, tree.getRootNodeInfo())
            }

            @Test
            fun `insert child node to root node`() {
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

            @Nested
            inner class RotationTest {
                @Test
                fun `insert causes left rotation`() {
                    val firstKey = 50
                    val secondKey = 100
                    val tree: AVLTree<Int, String> = treeBuilder(::defaultNodeValue, firstKey, secondKey)

                    val insertKey = 150
                    val insertValue = defaultNodeValue(insertKey)
                    tree.insert(insertKey, insertValue)

                    val expectedRootNodeInfo: Pair<Int, String> = Pair(secondKey, defaultNodeValue(secondKey))
                    val expectedHeight = 2

                    assertEquals(expectedRootNodeInfo, tree.getRootNodeInfo())
                    assertEquals(expectedHeight, tree.getHeight())
                }

                @Test
                fun `insert causes right-left rotation`() {
                    val firstKey = 50
                    val secondKey = 100
                    val tree: AVLTree<Int, String> = treeBuilder(::defaultNodeValue, firstKey, secondKey)

                    val insertKey = 75
                    val insertValue = defaultNodeValue(insertKey)
                    tree.insert(insertKey, insertValue)

                    val expectedRootNodeInfo: Pair<Int, String> = Pair(insertKey, defaultNodeValue(insertKey))
                    val expectedHeight = 2

                    assertEquals(expectedRootNodeInfo, tree.getRootNodeInfo())
                    assertEquals(expectedHeight, tree.getHeight())
                }

                @Test
                fun `insert causes right rotation`() {
                    val firstKey = 50
                    val secondKey = 0
                    val tree: AVLTree<Int, String> = treeBuilder(::defaultNodeValue, firstKey, secondKey)

                    val insertKey = -50
                    val insertValue = defaultNodeValue(insertKey)
                    tree.insert(insertKey, insertValue)

                    val expectedRootNodeInfo: Pair<Int, String> = Pair(secondKey, defaultNodeValue(secondKey))
                    val expectedHeight = 2

                    assertEquals(expectedRootNodeInfo, tree.getRootNodeInfo())
                    assertEquals(expectedHeight, tree.getHeight())
                }

                @Test
                fun `insert causes left-right rotation`() {
                    val firstKey = 50
                    val secondKey = 0
                    val tree: AVLTree<Int, String> = treeBuilder(::defaultNodeValue, firstKey, secondKey)

                    val insertKey = 25
                    val insertValue = defaultNodeValue(insertKey)
                    tree.insert(insertKey, insertValue)

                    val expectedRootNodeInfo: Pair<Int, String> = Pair(insertKey, defaultNodeValue(insertKey))
                    val expectedHeight = 2

                    assertEquals(expectedRootNodeInfo, tree.getRootNodeInfo())
                    assertEquals(expectedHeight, tree.getHeight())
                }
            }
        }

        @Nested
        inner class MultipleInsertTest {
            @Test
            fun `insert root node with its child nodes`() {
                val tree: AVLTree<Int, String> = AVLTree()
                val rootKey = 50
                val rootValue = defaultNodeValue(rootKey)

                val rightKey = 100
                val rightValue = defaultNodeValue(rightKey)
                val leftKey = 0
                val leftValue = defaultNodeValue(leftKey)

                tree.insert(rootKey, rootValue)
                tree.insert(rightKey, rightValue)
                tree.insert(leftKey, leftValue)

                val expectedRootNodeInfo: Pair<Int, String> = Pair(rootKey, rootValue)
                val expectedHeight = 2

                assertEquals(expectedRootNodeInfo, tree.getRootNodeInfo())
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
                assertNotNull(tree.getRootNodeInfo())

                tree.delete(deleteKey)

                val expectedHeight = oldHeight - 1

                assertNull(tree.getRootNodeInfo())
                assertEquals(expectedHeight, tree.getHeight())
            }

            @Test
            fun `delete child node of root node`() {
                val rootKey = 0
                val childKey = 1
                val tree: AVLTree<Int, String> = treeBuilder(::defaultNodeValue, rootKey, childKey)
                val oldHeight = 2

                tree.delete(childKey)

                val expectedTree = treeBuilder(::defaultNodeValue, rootKey)
                val expectedHeight = oldHeight - 1

                assertTrue(areSimilarTrees(expectedTree, tree))
                assertEquals(expectedHeight, tree.getHeight())
            }

            @Nested
            inner class RotationTest {
                @Test
                fun `delete causes left rotation`() {
                    val firstKey = 50
                    val deleteKey = 0
                    val secondKey = 100
                    val thirdKey = 150
                    val tree: AVLTree<Int, String> =
                        treeBuilder(
                            ::defaultNodeValue,
                            firstKey,
                            deleteKey,
                            secondKey,
                            thirdKey,
                        )
                    val oldHeight = 3

                    tree.delete(deleteKey)

                    val expectedTree: AVLTree<Int, String> =
                        treeBuilder(
                            ::defaultNodeValue,
                            secondKey,
                            firstKey,
                            thirdKey,
                        )
                    val expectedHeight = oldHeight - 1

                    assertTrue(areSimilarTrees(expectedTree, tree))
                    assertEquals(expectedHeight, tree.getHeight())
                }

                @Test
                fun `delete causes right-left rotation`() {
                    val firstKey = 50
                    val deleteKey = 0
                    val secondKey = 100
                    val thirdKey = 75
                    val tree: AVLTree<Int, String> =
                        treeBuilder(
                            ::defaultNodeValue,
                            firstKey,
                            deleteKey,
                            secondKey,
                            thirdKey,
                        )
                    val oldHeight = 3

                    tree.delete(deleteKey)

                    val expectedTree: AVLTree<Int, String> =
                        treeBuilder(
                            ::defaultNodeValue,
                            thirdKey,
                            firstKey,
                            secondKey,
                        )
                    val expectedHeight = oldHeight - 1

                    assertTrue(areSimilarTrees(expectedTree, tree))
                    assertEquals(expectedHeight, tree.getHeight())
                }

                @Test
                fun `delete causes right rotation`() {
                    val firstKey = 50
                    val secondKey = 0
                    val deleteKey = 100
                    val thirdKey = -50
                    val tree: AVLTree<Int, String> =
                        treeBuilder(
                            ::defaultNodeValue,
                            firstKey,
                            secondKey,
                            deleteKey,
                            thirdKey,
                        )
                    val oldHeight = 3

                    tree.delete(deleteKey)

                    val expectedTree: AVLTree<Int, String> =
                        treeBuilder(
                            ::defaultNodeValue,
                            secondKey,
                            thirdKey,
                            firstKey,
                        )
                    val expectedHeight = oldHeight - 1

                    assertTrue(areSimilarTrees(expectedTree, tree))
                    assertEquals(expectedHeight, tree.getHeight())
                }

                @Test
                fun `delete causes left-right rotation`() {
                    val firstKey = 50
                    val secondKey = 0
                    val deleteKey = 100
                    val thirdKey = 25
                    val tree: AVLTree<Int, String> =
                        treeBuilder(
                            ::defaultNodeValue,
                            firstKey,
                            secondKey,
                            deleteKey,
                            thirdKey,
                        )
                    val oldHeight = 3

                    tree.delete(deleteKey)

                    val expectedTree: AVLTree<Int, String> =
                        treeBuilder(
                            ::defaultNodeValue,
                            thirdKey,
                            secondKey,
                            firstKey,
                        )
                    val expectedHeight = oldHeight - 1

                    assertTrue(areSimilarTrees(expectedTree, tree))
                    assertEquals(expectedHeight, tree.getHeight())
                }
            }
        }
    }

    @Nested
    @TestInstance(PER_METHOD)
    inner class RepetitiveCaseTest {
        private val tree: AVLTree<Int, String> = treeBuilder(::defaultNodeValue, 1, -1, 0)

        @Test
        fun `double insert of similar nodes`() {
            val firstTryKey = 2
            val firstTryValue = defaultNodeValue(firstTryKey)

            val secondTryKey = 2
            val secondTryValue = defaultNodeValue(secondTryKey)

            val exceptionMessage: String? =
                assertFailsWith<IllegalArgumentException> {
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

            val exceptionMessage: String? =
                assertFailsWith<NoSuchElementException> {
                    with(tree) {
                        delete(firstTryKey)
                        delete(secondTryKey)
                    }
                }.message
            assertEquals(exceptionMessage, "Node with key $secondTryKey does not exist yet")
        }
    }
}
