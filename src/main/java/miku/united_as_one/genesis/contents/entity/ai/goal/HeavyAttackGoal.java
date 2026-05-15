package miku.united_as_one.genesis.contents.entity.ai.goal;

import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HeavyAttackGoal extends AbstractNavigationAttackGoal {
    private static final int COOLDOWN_TICKS = /*20*5*/20;
    private static final int DAMAGE_TICK = 26;
    private static final int TOTAL_DURATION = 41;
    private static final double DAMAGE_RADIUS = 2.5;
    private static final double HAMMER_DISTANCE = 4D;
    private static final float ATTACK_DAMAGE = 40;

    private boolean damageDone;

    public HeavyAttackGoal(HammerMob mob) {
        super(mob, COOLDOWN_TICKS);
    }

    public boolean canUse() {
        if (this.mob.getSelectedAttack() != HammerMob.ATTACK_HEAVY) return false;
        if (this.isCoolingDown()) return false;

        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            this.damageDone = false;
            return true;
        }

        return false;
    }

    public boolean canContinueToUse() {
        LivingEntity currentTarget = this.mob.getTarget();
        if (currentTarget == null || !currentTarget.isAlive()) return false;

        if (this.mob.getAttackState() == HammerMob.ATTACK_HEAVY) {
            return this.mob.getAttackTick() < TOTAL_DURATION;
        }

        return false;
    }

    public void tick() {
        this.updateTarget();

        if (this.target == null) return;

        if (this.mob.getAttackState() == HammerMob.ATTACK_IDLE) {
            if (this.mob.distanceTo(this.target) > ATTACK_RANGE) {
                this.moveToTarget();
            } else {
                this.lockAttackDirection();
                this.mob.setAttackState(HammerMob.ATTACK_HEAVY);
            }
        } else if (this.mob.getAttackState() == HammerMob.ATTACK_HEAVY) {
            Vec3 attackDir = this.getLockedAttackDirection();
            this.forceLookAtDirection(attackDir);

            if (this.mob.getAttackTick() >= DAMAGE_TICK && !this.damageDone) {
                this.damageDone = true;

                Vec3 hammerPos = this.mob.position().add(attackDir.scale(HAMMER_DISTANCE));

                AABB attackBox = new AABB(
                    hammerPos.x - DAMAGE_RADIUS,
                    hammerPos.y,
                    hammerPos.z - DAMAGE_RADIUS,
                    hammerPos.x + DAMAGE_RADIUS,
                    hammerPos.y + DAMAGE_RADIUS,
                    hammerPos.z + DAMAGE_RADIUS
                );

                for (LivingEntity entity : this.mob.level().getEntitiesOfClass(LivingEntity.class, attackBox)) {
                    if (entity != this.mob) {
                        entity.hurt(this.mob.level().damageSources().mobAttack(this.mob), ATTACK_DAMAGE);
                    }
                }
            }

            if (this.mob.getAttackTick() >= TOTAL_DURATION) {
                this.resetCooldown();
                this.mob.setAnimBufferTick(20);
                this.mob.setAttackState(HammerMob.ATTACK_IDLE);
            }
        }
    }
}