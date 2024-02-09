package net.liebealua.touhouthingies.events;


import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.entity.dollEnemy.DollEnemy;
import net.liebealua.touhouthingies.registries.EntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouThingies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.DOLL_ENEMY.get(), DollEnemy.createAttributes().build());
    }
}
