package net.liebealua.touhouthingies.entity.ai.goal.lancerDoll;

import net.liebealua.touhouthingies.entity.dolls.LancerDoll;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class LancerDollLungeAttackGoal extends Goal {
    private final LancerDoll mob;
    private LivingEntity target;
    private final double speedModifier;
    private final float attackRadiusSqr;
    private final float lungeForce;
    private final int requiredAimTime;
    private final int cooldown;
    private double deltaX = 0, deltaY = 0, deltaZ = 0;
    private boolean hasHitTarget = false;


    public LancerDollLungeAttackGoal(LancerDoll pMob, double pSpeedModifier, float pAttackRadius, float pLungeForce, int pRequiredAimTime, int pCooldown) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        this.lungeForce = pLungeForce;
        this.requiredAimTime = pRequiredAimTime;
        this.cooldown = pCooldown;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target != null && target.isAlive()) {
            this.target = target;
            return true;
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.canUse();
    }

    public void start() {
    }

    public void stop() {
        this.mob.setAimTime(20);
        this.mob.timeSinceLastAttack = 0;
        this.mob.setAttacking(false);
        this.hasHitTarget = false;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {

        //Set target and stuff
        this.target = this.mob.getTarget();
        assert this.target != null;
        double distanceToTarget = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean isWithinAttackRange = distanceToTarget <= (double) this.attackRadiusSqr;
        boolean canSeeTarget = this.mob.getSensing().hasLineOfSight(this.target);
        boolean isOnCooldown = this.mob.timeSinceLastAttack < this.cooldown;


        if (!this.mob.getAttacking()) {

            //Look at target
            this.mob.getLookControl().setLookAt(this.target);

            //Get closer if no line of sight or not within attack range, otherwise stay still
            if (!canSeeTarget || !isWithinAttackRange) {
                this.mob.getNavigation().moveTo(this.target, this.speedModifier);
            } else {
                //Ascend if not high enough above target
                double yDistance = this.mob.getY() - this.target.position().y;
                if (yDistance < 4 && (this.mob.getAimTime() > 20)) {
                    this.mob.move(MoverType.SELF, new Vec3(0, 0.15, 0));
                }

                if (this.mob.getAimTime() > 20) {
                    this.mob.getNavigation().stop();
                }
            }

            //this.mob.setCustomName(Component.literal("" + this.mob.getAimTime()));


            //Reset aim timer if no line of sight, otherwise increase it
            if (canSeeTarget) {
                this.mob.setAimTime(this.mob.getAimTime() + 1);
            } else {
                this.mob.setAimTime(0);
            }

            //If off cooldown, within attack range and having looked at target for long enough, proceed to attack
            if (!isOnCooldown && isWithinAttackRange && this.mob.getAimTime() >= this.requiredAimTime && Math.random() < 0.2f) {
                this.mob.getNavigation().stop();
                this.mob.setAttacking(true);
                this.mob.setAimTime(0);
            }

        } else {

            //Charge
            this.mob.setAimTime(this.mob.getAimTime() + 1);

            //If early charging, keep target position updated
            if (this.mob.getAimTime() < 40) {
                deltaX = this.target.position().x - this.mob.getX();
                deltaY = this.target.position().y - this.mob.getY();
                deltaZ = this.target.position().z - this.mob.getZ();
                this.mob.setAimYRot(-((float) Mth.atan2(deltaX, deltaZ)) * 57.295776F);
                //this.mob.setXRotat(-((float)Mth.atan2(deltaY, deltaZ)) * 57.295776F);

            } else if (this.mob.getAimTime() == 60) {
                this.mob.setDeltaMovement(new Vec3(deltaX, deltaY, deltaZ).normalize().multiply(lungeForce, lungeForce, lungeForce));

            } else if (this.mob.getAimTime() > 60 && this.mob.getAimTime() < 80 && !hasHitTarget) {
                if (this.mob.getBoundingBox().inflate(0.20000000298023224).intersects(target.getBoundingBox())) {
                    this.mob.doHurtTarget(target);
                    this.hasHitTarget = true;
                }

            } else if (this.mob.getAimTime() >= 80) {
                this.mob.setAimTime(20);
                this.mob.timeSinceLastAttack = 0;
                this.mob.setAttacking(false);
                this.hasHitTarget = false;
            }


        }


//        switch (this.mob.getState()) {
//            case FOLLOW -> {
//
//
//                //If off cooldown and having looked at target for long enough, proceed to charge attack after a random interval
//                this.timeSinceLastAttack++;
//                if (this.timeSinceLastAttack >= this.cooldown && this.aimTime >= this.requiredSeeTime && distanceToTarget <= (double)this.attackRadiusSqr) {
//                    if (Math.random() < 0.2f || true) {
//                        this.mob.setState(LancerDoll.State.CHARGE);
//                    }
//                }
//                break;
//            }
//
//            case CHARGE -> {
//                this.actionTimer++;
//                if (this.actionTimer < 40) {
//                    this.targetPos = this.target.position();
//                    this.mob.getLookControl().setLookAt(this.targetPos);
//                } else if (this.actionTimer >= 60) {
//                    this.actionTimer = 0;
//                    this.mob.setState(LancerDoll.State.LUNGE);
//                    this.mob.setDeltaMovement(this.mob.getViewVector(1.0f));
//                    this.mob.setYRot(mob.getViewYRot(1));
//                }
//                break;
//            }
//
//            case LUNGE -> {
//                this.actionTimer++;
//                if (this.actionTimer >= 40) {
//                    this.timeSinceLastAttack = 0;
//                    this.actionTimer = 0;
//                    this.mob.setState(LancerDoll.State.WANDER);
//                }
//                break;
//            }
//        }
        }

//    protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
//        float $$3 = Mth.degreesDifference(pFrom, pTo);
//        float $$4 = Mth.clamp($$3, -pMaxDelta, pMaxDelta);
//        return pFrom + $$4;
//    }
}

