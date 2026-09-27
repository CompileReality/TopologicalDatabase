package TDB;

public interface StorageMedia {
    public TableGroup read(TopologyLibrary lib);
    public void write(TableGroup group);
}
