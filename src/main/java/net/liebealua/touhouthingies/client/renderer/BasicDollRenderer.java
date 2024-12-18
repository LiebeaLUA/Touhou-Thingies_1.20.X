package net.liebealua.touhouthingies.client.renderer;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.model.BasicDollModel;
import net.liebealua.touhouthingies.entity.dolls.BasicDoll;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BasicDollRenderer extends MobRenderer<BasicDoll, BasicDollModel<BasicDoll>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TouhouThingies.MODID, "textures/entity/basic_doll.png");
    private static final ResourceLocation TEXTURE2 =
            new ResourceLocation(TouhouThingies.MODID, "textures/entity/basic_doll_explosive.png");

    public BasicDollRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BasicDollModel<>(pContext.bakeLayer(BasicDollModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(BasicDoll basicDoll) {
        if(!basicDoll.getExplosive()) {
            return TEXTURE;
        } else {
            return TEXTURE2;
        }
    }

    @Override
    protected boolean isShaking(BasicDoll pEntity) {
        return super.isShaking(pEntity) || pEntity.getPrimed();
    }

    @Override
    protected float getWhiteOverlayProgress(BasicDoll pLivingEntity, float pPartialTicks) {
        return super.getWhiteOverlayProgress(pLivingEntity, pPartialTicks);
    }
}
