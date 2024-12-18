package net.liebealua.touhouthingies.registries;

import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.entity.dolls.BasicDoll;
import net.liebealua.touhouthingies.entity.dolls.LancerDoll;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TouhouThingies.MODID);

    public static final RegistryObject<EntityType<BasicDoll>> BASIC_DOLL = ENTITIES.register("basic_doll",
            () -> EntityType.Builder.<BasicDoll>of(BasicDoll::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.4f)
                    .build(new ResourceLocation(TouhouThingies.MODID, "basic_doll").toString()));

    public static final RegistryObject<EntityType<LancerDoll>> LANCER_DOLL = ENTITIES.register("lancer_doll",
            () -> EntityType.Builder.<LancerDoll>of(LancerDoll::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.4f)
                    .build(new ResourceLocation(TouhouThingies.MODID, "lancer_doll").toString()));
}
