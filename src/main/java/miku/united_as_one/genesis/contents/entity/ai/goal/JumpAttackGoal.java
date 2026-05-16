package miku.united_as_one.genesis.contents.entity.ai.goal;

import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class JumpAttackGoal extends AbstractNavigationAttackGoal {
    private static final int COOLDOWN_TICKS = 0;
    private static final int CHARGE_TICK = 14;
    private static final int DAMAGE_TICK = 41;
    private static final int TOTAL_DURATION = 55;
    private static final double DAMAGE_RADIUS = 10.0D;
    private static final double DAMAGE_MULTIPLIER = 1.5D;

    private boolean damageDone;

    public JumpAttackGoal(HammerMob mob) {
        super(mob, COOLDOWN_TICKS);
    }

    public boolean canUse() {
        if (this.mob.getSelectedAttack() != HammerMob.ATTACK_JUMP) return false;
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
        if (this.mob.getAttackState() == HammerMob.ATTACK_JUMP) {
            return this.mob.getAttackTick() < TOTAL_DURATION;
        }

        return false;
    }

    public void tick() {
        this.updateTarget();

        if (this.target == null && this.mob.getAttackState() == HammerMob.ATTACK_IDLE) return;

        if (this.mob.getAttackState() == HammerMob.ATTACK_IDLE) {
            if (this.mob.distanceTo(this.target) > ATTACK_RANGE) {
                this.moveToTarget();
            } else {
                this.navigation.stop();
                this.lockAttackDirection();
                this.mob.setAttackState(HammerMob.ATTACK_JUMP);
            }
        } else if (this.mob.getAttackState() == HammerMob.ATTACK_JUMP) {
            Vec3 attackDir = this.getLockedAttackDirection();
            this.forceLookAtDirection(attackDir);

            int tick = this.mob.getAttackTick();

            if (tick < CHARGE_TICK) {
                this.navigation.stop();
            }

            if (tick == CHARGE_TICK) {
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 1.2D, 0.0D));
            }

            if (tick >= CHARGE_TICK && tick < DAMAGE_TICK) {
                this.mob.fallDistance = 0.0F;
            }

            if (tick >= DAMAGE_TICK && !this.damageDone) {
                this.damageDone = true;

                Vec3 centerPos = this.mob.position().add(attackDir.scale(2.0D));

                AABB damageBox = new AABB(
                    centerPos.x - DAMAGE_RADIUS,
                    centerPos.y - 2.0D,
                    centerPos.z - DAMAGE_RADIUS,
                    centerPos.x + DAMAGE_RADIUS,
                    centerPos.y + 4.0D,
                    centerPos.z + DAMAGE_RADIUS
                );

                for (LivingEntity entity : this.mob.level().getEntitiesOfClass(LivingEntity.class, damageBox)) {
                    if (entity != this.mob) {
                        double attackDamage = this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * DAMAGE_MULTIPLIER;
                        entity.hurt(this.mob.level().damageSources().mobAttack(this.mob), (float) attackDamage);
                    }
                }
            }

            if (tick >= TOTAL_DURATION) {
                this.resetCooldown();
                this.mob.setAnimBufferTick(20);
                this.mob.setAttackState(HammerMob.ATTACK_IDLE);
            }
        }
    }
}