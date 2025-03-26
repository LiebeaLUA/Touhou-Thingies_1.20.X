package net.liebealua.touhouthingies.item;

import net.liebealua.touhouthingies.structure.Structure;
import net.liebealua.touhouthingies.structure.StructurePieces;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class StructureDebugItem extends Item {
    public StructureDebugItem(Properties pProperties) {
        super(pProperties);
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//
//        pPlayer.getCooldowns().addCooldown(this, 5);
//        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide());
//    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        Structure structure = new Structure();
        structure.generate(pContext.getClickedPos(), pContext.getLevel(), StructurePieces.TEST_SMALL);

        return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide);
    }
}
