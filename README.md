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

The data stored in the Assets Value Processor is illustrated by the table below:

Parameters	| Type	| Description	| 
------------- | -------- | ------------- |
context	| Context | The context which the processor is used
currentTime	| Date	| The current time associated with the data source
assetsValues | List of AssetsValue objects	| The data source
assetsTypeTree | List of TypeTreeLeaf_Assets objects | The Type tree used to map the structure onto the Value tree

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

 Leaf Node (Name)  | Third Level Parent Node (Address) | Second Level Parent Node (Address) | First Level Parent Node (Address)
:-----------: | :-----------: | :-----------: | :-----------: |
 Real Estate | Ownership Interests | Invested Assets | Total Assets 
 Checking Accounts | / | Liquid Assets | Total Assets 
 Home Mortgage | / | Short Term Liabilities | Total Liabilities

#### Node Container

A node container is essentially a data carrier that contains the essential data of a node as well as its level in the tree structure.
It enables a more efficient access of the level as well as essential data when constructing the Multi-Expandable list.

The node container is mainly used in the following two ways:
- Constructing the Expandable Lists:

     When the data of a certain level is needed, the Type Tree Processor will prepare the node container by
     putting each node of the particular level into a node container and recording its level. After the preparation is done, the group of node containers will be injected into 
     the Expandable List Adapter. The mechanism in the adapter will assign the group to the corresponding level and retrieve the data in the node container and deliver them 
     to the right widget on the UI.
- Retrieve the data and inject into the Tree Processor:

     Each node container will be "paired" with a Input box (EditText), and a TextListener will be added to the Input Box. Once change is detected, 
     the value will be injected into an AssetsValue object and stored in the data source (list of AssetsValue objects) Value Tree Processor. 
     The id from the node container will also be injected and used as an identifier of the node.

The example below shows the structure of a node container and the group under Retirement Accounts that will be used to construct a nested expandable list:

Level | Name (Essential Data) | ID (Essential Data) | 
--- | ---- | ----
3	| IRA | 9
3	| Roth_IRA | 10
3	| 401k | 11
3	| SEP_IRA | 12
3	| Pension | 13
3	| Annuity | 14
3	| Keogh or Other Plan | 15

#### The DFS (Depth First Search) Algorithm




