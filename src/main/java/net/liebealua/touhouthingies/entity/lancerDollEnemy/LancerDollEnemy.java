package net.liebealua.touhouthingies.entity.lancerDollEnemy;

import net.liebealua.touhouthingies.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
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

import javax.annotation.Nullable;
import java.util.EnumSet;

public class LancerDollEnemy extends Monster {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState chargeAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    private ActionPhase actionPhase;
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING = SynchedEntityData.defineId(LancerDollEnemy.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(LancerDollEnemy.class, EntityDataSerializers.BOOLEAN);
//    private static final EntityDataAccessor<Integer> DATA_TIME_SINCE_LAST_ATTACK = SynchedEntityData.defineId(LancerDollEnemy.class, EntityDataSerializers.INT);
    private BlockPos boundOrigin;

    public LancerDollEnemy(EntityType<LancerDollEnemy> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, false);
        this.actionPhase = ActionPhase.WANDER;
    }
    public LancerDollEnemy(Level level, double x, double y, double z) {
        this(EntityRegistry.LANCER_DOLL_ENEMY.get(), level);
        setPos(x, y, z);
    }
    public LancerDollEnemy(Level level, BlockPos position) {
        this(level, position.getX(), position.getY(), position.getZ());
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LancerDollChargeAttackGoal(this, 1.0, 8.0f, 20, 60));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomFlyingGoal(this, 1.0f));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 6.0f));

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
        if (this.isControlledByLocalInstance() && this.actionPhase != ActionPhase.CHARGE) {
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

        this.calculateEntityAnimation(false);
    }

    @Override
    public void tick() {
        if(this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(this.actionPhase == ActionPhase.WANDER || this.actionPhase == ActionPhase.FOLLOW, this.tickCount);
            this.chargeAnimationState.animateWhen(this.actionPhase == ActionPhase.CHARGE, this.tickCount);
            this.attackAnimationState.animateWhen(this.actionPhase == ActionPhase.LUNGE, this.tickCount);
        }

        switch (this.actionPhase) {
            case WANDER -> this.playSound(SoundEvents.END_PORTAL_FRAME_FILL);
            case FOLLOW -> this.playSound(SoundEvents.AMETHYST_BLOCK_CHIME);
            case CHARGE -> this.playSound(SoundEvents.FLINTANDSTEEL_USE);
            case LUNGE -> this.playSound(SoundEvents.CAT_AMBIENT);
        }

        super.tick();
    }



    private class LancerDollChargeAttackGoal extends Goal {
        private final LancerDollEnemy mob;
        private LivingEntity target;
        private Vec3 targetPos;
        private final int cooldown;
        private int timeSinceLastAttack;
        private final double speedModifier;
        private final float attackRadius;
        private final float attackRadiusSqr;
        private final int requiredSeeTime;
        private int seeTime;
        private int chargeTime;


        public LancerDollChargeAttackGoal(LancerDollEnemy pMob, double pSpeedModifier, float pAttackRadius, int pRequiredSeeTime, int pCooldown) {
            this.mob = pMob;
            this.cooldown = pCooldown;
            this.timeSinceLastAttack = 0;
            this.speedModifier = pSpeedModifier;
            this.attackRadius = pAttackRadius;
            this.attackRadiusSqr = pAttackRadius * pAttackRadius;
            this.requiredSeeTime = pRequiredSeeTime;
            this.chargeTime = 0;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target != null && target.isAlive()) {
                this.target = target;
                this.targetPos = this.target.position();
                this.mob.actionPhase = ActionPhase.FOLLOW;
                return true;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canUse() && this.timeSinceLastAttack >= this.cooldown;
        }

        public void start() {
            this.mob.actionPhase = ActionPhase.FOLLOW;
        }

        public void stop() {
            this.target = null;
            this.seeTime = 0;
            this.timeSinceLastAttack = 0;
            this.mob.actionPhase = ActionPhase.WANDER;
            this.chargeTime = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            this.target = this.mob.getTarget();
            assert this.target != null;
            double distanceToTarget = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            boolean canSeeTarget = this.mob.getSensing().hasLineOfSight(this.target);
            if (this.mob.actionPhase != ActionPhase.CHARGE && this.mob.actionPhase != ActionPhase.LUNGE)
                this.targetPos = this.target.position();
            this.mob.getLookControl().setLookAt(this.targetPos);

            switch (this.mob.actionPhase) {
                case FOLLOW -> {
                    if (canSeeTarget) {
                        this.seeTime++;
                    } else {
                        this.seeTime = 0;
                    }
                    //Get closer if not within attack range
                    if (!(distanceToTarget > (double)this.attackRadiusSqr)) {
                        this.mob.getNavigation().stop();
                    } else {
                        this.mob.getNavigation().moveTo(this.target, this.speedModifier);
                    }
                    //If off cooldown and having looked at target for long enough, proceed to charge attack after a random interval
                    this.timeSinceLastAttack++;
                    if (this.timeSinceLastAttack >= this.cooldown && this.seeTime >= this.requiredSeeTime) {
                        if (LancerDollEnemy.this.random.nextFloat() < 0.2f || true) {
                            this.mob.actionPhase = ActionPhase.CHARGE;
                        }
                    }
                    break;
                }

                case CHARGE -> {
                    this.chargeTime++;
                    if (this.chargeTime < 60) {
                        this.targetPos = this.target.position();
                    } else if (this.chargeTime >= 80) {
                        this.mob.actionPhase = ActionPhase.LUNGE;
                    }
                    break;
                }

                case LUNGE -> {
                    this.mob.moveTo(this.targetPos);
                    this.timeSinceLastAttack = 0;
                    if (this.mob.getNavigation().isDone()) {
                        this.mob.actionPhase = ActionPhase.WANDER;
                    }
                    break;
                }
            }

//            if (LancerDollEnemy.this.actionPhase == Action) {
//                this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);



//                if (canSeeTarget) {
//                    ++this.seeTime;
//                } else {
//                    this.seeTime = 0;
//                }

//                //Get closer if not within attack range
//                if (!(distanceToTarget > (double)this.attackRadiusSqr)) {
//                    this.mob.getNavigation().stop();
//                } else {
//                    this.mob.getNavigation().moveTo(this.target, this.speedModifier);
//                }

//                //If off cooldown and having looked at target for long enough, proceed to attack after a random interval
//                this.timeSinceLastAttack++;
//                if (this.timeSinceLastAttack >= this.cooldown && this.seeTime >= this.requiredSeeTime) {
//                    if (LancerDollEnemy.this.random.nextFloat() < 0.2f) {
//                        setAttacking(true);
//                        setCharging(true);
//                    }
//                }
//            } else {
//                if (chargeTime < 60) {
//                    targetPos = this.target.position();
//                } else if (chargeTime >= 80) {
//                    this.mob.moveTo(targetPos);
//                    this.mob.playSound(SoundEvents.CHICKEN_HURT);
//                    this.timeSinceLastAttack = 0;
//                    return;
//                }
//                chargeTime++;
//            }
        }
    }


