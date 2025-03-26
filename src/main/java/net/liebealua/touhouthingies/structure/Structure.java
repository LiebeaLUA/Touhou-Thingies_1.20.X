package net.liebealua.touhouthingies.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Structure {
    Vec3i moduleSize = new Vec3i(8, 8, 8);
    Vec3i offset;

    public Structure() {
    }

    public void generate(BlockPos blockPos, Level level, StructurePieces piece) {
        offset = new Vec3i(
                Mth.positiveModulo(blockPos.getX(), moduleSize.getX()),
                Mth.positiveModulo(blockPos.getY(), moduleSize.getY()),
                Mth.positiveModulo(blockPos.getZ(), moduleSize.getZ())
        );

        List<Vec3i> occupiedModules = new ArrayList<Vec3i>();

        occupiedModules.add(blockPosToModuleCoords(blockPos));

        if (!level.isClientSide()) {
            //Place da fuggin' structure

        }

        //Minecraft.getInstance().player.sendSystemMessage(Component.literal("" + blockPosToModuleCoords(blockPos)));
    }

    public Vec3i blockPosToModuleCoords(BlockPos blockPos) {
        return new Vec3i(
                Mth.floor((float) blockPos.getX() / moduleSize.getX()),
                Mth.floor((float) blockPos.getY() / moduleSize.getY()),
                Mth.floor((float) blockPos.getZ() / moduleSize.getZ())
        );
    }
}
