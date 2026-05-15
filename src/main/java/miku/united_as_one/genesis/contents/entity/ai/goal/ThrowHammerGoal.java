package miku.united_as_one.genesis.contents.entity.ai.goal;

import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class ThrowHammerGoal extends AbstractNavigationAttackGoal {
    private static final int COOLDOWN_TICKS = /*20*5*/20;
    private static final int THROW_TICK = 11;
    private static final int RETURN_TICK = 24;
    private static final int TOTAL_DURATION = 35;
    private static final double MAX_DISTANCE = 8.0D;
    private static final double DAMAGE_RADIUS = 1.5D;
    private static final double DAMAGE_MULTIPLIER = 1.0D;

    private final Set<Integer> hitEntities = new HashSet<>();

    public ThrowHammerGoal(HammerMob mob) {
        super(mob, COOLDOWN_TICKS);
    }

    public boolean canUse() {
        if (this.mob.getSelectedAttack() != HammerMob.ATTACK_THROW) return false;
        if (this.isCoolingDown()) return false;

        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            this.hitEntities.clear();
            return true;
        }

        return false;
    }

    public boolean canContinueToUse() {
        LivingEntity currentTarget = this.mob.getTarget();
        if (currentTarget == null || !currentTarget.isAlive()) return false;

        if (this.mob.getAttackState() == HammerMob.ATTACK_THROW) {
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
                this.navigation.stop();
                this.lockAttackDirection();
                this.mob.setAttackState(HammerMob.ATTACK_THROW);
            }
        } else if (this.mob.getAttackState() == HammerMob.ATTACK_THROW) {
            Vec3 attackDir = this.getLockedAttackDirection();
            this.forceLookAtDirection(attackDir);

            int tick = this.mob.getAttackTick();

            if (tick >= THROW_TICK && tick <= RETURN_TICK) {
                double distance;
                if (tick <= 19) {
                    distance = (tick - THROW_TICK) / 8.0D * MAX_DISTANCE;
                } else {
                    distance = (RETURN_TICK - tick) / 5.0D * MAX_DISTANCE;
                }

                Vec3 hammerPos = this.mob.position().add(attackDir.scale(distance));

                AABB damageBox = new AABB(
                    hammerPos.x - DAMAGE_RADIUS,
                    hammerPos.y - DAMAGE_RADIUS,
                    hammerPos.z - DAMAGE_RADIUS,
                    hammerPos.x + DAMAGE_RADIUS,
                    hammerPos.y + DAMAGE_RADIUS,
                    hammerPos.z + DAMAGE_RADIUS
                );

                for (LivingEntity entity : this.mob.level().getEntitiesOfClass(LivingEntity.class, damageBox)) {
                    if (entity != this.mob && this.hitEntities.add(entity.getId())) {
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