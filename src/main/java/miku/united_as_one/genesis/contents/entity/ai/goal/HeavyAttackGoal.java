package miku.united_as_one.genesis.contents.entity.ai.goal;

import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.AABB;
import java.util.EnumSet;

public class HeavyAttackGoal extends Goal {
    private final HammerMob mob;
    private final PathNavigation navigation;
    private LivingEntity target;

    private static final int ATTACK_RANGE = 3;
    private static final int DAMAGE_TICK = 26;
    private static final int COOLDOWN_TICKS = 20*5;
    private static final double DAMAGE_RADIUS = 5;

    private int cooldown;
    private boolean attackDone;

    public HeavyAttackGoal(HammerMob mob) {
        this.mob = mob;
        this.navigation = mob.getNavigation();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (this.cooldown > 0) {
            --this.cooldown;
            return false;
        }

        if (!this.mob.isSprintAttackCooling()) return false;
        
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            if (this.mob.distanceTo(livingentity) <= ATTACK_RANGE) {
                this.target = livingentity;
                this.attackDone = false;
                return true;
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        if (this.target == null || !this.target.isAlive()) return false;
        if (this.mob.getAttackState() != HammerMob.ATTACK_IDLE) return true;
        return !this.attackDone;
    }

    public void tick() {
        if (this.target == null) return;

        this.mob.lookAt(this.target, 30.0F, 30.0F);

        if (this.mob.getAttackState() == HammerMob.ATTACK_IDLE) {
            double distance = this.mob.distanceTo(this.target);

            if (distance > ATTACK_RANGE) {
                this.navigation.moveTo(this.target, 1.0D);
            } else {
                this.navigation.stop();
                this.mob.setAttackState(HammerMob.ATTACK_HEAVY);
            }
        } else {
            this.navigation.stop();

            if (this.mob.getAttackTick() >= DAMAGE_TICK) {
                this.attackDone = true;
                double radius = DAMAGE_RADIUS;
                AABB attackBox = new AABB(
                    this.mob.getX() - radius,
                    this.mob.getY(),
                    this.mob.getZ() - radius,
                    this.mob.getX() + radius,
                    this.mob.getY() + radius,
                    this.mob.getZ() + radius
                );

                for (LivingEntity entity : this.mob.level().getEntitiesOfClass(LivingEntity.class, attackBox)) {
                    if (entity != this.mob) {
                        double attackDamage = this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        entity.hurt(this.mob.level().damageSources().mobAttack(this.mob), (float) attackDamage);
                    }
                }

                this.mob.setAnimBufferTick(20);
                this.mob.setAttackState(HammerMob.ATTACK_IDLE);
                this.cooldown = COOLDOWN_TICKS;
            }
        }
    }

    public void stop() {
        this.target = null;
        this.mob.setAttackState(HammerMob.ATTACK_IDLE);
    }
}