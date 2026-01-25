package miku.united_as_one.genesis.entity.boss.behavior;

import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.entity.boss.SkillMovementTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class LightningWhirlSlashBehavior
        extends AnimatedActionBehavior<BloodBoss> {

    private static final int DURATION = 30;
    private static final int HIT_1 = 16;
    private static final int HIT_2 = 21;

    private boolean hit1Done = false;
    private boolean hit2Done = false;

    public LightningWhirlSlashBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        LivingEntity target = entity.getTarget();
        return target != null && entity.distanceTo(target) <= 16F;
    }

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);

        hit1Done = false;
        hit2Done = false;

        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.addSkillMovement(createDashMovement());
        }
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);

        if (abilityTimer >= HIT_1 && !hit1Done) {
            hit1Done = true;
            dealAreaDamage(boss);
        }

        if (abilityTimer >= HIT_2 && !hit2Done) {
            hit2Done = true;
            dealAreaDamage(boss);
        }
    }

    @Override
    protected void stop(ServerLevel level, BloodBoss boss, long gameTime) {
        super.stop(level, boss, gameTime);

        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.clearSkillMovements();
        }
    }

    @Override protected int getActionTimestamp() { return 0; }
    @Override protected int getActionDuration() { return DURATION; }
    @Override protected int getCooldown() { return 60; }
    @Override protected String getAnimationId() { return "闪电旋风劈2"; }
    @Override protected void doAction(BloodBoss entity) {}



    private SkillMovementTask createDashMovement() {
        return new SkillMovementTask(DURATION) {

            private Vec3 lockedDirection;
            private static final float DASH_START_P = 13F / 30F;

            @Override
            public void start(net.minecraft.world.entity.Mob mob) {
                LivingEntity target = mob.getTarget();
                if (target != null) {

                    lockedDirection = target.position()
                            .subtract(mob.position())
                            .normalize();
                } else {

                    lockedDirection = mob.getLookAngle().normalize();
                }
            }

            @Override
            public Vec3 compute(net.minecraft.world.entity.Mob mob, float p) {

                if (p < DASH_START_P) {
                    return Vec3.ZERO;
                }

                float dashP = (p - DASH_START_P) / (1.0F - DASH_START_P);
                dashP = Mth.clamp(dashP, 0.0F, 1.0F);

                double speed = 0.45 * (1.0 - dashP);
                return lockedDirection.scale(speed);
            }
        };
    }


    

    private void dealAreaDamage(BloodBoss boss) {
        ServerLevel level = (ServerLevel) boss.level();

        double radius = 4.5;
        AABB box = boss.getBoundingBox().inflate(radius);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity target : targets) {
            target.invulnerableTime = 0;
            target.hurt(
                    boss.damageSources().mobAttack(boss),
                    (float) boss.getAttributeValue(
                            net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE
                    ) * 1.2F
            );
        }
    }
}
