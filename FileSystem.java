package TDB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystem {

    public static void write(TableGroup group) throws IOException {
        Path path = Paths.get("database.tdb");
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        int i = 0;
        for (TableTopology topo:group.group){
            byte[] datas = topo.ExtractData();
            int size = datas.length;
            data.write(new byte[]{(byte) group.lib.getIndex(topo.id())});
            data.write(new byte[]{(byte)(size>>24),(byte)(size>>16),(byte)(size>>8),(byte)(size)});
            data.write(datas);
        }
        Files.writeString(path,data.toString());
    }

    public static TableGroup read(TopologyLibrary lib) throws IOException {
        Path path = Paths.get("database.tdb");
        byte[] data = Files.readAllBytes(path);
        TableGroup group = new TableGroup();
        int index = 0;
        while(index < data.length){
            byte indexOfLib = data[index+1];
            index++;
            int lengthOfBlock = (data[index+1]<<24) | (data[index+2]<<16) | (data[index+3]<<8) | (data[index+4]);
            ByteArrayOutputStream byt = new ByteArrayOutputStream();
            for (int i = 0; i < lengthOfBlock; i++) {
                byt.write(data[index]);
                index++;
            }
            lib.getByIndex(indexOfLib).ImportData(byt.toByteArray());
            group.group.add(lib.getByIndex(indexOfLib).getCopy());
            lib.getByIndex(indexOfLib).ClearData();
        }
        group.lib = lib;
        return group;
    }
}
