package net.liebealua.touhouthingies.events;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.model.DollEnemyModel;
import net.liebealua.touhouthingies.client.renderer.DollEnemyRenderer;
import net.liebealua.touhouthingies.registries.EntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouThingies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.DOLL_ENEMY.get(), DollEnemyRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DollEnemyModel.LAYER_LOCATION, DollEnemyModel::createBodyLayer);
    }
}
