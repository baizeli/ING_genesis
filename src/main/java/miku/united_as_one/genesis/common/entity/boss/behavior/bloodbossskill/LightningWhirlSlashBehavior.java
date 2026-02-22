package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.common.entity.boss.SkillMovementTask;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import miku.united_as_one.genesis.init.registry.SoundRegister;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class LightningWhirlSlashBehavior
        extends AnimatedActionBehavior<BloodBoss> {


    //冷却(tick)
    public static final int COOL_DOWN = 8*20;
    //伤害倍率
    public static final float DAMAGE_MULTIPLIER = 3.5F;

    private static final int DURATION = 30;
    private static final int HIT_1 = 19;
    private static final int HIT_2 = 24;

    public static final String SKILL_ANIMATION = "lightning_whirlwind_2";//闪电旋风劈2


    private boolean hit1Done = false;
    private boolean hit2Done = false;

    public LightningWhirlSlashBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
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
        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);

        hit1Done = false;
        hit2Done = false;



        playHitSound(level, boss, 5.0F, 0.3F);

        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.addSkillMovement(createDashMovement());
        }
    }

    private static void playHitSound(ServerLevel level, BloodBoss boss, float volume, float pitch) {
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundRegister.CUTE_HIT.get(),
                SoundSource.HOSTILE,
                volume,
                pitch
        );
    }


    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);

        if (abilityTimer % 3 == 0 && abilityTimer < DURATION-10) {
            float pitch = 0.5F + (abilityTimer / (float) DURATION) * 0.5F;
            playHitSound(level, boss, 5F, pitch);
        }

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
        boss.getBrain().eraseMemory(ModMemoryModuleType.IS_CASTING_SKILL.get());
        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.clearSkillMovements();
        }
    }

    @Override protected int getActionTimestamp() { return 0; }
    @Override protected int getActionDuration() { return DURATION; }
    @Override protected int getCooldown() { return COOL_DOWN; }
    @Override protected String getAnimationId() { return SKILL_ANIMATION; }
    @Override protected void doAction(BloodBoss entity) {}



    private SkillMovementTask createDashMovement() {
        return new SkillMovementTask(DURATION) {
            private Vec3 startPos;
            private Vec3 lockedDirection;
            private Vec3 targetPos;
            private boolean hasTarget = false;
            private boolean hasPassedTarget = false;
            private static final float DASH_START_P = 13F / 30F;

            private Vec3 lastAppliedMovement = Vec3.ZERO;

            @Override
            public void start(net.minecraft.world.entity.Mob mob) {
                this.startPos = mob.position();
                LivingEntity target = mob.getTarget();

                if (target != null) {
                    this.targetPos = target.position();
                    double dist = targetPos.subtract(startPos).length();
                    if (dist < 1.0) {
                        this.lockedDirection = mob.getLookAngle().normalize();
                        this.targetDistance = 3.0;
                        this.hasTarget = false;
                    } else {
                        this.lockedDirection = targetPos.subtract(startPos).normalize();
                        this.targetDistance = dist;
                        this.hasTarget = true;
                    }
                } else {
                    this.lockedDirection = mob.getLookAngle().normalize();
                    this.hasTarget = false;
                    this.targetDistance = 10.0;
                }
                this.hasPassedTarget = false;
                this.lastAppliedMovement = Vec3.ZERO;
            }

            private double targetDistance;

            @Override
            public Vec3 compute(Mob mob, float p) {
                Vec3 counterForce = lastAppliedMovement.scale(-1.0);

                if (hasPassedTarget) {
                    Vec3 currentVel = mob.getDeltaMovement();
                    lastAppliedMovement = Vec3.ZERO;
                    return new Vec3(-currentVel.x, 0, -currentVel.z);
                }

                if (p < DASH_START_P) {
                    lastAppliedMovement = Vec3.ZERO;
                    return Vec3.ZERO;
                }

                if (hasTarget) {
                    Vec3 currentOffset = mob.position().subtract(startPos);
                    double currentProjection = currentOffset.dot(lockedDirection);

                    if (currentProjection > (targetDistance + 0.5) && p > (DASH_START_P + 0.05F)) {
                        hasPassedTarget = true;
                        Vec3 currentVel = mob.getDeltaMovement();
                        lastAppliedMovement = Vec3.ZERO;
                        return new Vec3(-currentVel.x, 0, -currentVel.z);
                    }
                }

                float dashP = (p - DASH_START_P) / (1.0F - DASH_START_P);
                dashP = Mth.clamp(dashP, 0.0F, 1.0F);

                double speed = 0.65 * (1.0 - dashP);
                Vec3 desiredPush = lockedDirection.scale(speed);

                Vec3 result = counterForce.add(desiredPush);
                lastAppliedMovement = desiredPush;

                return result;
            }

            @Override
            public void end(net.minecraft.world.entity.Mob mob) {
                Vec3 currentVel = mob.getDeltaMovement();
                mob.setDeltaMovement(0, currentVel.y, 0);
            }

            @Override
            public boolean finished() {
                return hasPassedTarget || super.finished();
            }
        };
    }


    

    private void dealAreaDamage(BloodBoss boss) {
        ServerLevel level = (ServerLevel) boss.level();

        double radius = 4;
        AABB box = boss.getBoundingBox().inflate(radius);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity target : targets) {
            applyDamageToTarget(boss, target);
        }
    }

    /**
     * 计算并应用伤害到单个目标
     * 
     * @param boss 攻击者
     * @param target 受害者
     */
    private void applyDamageToTarget(BloodBoss boss, LivingEntity target) {

        boss.applySkillDamage(target, DAMAGE_MULTIPLIER);
    }
}
