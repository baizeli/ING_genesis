package miku.united_as_one.genesis.contents.entity.ai.goal;

import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;

import java.util.EnumSet;

public class SprintAttackGoal extends Goal {
    private final HammerMob mob;
    private LivingEntity target;

    private static final int CHARGE_TICKS = 15;
    private static final int SPRINT_DURATION = 15;
    private static final int COOLDOWN_TICKS = /*20*30*/20;
    private static final double SPRINT_DISTANCE = 15.0D;
    private static final double DAMAGE_RADIUS = 2.0D;
    private static final double TRIGGER_RANGE = 8.0D;

    private int cooldown;
    private int sprintTick;
    private Vec3 sprintDirection;

    public SprintAttackGoal(HammerMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (this.cooldown > 0) {
            --this.cooldown;
            this.mob.setSprintAttackCooling(true);
            return false;
        }

        this.mob.setSprintAttackCooling(false);

        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            if (this.mob.distanceTo(livingentity) > TRIGGER_RANGE) {
                this.target = livingentity;
                return true;
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.sprintTick < CHARGE_TICKS + SPRINT_DURATION;
    }

    public void start() {
        this.sprintTick = 0;
        this.mob.setAttackState(HammerMob.ATTACK_SPRINT);
        this.sprintDirection = this.target.position().subtract(this.mob.position()).normalize();

        float targetYRot = (float) (Mth.atan2(this.sprintDirection.z, this.sprintDirection.x) * 180.0D / Math.PI) - 90.0F;
        this.mob.forceLookAt(targetYRot);
    }

    public void tick() {
        if (this.target == null) return;

        float targetYRot = (float) (Mth.atan2(this.sprintDirection.z, this.sprintDirection.x) * 180.0D / Math.PI) - 90.0F;
        this.mob.setYRot(targetYRot);
        this.mob.setXRot(0.0F);
        this.mob.yRotO = targetYRot;
        this.mob.xRotO = 0.0F;

        if (this.sprintTick < CHARGE_TICKS) {
            this.sprintTick++;
            return;
        }

        if (this.sprintTick == CHARGE_TICKS) {
            this.mob.setSprinting(true);
        }

        double speed = SPRINT_DISTANCE / SPRINT_DURATION;
        this.mob.setDeltaMovement(this.sprintDirection.x * speed, 0, this.sprintDirection.z * speed);

        AABB attackBox = new AABB(
            this.mob.getX() - DAMAGE_RADIUS,
            this.mob.getY(),
            this.mob.getZ() - DAMAGE_RADIUS,
            this.mob.getX() + DAMAGE_RADIUS,
            this.mob.getY() + 2.0D,
            this.mob.getZ() + DAMAGE_RADIUS
        );

        for (LivingEntity entity : this.mob.level().getEntitiesOfClass(LivingEntity.class, attackBox)) {
            if (entity != this.mob) {
                double attackDamage = this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2;
                entity.hurt(this.mob.level().damageSources().mobAttack(this.mob), (float) attackDamage);
                
                Vec3 knockbackDir = entity.position().subtract(this.mob.position()).normalize();
                entity.push(knockbackDir.x * 5.0D, 0.5D, knockbackDir.z * 5.0D);
            }
        }

        this.sprintTick++;

        if (this.sprintTick >= CHARGE_TICKS + SPRINT_DURATION) {
            this.mob.setSprinting(false);
            this.mob.setAnimBufferTick(40);
            this.cooldown = COOLDOWN_TICKS;
        }
    }

    public void stop() {
        this.target = null;
        this.mob.setSprinting(false);
        this.mob.setAttackState(HammerMob.ATTACK_IDLE);
    }
}