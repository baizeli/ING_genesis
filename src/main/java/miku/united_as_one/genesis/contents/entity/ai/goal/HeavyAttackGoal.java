package miku.united_as_one.genesis.contents.entity.ai.goal;

import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.world.entity.LivingEntity;
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
    private static final int COOLDOWN_TICKS = /*20*5*/20;
    private static final double DAMAGE_RADIUS = 2.5;

    private int cooldown;

    public HeavyAttackGoal(HammerMob mob) {
        this.mob = mob;
        this.navigation = mob.getNavigation();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        LivingEntity currentTarget = this.mob.getTarget();
        if (currentTarget == null || !currentTarget.isAlive()) {
            return false;
        }
        if (this.mob.getAttackState() != HammerMob.ATTACK_IDLE) {
            return true;
        }
        return true;
    }

    public void tick() {
        LivingEntity currentTarget = this.mob.getTarget();
        if (currentTarget == null) {
            return;
        }
        this.target = currentTarget;

        this.mob.lookAt(this.target, 30.0F, 30.0F);

        if (this.cooldown > 0) {
            --this.cooldown;
        }

        if (this.mob.getAttackState() == HammerMob.ATTACK_IDLE) {
            double distance = this.mob.distanceTo(this.target);

            if (distance > ATTACK_RANGE) {
                this.navigation.moveTo(this.target, 1.0D);
            } else if (this.cooldown <= 0) {
                this.navigation.stop();
                this.mob.setAttackState(HammerMob.ATTACK_HEAVY);
            }
        } else {
            if (this.mob.getAttackTick() >= DAMAGE_TICK) {
                AABB attackBox = new AABB(
                    this.mob.getX() - DAMAGE_RADIUS,
                    this.mob.getY(),
                    this.mob.getZ() - DAMAGE_RADIUS,
                    this.mob.getX() + DAMAGE_RADIUS,
                    this.mob.getY() + DAMAGE_RADIUS,
                    this.mob.getZ() + DAMAGE_RADIUS
                );

                for (LivingEntity entity : this.mob.level().getEntitiesOfClass(LivingEntity.class, attackBox)) {
                    if (entity != this.mob) {
                        entity.hurt(this.mob.level().damageSources().mobAttack(this.mob), 40);
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
        this.navigation.stop();
    }
}