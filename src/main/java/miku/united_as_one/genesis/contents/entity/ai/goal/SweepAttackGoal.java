package miku.united_as_one.genesis.contents.entity.ai.goal;

import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SweepAttackGoal extends AbstractNavigationAttackGoal {
    private static final int SWEEP_TICK_1 = 12;
    private static final int SWEEP_TICK_2 = 24;
    private static final int TOTAL_DURATION = 61;
    private static final double SWEEP_1_LENGTH = 2D;
    private static final double SWEEP_1_WIDTH = 4D;
    private static final double SWEEP_2_DISTANCE = 4D;
    private static final double SWEEP_2_RADIUS = 2.5D;
    private static final double DAMAGE_1 = 0.75;
    private static final double DAMAGE_2 = 1.2;

    private boolean damage1Done;
    private boolean damage2Done;
    private boolean targetDead;

    private static final int COOLDOWN_TICKS = 20*5;

    public SweepAttackGoal(HammerMob mob) {
        super(mob, COOLDOWN_TICKS);
    }

    public boolean canUse() {
        if (this.mob.getSelectedAttack() != HammerMob.ATTACK_SWEEP) return false;
        if (this.isCoolingDown()) return false;

        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            this.damage1Done = false;
            this.damage2Done = false;
            this.targetDead = false;
            return true;
        }

        return false;
    }

    public boolean canContinueToUse() {
        LivingEntity currentTarget = this.mob.getTarget();

        if (currentTarget == null || !currentTarget.isAlive()) return false;

        if (this.mob.getAttackState() == HammerMob.ATTACK_SWEEP) {
            return this.mob.getAttackTick() < TOTAL_DURATION;
        }

        return false;
    }

    public void tick() {
        this.updateTarget();

        if (this.target == null) return;

        if (!this.targetDead && !this.target.isAlive()) {
            this.targetDead = true;
        }

        if (this.mob.getAttackState() == HammerMob.ATTACK_IDLE) {
            if (this.mob.distanceTo(this.target) > ATTACK_RANGE) {
                this.moveToTarget();
            } else {
                this.navigation.stop();
                this.lockAttackDirection();
                this.mob.setAttackState(HammerMob.ATTACK_SWEEP);
            }
        } else if (this.mob.getAttackState() == HammerMob.ATTACK_SWEEP) {
            Vec3 attackDir = this.getLockedAttackDirection();
            this.forceLookAtDirection(attackDir);
            if (this.mob.getAttackTick() >= SWEEP_TICK_1 && !this.damage1Done && !this.targetDead) {
                this.damage1Done = true;
                this.performSweep1Damage();
                this.target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            }

            if (this.mob.getAttackTick() >= SWEEP_TICK_2 && !this.damage2Done && !this.targetDead) {
                this.damage2Done = true;
                this.performSweep2Damage();
            }

            if (this.mob.getAttackTick() >= TOTAL_DURATION) {
                this.resetCooldown();
                this.mob.setAnimBufferTick(20);
                this.mob.setAttackState(HammerMob.ATTACK_IDLE);
            }
        }
    }

    private void performSweep1Damage() {
        Vec3 attackDir = this.getLockedAttackDirection();
        Vec3 startPos = this.mob.position();
        Vec3 endPos = startPos.add(attackDir.scale(SWEEP_1_LENGTH));
        double halfWidth = SWEEP_1_WIDTH / 2.0D;

        AABB attackBox;
        if (Math.abs(attackDir.x) > Math.abs(attackDir.z)) {
            attackBox = new AABB(
                Math.min(startPos.x, endPos.x),
                this.mob.getY(),
                Math.min(startPos.z, endPos.z) - halfWidth,
                Math.max(startPos.x, endPos.x),
                this.mob.getY() + 2.0D,
                Math.max(startPos.z, endPos.z) + halfWidth
            );
        } else {
            attackBox = new AABB(
                Math.min(startPos.x, endPos.x) - halfWidth,
                this.mob.getY(),
                Math.min(startPos.z, endPos.z),
                Math.max(startPos.x, endPos.x) + halfWidth,
                this.mob.getY() + 2.0D,
                Math.max(startPos.z, endPos.z)
            );
        }

        for (LivingEntity entity : this.mob.level().getEntitiesOfClass(LivingEntity.class, attackBox)) {
            if (entity != this.mob) {
                double attackDamage = this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * DAMAGE_1;
                entity.hurt(this.mob.level().damageSources().mobAttack(this.mob), (float) attackDamage);
            }
        }
    }

    private void performSweep2Damage() {
        Vec3 attackDir = this.getLockedAttackDirection();
        Vec3 centerPos = this.mob.position().add(attackDir.scale(SWEEP_2_DISTANCE));

        AABB attackBox = new AABB(
            centerPos.x - SWEEP_2_RADIUS,
            centerPos.y,
            centerPos.z - SWEEP_2_RADIUS,
            centerPos.x + SWEEP_2_RADIUS,
            centerPos.y + SWEEP_2_RADIUS,
            centerPos.z + SWEEP_2_RADIUS
        );

        for (LivingEntity entity : this.mob.level().getEntitiesOfClass(LivingEntity.class, attackBox)) {
            if (entity != this.mob) {
                double attackDamage = this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * DAMAGE_2;
                entity.hurt(this.mob.level().damageSources().mobAttack(this.mob), (float) attackDamage);
            }
        }
    }

    public void stop() {
        this.targetDead = false;
        super.stop();
    }
}