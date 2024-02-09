package net.liebealua.touhouthingies.registries;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.item.TestItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TouhouThingies.MODID);

    public static final RegistryObject<ForgeSpawnEggItem> DOLL_ENEMY_SPAWN_EGG = ITEMS.register("doll_enemy_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.DOLL_ENEMY, 0x139dbf, 0xffffff, new Item.Properties()));

    public static final RegistryObject<BlockItem> CUSHION_BLOCK_ITEM = ITEMS.register("cushion_block",
            () -> new BlockItem(BlockRegistry.CUSHION_BLOCK.get(), new Item.Properties()));


    public static final  RegistryObject<TestItem> TEST = ITEMS.register("test",
            () -> new TestItem(new Item.Properties()));
}
