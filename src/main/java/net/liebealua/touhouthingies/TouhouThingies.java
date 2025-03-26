package net.liebealua.touhouthingies;

import net.liebealua.touhouthingies.registries.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TouhouThingies.MODID)
public class TouhouThingies {
    public static final String MODID = "touhouthingies";

    public TouhouThingies() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.ITEMS.register(bus);
        BlockRegistry.BLOCKS.register(bus);
        EntityRegistry.ENTITIES.register(bus);
        CreativeTabRegistry.TABS.register(bus);
        SoundRegistry.SOUND_EVENTS.register(bus);
    }
}
