package TDB;

import TDB.TopologyComponents.Node;

import java.io.IOException;

public class DB {

    TableGroup group;
    TableTopology topology;
    TopologyLibrary lib;
    private boolean override = false;
    StorageMedia media = null;

    public DB(TableGroup group) throws IOException {
        this.group = group;
        this.lib = group.lib;
        reload();
    }

    public DB(TableGroup group, StorageMedia media) throws IOException {
        this.group = group;
        this.lib = group.lib;
        this.media = media;
        override = media != null;
        reload();
    }

    public void save() throws IOException {
        if (override){
            media.write(group);
            return;
        }
        FileSystem.write(group);
    }

    public void reload() throws IOException {
        if (override){
            media.read(lib);
        }
        group = FileSystem.read(lib);
    }

    public Object executeQuery(String query,String extraArguments, Object data) throws IOException {
        switch (query){
            case "SELECT":
                try {
                    topology = group.group.get((Integer) data);
                } catch (ClassCastException e) {
                    System.out.println("Invalid Data type for given query: SELECT");
                    return null;
                }
                break;
            case "UPDATE":
                try {
                    topology.InsertData(data, topology.Parse(extraArguments));
                }catch (Exception e){
                    System.out.println("Invalid Extra Argument / Unable to Parse the Extra Argument. Extra Argument:"+extraArguments);

                    return null;
                }
                break;
            case "ADD":
                Node node = topology.AddNewData(data);
                System.out.println(node);
                return node;
            case "DELETE":
                try {
                    topology.RemoveData(topology.GetNode(topology.Parse((String) data)));
                }catch (Exception e){
                    System.out.println("Invalid Extra Argument / Unable to Parse the Extra Argument. Extra Argument:"+extraArguments);

                    return null;
                }
                break;
            case "READ":
                try{
                    return topology.GetNode(topology.Parse((String) data));
                } catch (Exception e) {
                    System.out.println("Invalid Extra Argument / Unable to Parse the Extra Argument. Extra Argument:"+extraArguments);
                }
            case "CREATE":
                try {
                    int index = (Integer) data;
                    System.out.println(group.group.size());
                    group.group.add(lib.getByIndex((byte) index).getCopy());
                    return group.group.size()-1;
                } catch (Exception e) {
                    System.out.println("Invalid Topology library index. Index Object:" + data.toString());
                    return null;
                }
            case "EXTRACT":
                System.out.println(topology.ExtractData());
                return topology.ExtractData();
            case "IMPORT":
                topology.ImportData((byte[])data);
                break;
            case "REMOVE_TABLE":
                group.group.remove(topology);
                break;
            case "RELOAD":
                reload();
                break;
            case "SAVE":
                save();
                break;
        }
        System.out.println("Query succeed!");
        return null;
    }
}
