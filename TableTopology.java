package TDB;

import TDB.TopologyComponents.Node;
import TDB.TopologyComponents.NodeIdentifier;

public interface TableTopology {

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

}
