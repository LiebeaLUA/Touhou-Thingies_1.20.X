package net.liebealua.touhouthingies.registries;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.block.CushionBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TouhouThingies.MODID);

    public static final RegistryObject<Block> CUSHION_BLOCK = BLOCKS.register("cushion_block",
            () -> new CushionBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_MAGENTA)
                    .sound(SoundType.WOOL)
                    .strength(1.5f)));
}
