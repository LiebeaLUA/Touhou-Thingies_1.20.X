package net.liebealua.touhouthingies.structure;

import org.joml.Vector3i;

import java.util.HashMap;

public class StructureModuleGrid {
    public HashMap<Vector3i, Boolean> grid;

    public boolean getModuleAt(Vector3i coords) {
//        if (!grid.containsKey(coords))
//            grid.put(coords, false);
        grid.putIfAbsent(coords, false);
        return grid.get(coords);
    }

    public void setModuleAt(Vector3i coords, boolean value) {
        grid.put(coords, value);
    }
}
