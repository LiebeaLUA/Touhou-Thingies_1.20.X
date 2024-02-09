package net.liebealua.touhouthingies.entity.dollEnemy;

import net.liebealua.touhouthingies.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import org.jetbrains.annotations.NotNull;

public class DollEnemy extends Monster {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    public final AnimationState shakeAnimationState = new AnimationState();
    private static final EntityDataAccessor<Integer> DATA_FUSE_COUNTDOWN = SynchedEntityData.defineId(DollEnemy.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_PRIMED = SynchedEntityData.defineId(DollEnemy.class, EntityDataSerializers.BOOLEAN);
    public DamageSource deathSource;
    private static final int maxFuseTime = 80;
    private static final int explosionRadius = 2;

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
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 6.0f));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(this, Player.class, true));
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Pig>(this, Pig.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<ZombifiedPiglin>(this, ZombifiedPiglin.class, true));
    }

    @Override
    public void tick() {
        if(this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(!this.isInWaterOrBubble() && !this.walkAnimation.isMoving() && !this.getPrimed(), this.tickCount);
        }

        if (this.getPrimed()) {

            if (level().isClientSide()) {
                this.idleAnimationState.stop();
                this.deathAnimationState.startIfStopped(this.tickCount);
//                if (this.deathAnimationState.getAccumulatedTime() >= DollEnemyAnimation.death.lengthInSeconds() * 20) {
//                    this.deathAnimationState.stop();
//                    this.shakeAnimationState.start(this.tickCount);
//                }
            }
            this.setFuse(this.getFuse() - 1);
            if (this.getFuse() <= 0) {
                if (!this.level().isClientSide) {
                    this.explode();
                }
                super.die(this.deathSource);
                this.remove(RemovalReason.KILLED);
            }
        }

        super.tick();
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.setHealth(1);
        if (!this.getPrimed()) {
            this.deathSource = pDamageSource;
            this.setPrimed(true);
            this.removeFreeWill();
            this.playSound(SoundEvents.TNT_PRIMED);
//            this.deathAnimationState.start(tickCount);
        }
    }

    protected void explode() {
        this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), explosionRadius, Level.ExplosionInteraction.MOB);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FUSE_COUNTDOWN, maxFuseTime);
        this.entityData.define(DATA_PRIMED, false);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putShort("Fuse", (short)this.getFuse());
        pCompound.putBoolean("Primed", this.getPrimed());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.setFuse(pCompound.getShort("Fuse"));
        this.setPrimed(pCompound.getBoolean("Primed"));
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

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }
}