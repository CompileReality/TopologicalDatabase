package TDB;

import java.util.ArrayList;
import java.util.Objects;

public class TopologyLibrary {
    ArrayList<TableTopology> lib = new ArrayList<>();

    public void add(TableTopology topo){
        for (TableTopology topology: lib){
            if (Objects.equals(topology.id(), topo.id())){
                return;
            }
        }
        lib.add(topo);
    }

    public TableTopology get(String id){
        for (TableTopology topology: lib){
            if (Objects.equals(topology.id(), id)){
                return topology;
            }
        }
        return null;
    }

    public TableTopology getByIndex(byte index){
        return lib.get(index);
    }

    public int getIndex(String id){
        int i = 0;
        for (TableTopology topology: lib){
            if (Objects.equals(topology.id(), id)){
                return i;
            }
            i++;
        }
        return -1;
    }
}
