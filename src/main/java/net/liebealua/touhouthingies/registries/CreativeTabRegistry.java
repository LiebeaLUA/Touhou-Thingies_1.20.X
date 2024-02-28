package net.liebealua.touhouthingies.registries;

import net.liebealua.touhouthingies.TouhouThingies;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TouhouThingies.MODID);

    public static final RegistryObject<CreativeModeTab> TOUHOU_THINGIES_TAB = TABS.register("touhou_thingies_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.touhou_thingies_tab"))
                    .icon(ItemRegistry.CUSHION_BLOCK_ITEM.get()::getDefaultInstance)
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ItemRegistry.CUSHION_BLOCK_ITEM.get());
                        output.accept(ItemRegistry.DOLL_ENEMY_SPAWN_EGG.get());
                        output.accept(ItemRegistry.LANCER_DOLL_ENEMY_SPAWN_EGG.get());
                    })
                    .build());
}
