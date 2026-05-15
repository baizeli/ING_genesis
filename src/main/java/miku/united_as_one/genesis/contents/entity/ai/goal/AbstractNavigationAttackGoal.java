package miku.united_as_one.genesis.contents.entity.ai.goal;

import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public abstract class AbstractNavigationAttackGoal extends Goal {
    protected final HammerMob mob;
    protected final PathNavigation navigation;
    protected LivingEntity target;

    protected static final int ATTACK_RANGE = 3;
    protected final int cooldownTicks;

    protected int lastAttackTick;

    protected Vec3 lockedAttackDirection;

    protected AbstractNavigationAttackGoal(HammerMob mob, int cooldownTicks) {
        this.mob = mob;
        this.navigation = mob.getNavigation();
        this.cooldownTicks = cooldownTicks;
        this.lastAttackTick = -cooldownTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    protected boolean isCoolingDown() {
        return this.mob.tickCount - this.lastAttackTick < this.cooldownTicks;
    }

    protected void resetCooldown() {
        this.lastAttackTick = this.mob.tickCount;
    }

    protected void updateTarget() {
        LivingEntity currentTarget = this.mob.getTarget();
        if (currentTarget != null) {
            this.target = currentTarget;
        }
    }

    protected void lockAttackDirection() {
        if (this.target != null) {
            this.lockedAttackDirection = this.target.position()
                .subtract(this.mob.position())
                .normalize();
        }
    }

    protected Vec3 getLockedAttackDirection() {
        if (this.lockedAttackDirection != null) {
            return this.lockedAttackDirection;
        }
        return this.mob.getLookAngle();
    }

    protected void moveToTarget() {
        if (this.mob.horizontalCollision) {
            Vec3 toTarget = new Vec3(
                this.target.getX() - this.mob.getX(),
                0,
                this.target.getZ() - this.mob.getZ()
            ).normalize();
            Vec3 look = this.mob.getLookAngle();
            double cross = look.x * toTarget.z - look.z * toTarget.x;

            float strafe = cross > 0 ? 1.0F : -1.0F;
            this.mob.getMoveControl().strafe(0.3F, strafe);
        } else {
            this.mob.getMoveControl().setWantedPosition(
                this.target.getX(), this.target.getY(), this.target.getZ(), 1.0D
            );
        }
    }

    public void stop() {
        this.target = null;
        this.mob.setAttackState(HammerMob.ATTACK_IDLE);
        this.navigation.stop();
    }
}