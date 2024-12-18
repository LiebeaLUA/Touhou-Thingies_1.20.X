package net.liebealua.touhouthingies.events;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.model.BasicDollModel;
import net.liebealua.touhouthingies.client.model.LancerDollModel;
import net.liebealua.touhouthingies.client.renderer.BasicDollRenderer;
import net.liebealua.touhouthingies.client.renderer.LancerDollRenderer;
import net.liebealua.touhouthingies.registries.EntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouThingies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.BASIC_DOLL.get(), BasicDollRenderer::new);
        event.registerEntityRenderer(EntityRegistry.LANCER_DOLL.get(), LancerDollRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BasicDollModel.LAYER_LOCATION, BasicDollModel::createBodyLayer);
        event.registerLayerDefinition(LancerDollModel.LAYER_LOCATION, LancerDollModel::createBodyLayer);
    }
}
