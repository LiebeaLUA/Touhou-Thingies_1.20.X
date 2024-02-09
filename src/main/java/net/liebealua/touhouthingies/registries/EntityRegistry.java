package net.liebealua.touhouthingies.registries;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.entity.dollEnemy.DollEnemy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TouhouThingies.MODID);

    public static final RegistryObject<EntityType<DollEnemy>> DOLL_ENEMY = ENTITIES.register("doll_enemy",
            () -> EntityType.Builder.<DollEnemy>of(DollEnemy::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.5f)
                    .build(new ResourceLocation(TouhouThingies.MODID, "doll_enemy").toString()));
}
