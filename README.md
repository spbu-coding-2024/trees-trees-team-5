# Monke - library for easy work with binary trees

<b>Monke</b> is library for kotlin that help you work with data by simple interface of Binary Tree.

## Features
* <b>Simple code</b> - you can write clear and simple code for work with trees.

## Example
### Simple insert and search element
```kotlin
import trees.BSTree

fun main(){
    
    val bst = BSTree<Int,String>()
    
    bst.insert(1,"Example value for search method")
    
    println(bst.search(1).value) // Example value for search method
}
```

### Get value by key
```kotlin
import trees.BSTree

fun main(){
    
    val bst = BSTree<Int,String>()
    
    bst.insert(1,"Example value for get method")
    
    println(bst[1]) // Example value for get method
}
```

>[!IMPORTANT]
>
>Get returns only value by key, when search is returning node object.

### Arithmetic
#### Plus
```kotlin
import trees.BSTree

fun main(){
    val tree1 = BSTree<Int, String>()
    val tree2 = BSTree<Int, String>()
    tree1.insert(1,"Value from first tree")
    tree2.insert(2,"Value from second tree")

    val tree3 = tree1.copy() + tree2
    tree3.insert(-1,"Value from third tree")
    for (i in tree3){
        println(i)
    }
    /*
    (1, Value from first tree)
    (-1, Value from third tree)
    (2, Value from second tree)
    */
}
```

>[!IMPORTANT]
>
>Inserting node with same key will throw error, so you need to check if keys is unique

#### Minus
```kotlin
    var tree1 = BSTree<Int, String>()
    tree1.insert(1,"Value 1")
    tree1.insert(2,"Value 2")
    tree1.insert(3,"Value 3")
    val tree2 = tree1.copy()

    tree1.insert(4,"Value 4")
    tree1 -= tree2
    for (i in tree1){
        println(i)
    }
    /*
    (4, Value 4)
    */
```
> [!IMPORTANT]
> 
> How in plus if you try to delete key which is not in tree it will throw an error
