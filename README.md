<p align="center"><img src="https://i.imgur.com/j33EyeF.jpeg" height="258" alt="Monke pic" /> </p>
<h1 align="center">MONKE</h1>
<p align="center">Kotlin library providing simple interface to work with data for some types of binary trees</p>

<hr>

## 🌲 Supported Trees

* Binary Search Tree
* AVL Tree
* Red-Black Tree

## 🛠️ Quick Start

```kotlin
import monke.trees.BSTree

fun main() {

    val bst = BSTree<Int, String>()

    bst.insert(1, "Example value for search method")

    println(bst.search(1)) // Example value for search method
}
```

## 💡 Features

* <b>Arithmetic operations</b> - add and subtract interface for two trees to insert or remove all nodes of one tree
  into/from another one
* <b>Get operator</b> - retrieving values using the get operator with a key

## 📃 Example

### Get value by key

```kotlin
import monke.trees.BSTree

fun main() {

    val bst = BSTree<Int, String>()

    bst.insert(1, "Example value for get method")

    println(bst[1]) // Example value for get method
}
```

### 🧮 Arithmetic

#### Plus

```kotlin
import monke.trees.BSTree

fun main() {
    val tree1 = BSTree<Int, String>()
    val tree2 = BSTree<Int, String>()
    tree1.insert(1, "value from first tree")
    tree2.insert(2, "value from second tree")

    val tree3 = tree1.copy() + tree2
    tree3.insert(-1, "value from third tree")
    for (i in tree3) {
        println(i)
    }
    /*
    (1, value from first tree)
    (-1, value from third tree)
    (2, value from second tree)
    */
}
```

> [!IMPORTANT]
>
> Inserting a node with an existing key results in an error. Ensure keys are unique before inserting.

#### Minus

```kotlin
import monke.trees.BSTree

fun main() {
    var tree1 = BSTree<Int, String>()
    tree1.insert(1, "value 1")
    tree1.insert(2, "value 2")
    tree1.insert(3, "value 3")
    val tree2 = tree1.copy()

    tree1.insert(4, "value 4")

    tree1 -= tree2

    for (i in tree1) {
        println(i)
    }
/*
(4, value 4)
*/
```

> [!IMPORTANT]
>
> As in addition, try to delete a key that does not exist in the tree throws an error.

## 📖 Documentation

To generate documentation, run:<br>
`./gradlew dokkaHtml` <br>
from the project root.
<hr>

## 👨‍💻 Authors

* [Kharisov Bogdan](https://github.com/lospollosenjoyer)
* [Oderiy Yaroslav](https://github.com/XRenso)
* [Tarasov Andrei](https://github.com/TheFollan)

## 🪪 License

This project is licensed under the [<b>MIT License</b>](LICENSE).
