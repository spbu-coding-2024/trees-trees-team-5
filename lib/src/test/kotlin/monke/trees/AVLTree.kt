package monke.trees

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AVLTreeTest {
    private val tree: AVLTree<Int, String> = AVLTree()

    @Test
    fun `search the node in empty tree`() {
        val searchKey = 0
        assertNull(tree.search(searchKey))
    }

    @Test()
    fun `delete the node from empty tree`() {
        val deleteKey = 0

    }
}