<p align="center"><img src="https://i.imgur.com/ZNsaOXf.jpeg" height="258" alt="Monke pic" /> </p>
<h1 align="center">MONKE</h1>
<p align="center">Library for kotlin that help you work with data by simple interface of Binary Tree.</p>

<hr>

## 🌲 Supported Trees

* Binary Search Tree
* AVL Tree
* Red-Black Tree

## 🛠️ Quick Start

```kotlin
import monke.trees

fun main() {

    val bst = BSTree<Int, String>()

    bst.insert(1, "Example value for search method")

    println(bst.search(1)) // Example value for search method
}
```

## 💡 Features

* <b>Arithmetic operators</b> - you can plus and minus one tree from another. This will insert/delete nodes from one
  tree in another
* <b>Get operator</b> - you can get value just by get operator from key

## 📃 Example

### Get value by key

```kotlin
import monke.trees

fun main() {

    val bst = BSTree<Int, String>()

    bst.insert(1, "Example value for get method")

    println(bst[1]) // Example value for get method
}
```

> [!IMPORTANT]
>
>Get returns only value by key, when search is returning node object.

### 🧮 Arithmetic

#### Plus

```kotlin
import monke.trees

fun main() {
    val tree1 = BSTree<Int, String>()
    val tree2 = BSTree<Int, String>()
    tree1.insert(1, "Value from first tree")
    tree2.insert(2, "Value from second tree")

    val tree3 = tree1.copy() + tree2
    tree3.insert(-1, "Value from third tree")
    for (i in tree3) {
        println(i)
    }
    /*
    (1, Value from first tree)
    (-1, Value from third tree)
    (2, Value from second tree)
    */
}
```

> [!IMPORTANT]
>
>Inserting node with same key will throw error, so you need to check if keys is unique

#### Minus

```kotlin
import monke.trees

fun main() {
    var tree1 = BSTree<Int, String>()
    tree1.insert(1, "Value 1")
    tree1.insert(2, "Value 2")
    tree1.insert(3, "Value 3")
    val tree2 = tree1.copy()

    tree1.insert(4, "Value 4")
    tree1 -= tree2
    for (i in tree1) {
        println(i)
    }
/*
(4, Value 4)
*/
```

> [!IMPORTANT]
>
> How in plus if you try to delete key which is not in tree it will throw an error.


## 📖 Documentation
You need to build it with `./gradlew dokkaHtml` in project root

<hr>

## 👨‍💻 Authors

* [Kharisov Bogdan](https://github.com/lospollosenjoyer)
* [Oderiy Yaroslav](https://github.com/XRenso)
* [Tarasov Andrei](https://github.com/TheFollan)


## 🪪 License

This projected is licensed under the [<b>MIT License</b>](LICENSE).