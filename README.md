# Finia

A modern Android app that helps users to manage their personal finance.

## Contributions

- This is Kevin Zhijun Wang's original project. 
- Many thanks to Shilei Mao who occasionally helps out with unprecedented issues in Android Studio.

## Finia's Net Worth Data Structure

Modified tree structures are used to stored the categories and values of net worth items.

#### An Innovative List Representation of the tree structure

Each individual leaf is the fundamental element of the AssetsTypeTree expressed as a list.
- The idea behind a list representation of the tree is to use each leaf node to store the address of the leaf node in the tree
  by documenting the name of its parent and its parent's parent.
- Advantages of a list representation of the Tree structure
    1. A list representation of the tree is the best way to store the results of deserialization.
    2. A list is easier to manipulate in a java setting with the Tree processor objects.

#### Net Worth Type Tree

A tree structure that stored the item name and address.
- The Type Tree provide a reference for the Value Tree to construct itself.
- The tree structure can be obtained through a deserialization of the database entity AssetsType.

#### Net Worth Value Tree

A tree structure that is mostly used to store the value of each net worth item.
- The relationship of the Value trees' nodes are mapped from the Type trees' nodes as it stored the relationship between nodes.
- Each node is an AssetsValue object which contains the id, name, value, time.

#### Type Tree Processor

A data processor is designed to retrieve data of a particular node or a collection of nodes.
- The Type Tree Processor is designed to be read-only as the business logic of Finance defines a fixed number of categories for net worth items.
- Usage:
     1. Used by the adapter of the Expandable List to retrieve the sub group of net worth items 
        in order to format the data stored in AssetsTypeTree so that they are compatible with the Expandable List widgets.
     2. Used in the Breadth-First Search algorithm to retrieve the child nodes of a particular node.

#### Value Tree Processor

The Value Tree Processor is used to manipulate the data stored in the Value tree structure.
- This processor contains methods to perform READ, EDIT, GROUP, DELETE on the data structure.
- This data source of the processor also stores all the data including the values and the Type tree.

The data stored in the processor is illustrated by the table below:

Parameters	| Type	| Description	| 
------------- | ------------------------- | ------------- |
context	| Context | The context which the processor is used
currentTime	| Date	| The current time associated with the data source
assetsValues/liabilitiesValues | List<AssetsValue>	| The data source
assetsTypeTree/liabilitiesTypeTree | List<TypeTreeLeaf_Assets> | The Type tree used to map the structure onto the Value tree

The following table shows the actions that can be performed on the Value tree structure:

Action	| Description	| Methods in Processor Class	| 
------------- | ------------------------- | ------------- |
READ	| Retrieve the data of a particular node | getAssetValue, getAllAssetsValues, getAssetsId
EDIT	| Edit the data of a particular	| setAssetValue, setAllAssetsValues 
GROUP	| Group the nodes and then retrieve the data | getAssetsChildrenNodeIDs	
DELETE	| Delete the node from the tree	| clearAllAssetsValues

#### Leaf Node of Type Tree

Each leaf node contains the name of the leaf node, and the address of the node.
A simple example below demonstrates the structure of a leaf node.

Leaf Node	| Third Level Parent	| Second Level Parent	| First Level Parent	| 
------------- | ------------------------- | ------------- | ------------- |
Name	| Address 
Real Estate	| Ownership Interests	| Invested Assets | Total Assets

Name	| Address	|||
------- | :-----------: | ----------- | ----------- | 
Leaf Node	| Third Level Parent | Second Level Parent | First Level Parent | 
Real Estate	| Ownership Interests	| Invested Assets | Total Assets |

#### Node Container

#### The DFS (Depth First Search) Algorithm




