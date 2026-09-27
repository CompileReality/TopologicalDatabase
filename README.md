# Topological Database (TDB)
A java database management system for managing datas which are meant to arranged non-linearly or linearly with custom storage media option, endless topological structures for data management, and minimalized query commands.

## Why TDB?
- It allows us to arrange very-complex data in meaningfull structures
- Allows to override pre-built data storing method, so that data can be stored anywhere as per our need.
- Generialized framework structure of this database helps us to make custom topological structures/tables to arrange the complex data in meaningfull way.
- Minimalized Query commands with maximum 2 arguments allows even beginners to create their fully production-ready database.

## Working of TDB

Initialization of Database happens when `DB` class's constructor has been called. It initializes the necessary variables then retrives the data from either `StorageMedia` class or executes pre-built file read-write system, the reason behind `StorageMedia` seperatly introduced is to allow developer to save and reload the data wherever they needed for example, if the data is to be stored on other cloud storage or third party storage system, in that case `FileSystem` is completely useless, so to fix that problem we introduced `StorageMedia` interface. We will later discuss on how to make custom class which implements this interface and integrate with all other componenets.

For proper initialization, Database has to be feed exact order of `TableTopology` instances through `TopologyLibrary` as while converting the data to array of bytes the information about the data corresponds to which TableTopology is stored as index of that TableTopology in `TopologyLibrary` class's `lib` arraylist. This problem may not occur if you implement `StorageMedia` and make the data identifiable so that it can be correctly map to it's corresponding `TableTopology`.

When a Query is feed to `DB` through `executeQuery` method whose 3 arguments are QUERY, EXTRA_ARGUMENT, DATA.
It depend on QUERY, whether EXTRA_ARGUMENT should be null or not, as some query command does need an extra input to execute.
Switch through the QUERY, the necessary task is executed and Output is omitted as `Object` class.

The QUERY commands are following:
---
1. **SELECT**:<br>
        Selects the table to further work on. It doesn't need EXTRA_ARGUMENT and can be null, DATA type should be an integer and corresponds to index of Table present in `TableGroup` class's `group` arraylist. Output is null in this command.
---
2. **ADD**:<br>
        Adds the provided DATA to selected to TableTopology, and prints out the `Node` added in topology as string. `Node` is also omitted as output object. EXTRA_ARGUMENT can be null as there's no need.
---
3. **UPDATE**:<br>
        Overrides the provided DATA to selected to TableTopology in place of node represented by provided `NodeIdentifier` as EXTRA_ARGUMENT.
---
4. **READ**:<br>
        Retrives the node represented by provided `NodeIdentifier` as DATA (String) from selected TableTopology and EXTRA_ARGUMENT being ignored here.
---
5. **DELETE**:<br>
        Deletes the node represented by provided `NodeIdentifier` as DATA (String) from selected TableTopology and EXTRA_ARGUMENT being ignored here.
---
6. **CREATE**:<br>
        Creates a new TableTopology copy from the TopologyLibrary and adds it to table group arraylist regardless of no.of copies already present in TableGroup class's `group` arraylist. It requires only DATA which is integer and is index of that TableTopology in TopologyLibrary arraylist. EXTRA_ARGUMENT is ignored and can be set to null. Ouput is the index at which copy is added which can be further used in **SELECT** command.
---
7. **EXTRACT**:<br>
        Outputs full extracted data in form of byte array of selected topology.This neither requires DATA nor EXTRA_ARGUMENTS. 
---
8. **IMPORT**:<br>
        Imports full extracted data which is in form of byte array and provided as DATA. This imports all data present in it directly in TableTopology.
> The DATA here is Topology sensitive meaning it does depends on whether the data provided to TableTopology corresponds to the same or not, if not then the corrupted data may get injected.
---
9. **REMOVE_TABLE**:<br>
        Removes the the table from group and also from storage media when saved.
---
10. **RELOAD**:<br>
        Reloads the whole data previously stored data, regardless of changed the data
---
11. **SAVE**:<br>
        Saves the whole data in storage through either `StorageMedia` or `FileSystem` depending upon whether `StorageMedia` is provided or not.
---

`DB` class handles every Query commands, but it handles the job of arranging the data to selected `TableTopology`.

`TableTopology` interface allows actual manageing part of data that is storing,retriveing or deleting any node from it's structure. The example of implemenation of this interface to build custom Topology is provided in PreBuilt package, `GridTopology` class.
`TableTopology` interface has 

``` java
    TableTopology getCopy();
    String id();
    void ClearData();
    NodeIdentifier Parse(String nodeData);
    void InsertData(Object data, Node node);
    void InsertData(Object data, String nodeData);
    void InsertData(Object data, NodeIdentifier nodeIdentifier);
    Node AddNewData(Object data);
    Node GetNode(NodeIdentifier identifier);
    Node GetNode(String nodeData);
    void RemoveData(Node node);
    void RemoveData(NodeIdentifier identifier);
    void RemoveData(String nodeData);
    byte[] ExtractData();
    void ImportData(byte[] table);
```

Their names are their literal function. The functions which are important to dicuss are `getCopy` and `id`.
1. `getCopy`:<br>
        It create another copy of the class which doesn't have any data or return a copy of itself after erasing all data. It should not return a new instance of other child class of `TableTopology` interface. This is used in `TopologyLibrary` as blueprint of structure, so that this framework doesn't collapse while working with multiple Topologies.
2. `id`:<br>
        This returns the String of ID of Topology. ID is unqiue for every TableTopology in TopologyLibrary. This is used to identify whether the TableTopology is already present in the library.

These both functions serves an important role holding this framework and not collapsing when working with multiple TableTopology. Actual part of data structure and mangement should be handled by the child of this interface. Every TableTopology must have it's own seperate `Node` and `NodeIdentifier` interface's child, the reason behind making seperate `Node` and `NodeIdentifier` is, Data and it's relation with other Data may depend on actual structure of that TableTopology and similarly the Identification of Data may not only depend on data but also it's position or relation with other data.
`GridTopology` does have it's own Node and NodeIdentifier which is named as `GridNode` and `GridNodeIdentifier`.

`StorageMedia` is interface which allows this Database to connect to other storage media for storing actual data as per need. It only contains 2 functions
1. **read**:<br>
It takes one argument which is `TopologyLibrary`. `TopologyLibrary` is used to map the data to it's corresponding `TableTopology`. It returns the `TableGroup` which contains `TopologyLibrary` as well as Arraylist of `TableTopology` which is nothing but stored data.

2. **write**:<br>
It takes one argument which is `TableGroup`. It reads stored data from Arraylist of `TableTopology` and maps that data to corresponding `TableTopology` and extracts the data then stores it.

For storing data as File, there's already a class `FileSystem` and it is default mode for `DB` class. If there's no specified `StorageMedia`, `DB` class uses this class to store and retirve the data. But it has a drawback and that is `TableTopology` must be serializable to store it and not every class can be Serializable so we recommend switching to `StorageMedia` if data is too-complex allowing you full freedom over data storing efficiency.

## Contribution
We appreciate and welcome your contributions to this project.

## License
This project is licensed under Apache 2.0 License.