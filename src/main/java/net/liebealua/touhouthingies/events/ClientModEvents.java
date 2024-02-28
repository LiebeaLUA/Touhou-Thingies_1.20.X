package net.liebealua.touhouthingies.events;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.model.DollEnemyModel;
import net.liebealua.touhouthingies.client.model.LancerDollEnemyModel;
import net.liebealua.touhouthingies.client.renderer.DollEnemyRenderer;
import net.liebealua.touhouthingies.client.renderer.LancerDollEnemyRenderer;
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
        event.registerEntityRenderer(EntityRegistry.LANCER_DOLL_ENEMY.get(), LancerDollEnemyRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DollEnemyModel.LAYER_LOCATION, DollEnemyModel::createBodyLayer);
        event.registerLayerDefinition(LancerDollEnemyModel.LAYER_LOCATION, LancerDollEnemyModel::createBodyLayer);
    }
}
