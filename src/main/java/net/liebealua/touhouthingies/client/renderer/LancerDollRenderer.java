package net.liebealua.touhouthingies.client.renderer;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.model.LancerDollModel;
import net.liebealua.touhouthingies.entity.dolls.LancerDoll;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LancerDollRenderer extends MobRenderer<LancerDoll, LancerDollModel<LancerDoll>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TouhouThingies.MODID, "textures/entity/lancer_doll.png");

    public LancerDollRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LancerDollModel<>(pContext.bakeLayer(LancerDollModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LancerDoll lancerDoll) {
        return TEXTURE;
    }
}
