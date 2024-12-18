//package net.liebealua.touhouthingies.entity.ai.goal;
//
//import net.liebealua.touhouthingies.entity.dolls.LancerDoll;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.goal.Goal;
//import net.minecraft.world.phys.Vec3;
//
//import java.util.EnumSet;
//
//public class LancerDollChargeAttackGoal extends Goal {
//    private final LancerDoll mob;
//    private LivingEntity target;
//    private Vec3 targetPos;
//    private final int cooldown;
//    private int timeSinceLastAttack;
//    private final double speedModifier;
//    private final float attackRadius;
//    private final float attackRadiusSqr;
//    private final int requiredSeeTime;
//    private int seeTime;
//    private int actionTimer;
//
//
//    public LancerDollChargeAttackGoal(LancerDoll pMob, double pSpeedModifier, float pAttackRadius, int pRequiredSeeTime, int pCooldown) {
//        this.mob = pMob;
//        this.cooldown = pCooldown;
//        this.timeSinceLastAttack = 0;
//        this.speedModifier = pSpeedModifier;
//        this.attackRadius = pAttackRadius;
//        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
//        this.requiredSeeTime = pRequiredSeeTime;
//        this.actionTimer = 0;
//        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
//    }
//
//    public boolean canUse() {
//        LivingEntity target = this.mob.getTarget();
//        if (target != null && target.isAlive()) {
//            this.target = target;
//            if (this.mob.getState() == LancerDoll.State.WANDER)
//                this.mob.setState(LancerDoll.State.FOLLOW);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean canContinueToUse() {
//        return this.canUse();
//    }
//
//    public void start() {
//    }
//
//    public void stop() {
//        this.seeTime = 0;
//        this.actionTimer = 0;
//        this.timeSinceLastAttack = 0;
//        this.mob.setTarget(null);
//        this.mob.setState(LancerDoll.State.WANDER);
//        this.mob.randomFlyingGoal.trigger();
//    }
//
//    public boolean requiresUpdateEveryTick() {
//        return true;
//    }
//
//    public void tick() {
//
//        this.target = this.mob.getTarget();
//        assert this.target != null;
//        double distanceToTarget = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
//        boolean canSeeTarget = this.mob.getSensing().hasLineOfSight(this.target);
//        if (this.mob.getState() != LancerDoll.State.CHARGE && this.mob.getState() != LancerDoll.State.LUNGE && this.targetPos != null) {
//            this.targetPos = this.target.position();
//            this.mob.getLookControl().setLookAt(targetPos);
//        }
//
//        switch (this.mob.getState()) {
//            case FOLLOW -> {
//                if (canSeeTarget) {
//                    this.seeTime++;
//                } else {
//                    this.seeTime = 0;
//                }
//                //Get closer if not within attack range
//                if (distanceToTarget <= (double)this.attackRadiusSqr) {
//                    this.mob.getNavigation().stop();
//                } else {
//                    this.mob.getNavigation().moveTo(this.target, this.speedModifier);
//                }
//                //If off cooldown and having looked at target for long enough, proceed to charge attack after a random interval
//                this.timeSinceLastAttack++;
//                if (this.timeSinceLastAttack >= this.cooldown && this.seeTime >= this.requiredSeeTime && distanceToTarget <= (double)this.attackRadiusSqr) {
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
//    }
//}
