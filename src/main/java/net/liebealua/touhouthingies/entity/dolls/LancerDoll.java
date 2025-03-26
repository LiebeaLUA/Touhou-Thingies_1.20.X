package net.liebealua.touhouthingies.entity.dolls;

import net.liebealua.touhouthingies.entity.ai.goal.lancerDoll.LancerDollLungeAttackGoal;
import net.liebealua.touhouthingies.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class LancerDoll extends AbstractDoll {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState chargeAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> DATA_ATTACKING = SynchedEntityData.defineId(LancerDoll.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_AIM_TIME = SynchedEntityData.defineId(LancerDoll.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_AIM_Y_ROT = SynchedEntityData.defineId(LancerDoll.class, EntityDataSerializers.FLOAT);
    public WaterAvoidingRandomFlyingGoal randomFlyingGoal;
    private BlockPos boundOrigin;
    public int timeSinceLastAttack = 0;

    public LancerDoll(EntityType<LancerDoll> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, false);
    }
    public LancerDoll(Level level, double x, double y, double z) {
        this(EntityRegistry.LANCER_DOLL.get(), level);
        setPos(x, y, z);
    }
    public LancerDoll(Level level, BlockPos position) {
        this(level, position.getX(), position.getY(), position.getZ());
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    protected @NotNull PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LancerDollLungeAttackGoal(this, 1.0, 8.0f, 2.0f, 40, 100));
        this.goalSelector.addGoal(2, randomFlyingGoal = new WaterAvoidingRandomFlyingGoal(this, 1.0f));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 6.0f));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Pig.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, ZombifiedPiglin.class, true));
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
        //don't
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.800000011920929));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                BlockPos ground = this.getBlockPosBelowThatAffectsMyMovement();
                float f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }

                float f1 = 0.16277137F / (f * f * f);
                f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }

                this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)f));
            }
        }

        this.calculateEntityAnimation(true);
    }

    @Override
    public void tick() {
        if(this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(!this.getAttacking(), this.tickCount);
            this.chargeAnimationState.animateWhen(this.getAttacking() && this.getAimTime() < 60, this.tickCount);
            this.attackAnimationState.animateWhen(this.getAttacking() && this.getAimTime() >= 60, this.tickCount);
        }


        if (this.getAttacking()) {
            this.setYRot(getAimYRot());
            this.yBodyRot = getAimYRot();
            this.yHeadRot = getAimYRot();
        }

        if (!getAttacking())
            timeSinceLastAttack++;



        super.tick();
    }




    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos pBoundOrigin) {
        this.boundOrigin = pBoundOrigin;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return this.getDimensions(pPose).height * 0.75F;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {

    }



    public void addAdditionalSaveData(CompoundTag pCompound) {
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }




    public boolean getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public void setAttacking(boolean isAttacking) {
        this.entityData.set(DATA_ATTACKING, isAttacking);
    }

    public int getAimTime() {
        return this.entityData.get(DATA_AIM_TIME);
    }

    public void setAimTime(int aimTime) {
        this.entityData.set(DATA_AIM_TIME, aimTime);
    }

    public float getAimYRot() {
        return this.entityData.get(DATA_AIM_Y_ROT);
    }

    public void setAimYRot(float rotat) {
        this.entityData.set(DATA_AIM_Y_ROT, rotat);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACKING, false);
        this.entityData.define(DATA_AIM_TIME, 0);
        this.entityData.define(DATA_AIM_Y_ROT, 0.0f);
    }
}
