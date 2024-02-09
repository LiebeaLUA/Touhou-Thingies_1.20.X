package net.liebealua.touhouthingies.client.renderer;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.model.DollEnemyModel;
import net.liebealua.touhouthingies.entity.dollEnemy.DollEnemy;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DollEnemyRenderer extends MobRenderer<DollEnemy, DollEnemyModel<DollEnemy>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TouhouThingies.MODID, "textures/entity/doll_enemy.png");

    public DollEnemyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new DollEnemyModel<>(pContext.bakeLayer(DollEnemyModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DollEnemy dollEnemy) {
        if(true) {
            return TEXTURE;
        } else {
            return null;
        }
    }
}
