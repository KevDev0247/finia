# Finia

A modern Android app that helps users to manage their personal finance.

## Contributions

This is Kevin Zhijun Wang's original project. 
Many thanks to Shilei Mao who occasionally helps out with unprecedented issues in Android Studio.

## Finia's Net Worth Data Structure

Modified tree structures are used to stored the categories and values of net worth items.

#### An Innovative List Representation of Trees

In order 
Each individual leaf is the fundamental element of the AssetsTypeTree expressed as a list.

#### Net Worth Type Tree

A tree structure that stored the item name and address.
The Type Tree provide a reference for the Value Tree to construct itself.
The tree structure can be obtained through a deserialization of the database entity AssetsType.
a

#### Net Worth Value Tree

A tree structure that is mostly used to store the value of each net worth item.
This data structure is dependent on the Type Tree as it stored the relationship between nodes.

#### Type Tree Processor

A data processor is designed to retrieve data of a particular node or a collection of nodes.
The Type Tree Processor is designed to be read-only as the business logic of Finance defines a fixed number of categories for net worth items.
Therefore, the Type Tree Processor only allow the READ action.
The type processor is mostly used by the adapter of the Expandable List to retrieve the sub group of net worth items 
in order to format the data stored in AssetsTypeTree so that they are compatible with the Expandable List widgets.
It is also used in the Breadth-First Search algorithm to retrieve the child nodes of a particular node.

#### Value Tree Processor

The Value Tree Processor is 

Action	| Description	| Methods in Processor	| 
------------- | ------------------------- | ------------- |
READ	| Retrieve the data of a particular node | getAssetValue, getAllAssetsValues, getAssetsId
EDIT	| Edit the data of a particular	| setAssetValue, setAllAssetsValues 
GROUP	| Group the nodes and then retrieve the data | getAssetsChildrenNodeIDs	
DELETE	| Delete the node from the tree	| clearAllAssetsValues

#### Leaf Node of Value Tree

#### Node Container


