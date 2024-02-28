package net.liebealua.touhouthingies.client.renderer;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.model.LancerDollEnemyModel;
import net.liebealua.touhouthingies.entity.lancerDollEnemy.LancerDollEnemy;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LancerDollEnemyRenderer extends MobRenderer<LancerDollEnemy, LancerDollEnemyModel<LancerDollEnemy>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TouhouThingies.MODID, "textures/entity/lancer_doll_enemy.png");

    public LancerDollEnemyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LancerDollEnemyModel<>(pContext.bakeLayer(LancerDollEnemyModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LancerDollEnemy lancerDollEnemy) {
        return TEXTURE;
    }
}
