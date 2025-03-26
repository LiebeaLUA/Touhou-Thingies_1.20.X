package net.liebealua.touhouthingies.structure;

import net.minecraft.core.Vec3i;

public enum StructurePieces {

    TEST_SMALL ("test1x1", new Vec3i(1,1,1)),
    TEST_CORRIDOR ("-----", Vec3i.ZERO);


    private final String name;
    private final Vec3i size;
    private enum BlockPalette {};

    StructurePieces(String name, Vec3i size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return this.name;
    }

    public Vec3i getSize() {
        return size;
    }
}
