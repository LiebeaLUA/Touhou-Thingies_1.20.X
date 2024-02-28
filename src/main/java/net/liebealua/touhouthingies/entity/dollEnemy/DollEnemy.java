package net.liebealua.touhouthingies.entity.dollEnemy;

import net.liebealua.touhouthingies.client.animation.DollEnemyAnimation;
import net.liebealua.touhouthingies.registries.EntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DollEnemy extends Monster {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    public final AnimationState shakeAnimationState = new AnimationState();
    private static final EntityDataAccessor<Integer> DATA_FUSE_COUNTDOWN = SynchedEntityData.defineId(DollEnemy.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_PRIMED = SynchedEntityData.defineId(DollEnemy.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_EXPLOSIVE = SynchedEntityData.defineId(DollEnemy.class, EntityDataSerializers.BOOLEAN);
    public DamageSource deathSource;
    private static final int maxFuseTime = 80;
    private static final int explosionRadius = 2;
    private SoundEvent fuseSound = SoundEvents.BOTTLE_FILL;

    public DollEnemy(EntityType<DollEnemy> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public DollEnemy(Level level, double x, double y, double z) {
        this(EntityRegistry.DOLL_ENEMY.get(), level);
        setPos(x, y, z);
    }
    public DollEnemy(Level level, BlockPos position) {
        this(level, position.getX(), position.getY(), position.getZ());
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 6.0f));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Pig>(this, Pig.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<ZombifiedPiglin>(this, ZombifiedPiglin.class, true));
    }

    @Override
    public void tick() {
        if(this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(!this.isInWaterOrBubble() && !this.walkAnimation.isMoving() && !this.getPrimed(), this.tickCount);
            this.deathAnimationState.animateWhen(this.getPrimed(), this.tickCount);
            this.shakeAnimationState.animateWhen(this.getPrimed(), this.tickCount);
        }

        if (!this.level().isClientSide()) {
            if (this.getPrimed()) {
                this.setFuse(this.getFuse() - 1);
                if (this.getFuse() % 2 == 0)
                    playSound(fuseSound, ((maxFuseTime - this.getFuse())/100F), ((maxFuseTime - this.getFuse())/(maxFuseTime/2F)));
                if (this.getFuse() <= 0) {
                    this.explode();
                    if (this.deathSource != null) {
                        super.die(deathSource);
                    }
                    this.remove(RemovalReason.KILLED);
                }
            }

            if (isInWater()) {
                this.setExplosive(false);
                this.setPrimed(false);
            }
        }

        super.tick();
    }

    @Override
    public void die(@NotNull DamageSource pDamageSource) {
        if (this.getExplosive()) {
            this.setHealth(1);
            if (!this.getPrimed()) {
                if (!this.level().isClientSide()) {
                    this.deathSource = pDamageSource;
                }
                this.setPrimed(true);
            }
        } else {
            super.die(pDamageSource);
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_PRIMED.equals(pKey)) {
            if (this.getPrimed()) {
                this.removeFreeWill();
                this.setTarget(null);
                this.getNavigation().stop();
                this.refreshDimensions();
                this.playSound(SoundEvents.TNT_PRIMED);
                if (this.level().getRandom().nextFloat() < 0.05F) {
                    this.fuseSound = SoundEvents.CAT_AMBIENT;
                }
            } else {
                this.setFuse(maxFuseTime);
                this.refreshDimensions();
                this.registerGoals();
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (this.random.nextFloat() < 0.2F || pLevel.getBlockState(this.blockPosition().below()).is(Blocks.RED_NETHER_BRICKS)) {
            this.setExplosive(true);
        } else {
            this.setExplosive(false);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        if (this.getPrimed()) {
            return new EntityDimensions(super.getDimensions(pPose).width, 1.0f, super.getDimensions(pPose).fixed);
        } else {
            return super.getDimensions(pPose);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return this.getDimensions(pPose).height * 0.8F;
    }

    protected void explode() {
        this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), explosionRadius, Level.ExplosionInteraction.NONE);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FUSE_COUNTDOWN, maxFuseTime);
        this.entityData.define(DATA_PRIMED, false);
        this.entityData.define(DATA_EXPLOSIVE, false);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putShort("Fuse", (short)this.getFuse());
        pCompound.putBoolean("Primed", this.getPrimed());
        pCompound.putBoolean("Explosive", this.getExplosive());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.setFuse(pCompound.getShort("Fuse"));
        this.setPrimed(pCompound.getBoolean("Primed"));
        this.setExplosive(pCompound.getBoolean("Explosive"));
    }

    public void setFuse(int pFuse) {
        this.entityData.set(DATA_FUSE_COUNTDOWN, pFuse);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_COUNTDOWN);
    }

    public void setPrimed(boolean pPrimed) {
        this.entityData.set(DATA_PRIMED, pPrimed);
    }

    public boolean getPrimed() {
        return this.entityData.get(DATA_PRIMED);
    }

    public void setExplosive(boolean pExplosive) {
        this.entityData.set(DATA_EXPLOSIVE, pExplosive);
    }

    public boolean getExplosive() {
        return this.entityData.get(DATA_EXPLOSIVE);
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }
}

//                            SoundEvents.CHICKEN_EGG
//                            SoundEvents.GHAST_HURT
//                            SoundEvents.GHAST_WARN
//                            SoundEvents.END_PORTAL_FRAME_FILL
//                            SoundEvents.GENERIC_HURT
//                            SoundEvents.CAT_AMBIENT
//                            SoundEvents.CHAIN_BREAK
//                            SoundEvents.ANVIL_PLACE
//                            SoundEvents.FLINTANDSTEEL_USE
//                            SoundEvents.GENERIC_DRINK
//                            SoundEvents.CHICKEN_AMBIENT
