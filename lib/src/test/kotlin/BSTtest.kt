package monke.trees

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class BSTreeTest{
    @Test
    fun `Basic insert BST test`(){
        val tree = BSTree<Int, String>()
        val firstValue = "First Value"
        val secondValue = "Second Value"
        val thirdValue = "Third Value"
        val expectedResult = listOf(Pair(1, firstValue), Pair(-1, thirdValue), Pair(2, secondValue))
        tree.insert(1,firstValue )
        tree.insert(2,secondValue )
        tree.insert(-1,thirdValue )

        assertEquals(firstValue, tree[1])
        assertEquals(secondValue, tree[2])
        assertEquals(thirdValue, tree[-1])

        var idx = 0
        for (i in tree){
            assertEquals(expectedResult[idx], i)
            idx++
        }
    }
}