//    private class LancerDollRandomFlyingGoal extends WaterAvoidingRandomFlyingGoal {
//        public LancerDollRandomFlyingGoal(PathfinderMob pMob, double pSpeedModifier) {
//            super(pMob, pSpeedModifier);
//        }
//
//        public boolean canUse() {
//            return !LancerDollEnemy.this.getMoveControl().hasWanted() && LancerDollEnemy.this.random.nextInt(reducedTickDelay(7)) == 0;
//        }
//
//        public boolean canContinueToUse() {
//            return false;
//        }
//
//        @Override
//        public boolean isInterruptable() {
//            return true;
//        }
//
//        @Nullable
//        @Override
//        protected Vec3 getPosition() {
//            BlockPos startingPos = LancerDollEnemy.this.getBoundOrigin();
//            if (startingPos == null) {
//                startingPos = LancerDollEnemy.this.blockPosition();
//            }
//
//            for(int i = 0; i < 3; i++) {
//                BlockPos endingPos = startingPos.offset(LancerDollEnemy.this.random.nextInt(15) - 7, LancerDollEnemy.this.random.nextInt(11) - 5, LancerDollEnemy.this.random.nextInt(15) - 7);
//                if (LancerDollEnemy.this.level().isEmptyBlock(endingPos)) {
//                    LancerDollEnemy.this.getMoveControl().setWantedPosition((double)endingPos.getX() + 0.5, (double)endingPos.getY() + 0.5, (double)endingPos.getZ() + 0.5, 0.25);
//                    if (LancerDollEnemy.this.getTarget() == null) {
//                        LancerDollEnemy.this.getLookControl().setLookAt((double)endingPos.getX() + 0.5, (double)endingPos.getY() + 0.5, (double)endingPos.getZ() + 0.5, 180.0F, 20.0F);
//                    }
//                    return new Vec3(endingPos.getX(), endingPos.getY(), endingPos.getZ());
//                }
//            }
//
//            return new Vec3(startingPos.getX(), startingPos.getY(), startingPos.getZ());
//        }
//
//        public void tick() {
//
//        }
//    }


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

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ATTACKING, false);
        this.entityData.define(DATA_IS_CHARGING, false);
//        this.entityData.define(DATA_TIME_SINCE_LAST_ATTACK, 0);
    }

    public void setAttacking(boolean pAttacking) {
        this.entityData.set(DATA_IS_ATTACKING, pAttacking);
    }
    public boolean getAttacking() {
        return this.entityData.get(DATA_IS_ATTACKING);
    }

    public void setCharging(boolean pCharging) {
        this.entityData.set(DATA_IS_CHARGING, pCharging);
    }
    public boolean getCharging() {
        return this.entityData.get(DATA_IS_CHARGING);
    }

//    public void setTimeSinceLastAttack(int pTime) {
//        this.entityData.set(DATA_TIME_SINCE_LAST_ATTACK, pTime);
//    }
//    public int getTimeSinceLastAttack() {
//        return this.entityData.get(DATA_TIME_SINCE_LAST_ATTACK);
//    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }


    private enum ActionPhase {
        WANDER,
        FOLLOW,
        CHARGE,
        LUNGE;

        ActionPhase() {
        }
    }

    private ActionPhase getActionPhase() {
        return this.actionPhase;
    }

    private void setActionPhase(ActionPhase pPhase) {
        this.actionPhase = pPhase;
    }
}
