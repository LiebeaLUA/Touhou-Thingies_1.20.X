package net.liebealua.touhouthingies.structure;

public class StructureModule {
    public final int x, y, z;
    public boolean isOccupied;

    public StructureModule(int x, int y, int z, boolean isOccupied) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.isOccupied = isOccupied;
    }
}
