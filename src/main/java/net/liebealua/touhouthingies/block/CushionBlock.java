package net.liebealua.touhouthingies.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CushionBlock extends Block {
    public CushionBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        pEntity.causeFallDamage(pFallDistance, 0.2f, pLevel.damageSources().fall());
    }
}
