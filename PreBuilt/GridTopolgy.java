package TDB.PreBuilt;

import TDB.TableTopology;
import TDB.TopologyComponents.Node;
import TDB.TopologyComponents.NodeIdentifier;

import java.io.*;
import java.util.ArrayList;

public class GridTopolgy implements TableTopology {

    public static class GridNode implements Node,Serializable{
        Object data;
        int x;
        int y;
    }

    public static class GridNodeIdentifier implements NodeIdentifier{
        int x;
        int y;
    }

    ArrayList<ArrayList<GridNode>> datas = new ArrayList<>();
    int Xsize;
    int Ysize;

    public GridTopolgy(int Xsize,int Ysize){
        this.Xsize = Xsize;
        this.Ysize = Ysize;
    }

    public GridTopolgy(){
        this.Xsize = 10000;
        this.Ysize = 10000;
    }

    @Override
    public TableTopology getCopy() {
        return new GridTopolgy();
    }

    @Override
    public String id() {
        return "grid-8429244-topology";
    }

    @Override
    public void ClearData() {
        datas = new ArrayList<>();
    }

    @Override
    public NodeIdentifier Parse(String nodeData) {
        try {
            GridNodeIdentifier node = new GridNodeIdentifier();
            node.x = Integer.parseInt(nodeData.substring(1));
            node.y = Integer.parseInt(nodeData.substring(1, 2));
            return node;
        } catch (Exception e) {
            throw new RuntimeException("Invalid parsing of data ("+nodeData+") for GridNodeIdentifier");
        }
    }

    @Override
    public void InsertData(Object data, Node node) {
        datas.get(((GridNode)node).x).get(((GridNode)node).y).data = data;
    }

    @Override
    public void InsertData(Object data, String nodeData) {
        InsertData(data,GetNode(nodeData));
    }

    @Override
    public void InsertData(Object data, NodeIdentifier nodeIdentifier) {
        InsertData(data,GetNode(nodeIdentifier));
    }

    @Override
    public Node AddNewData(Object data) {
        GridNode node = new GridNode();
        node.data = data;
        if (datas.size() >= Xsize) {
            return null;
        }
        if(datas.getLast().size() < (Ysize)){
            node.x = datas.size() -1;
            node.y = datas.getLast().size();
            datas.getLast().add(node);
        }else {
            ArrayList<GridNode> nodes = new ArrayList<>();
            node.x = datas.size();
            node.y = 0;
            nodes.add(node);
            datas.add(nodes);
        }
        return node;
    }

    @Override
    public Node GetNode(NodeIdentifier identifier) {
        return datas.get(((GridNodeIdentifier)identifier).x).get(((GridNodeIdentifier)identifier).y);
    }

    @Override
    public Node GetNode(String nodeData) {
        return GetNode(Parse(nodeData));
    }

    @Override
    public void RemoveData(Node node) {
        datas.get(((GridNode)node).x).remove(((GridNode) node).y);
    }

    @Override
    public void RemoveData(NodeIdentifier identifier) {
        RemoveData(GetNode(identifier));
    }

    @Override
    public void RemoveData(String nodeData) {
        RemoveData(GetNode(nodeData));
    }

    @Override
    public byte[] ExtractData() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream obj = new ObjectOutputStream(baos);
            obj.writeObject(datas);
            return baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public void ImportData(byte[] table) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(table);
            ObjectInputStream os = new ObjectInputStream(bais);
            datas = (ArrayList<ArrayList<GridNode>>) os.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
