package miku.united_as_one.genesis.entity.boss.behavior;

import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.entity.boss.SkillMovementTask;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class BloodBossGrabBehavior extends AnimatedActionBehavior<BloodBoss> {

    private static final String ANIM_START = "登！";
    private static final String ANIM_SLAM  = "龙！";
    public static final String ANIM_FAIL = "寄！";

    private static final int DASH_DURATION = 9;
    private static final int IMPACT_TIME   = 20;

    private static final int DURATION_SUCCESS = 49;
    private static final int DURATION_FAIL    = 34;

    private static final int DASH_WINDUP = 8; 

    private static final int DASH_START_TICK = DASH_WINDUP;
    private static final int DASH_END_TICK   = DASH_WINDUP + DASH_DURATION;
    public static final int Cooldown = 80;

    private LivingEntity grabbedTarget;
    private LivingEntity impactTarget;

    private boolean grabSuccess;
    private boolean impactDealt;

    private int slamTimer;
    private float slamYaw;
    private boolean grabFailed;


    private float slamAngle;



    public BloodBossGrabBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    /**
     * 计算并应用主要目标受到的伤害
     * 
     * @param boss 攻击者（血 Boss）
     * @param target 受害者（主要目标）
     * @param baseDamage 基础伤害值
     */
    private void applyMainTargetDamage(BloodBoss boss, LivingEntity target, float baseDamage) {
        // 此方法已被弃用，请直接使用 boss.applyMainTargetDamage 方法
        boss.applyMainTargetDamage(target, baseDamage, 2.5f);
    }

    /**
     * 计算并应用范围伤害效果
     * 
     * @param level 游戏世界
     * @param boss 攻击者（血 Boss）
     * @param baseDamage 基础伤害值
     */
    private void applyAreaOfEffectDamage(ServerLevel level, BloodBoss boss, float baseDamage) {
        float aoeDamage = baseDamage * 0.75f;
        AABB box = boss.getBoundingBox().inflate(5.5, 2.5, 5.5);
        java.util.List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box,
                ent -> ent != boss && ent.isAlive());
        
        // 使用 Boss 类中的通用方法
        boss.applyAreaOfEffectDamage(level, targets, baseDamage, 0.75f);
    }

    @Override
    protected boolean canStartAction(BloodBoss boss) {
        return boss.getTarget() != null;
    }

    @Override
    protected int getActionTimestamp() {
        return DASH_START_TICK;
    }



    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        grabFailed = false;
        grabSuccess = false;
        impactDealt = false;
        grabbedTarget = null;
        impactTarget = null;
        slamTimer = 0;

        super.start(level, boss, gameTime);
        boss.playSound(SoundEvents.WANDERING_TRADER_HURT, 0.7f, 0.8f);
        LivingEntity target = boss.getTarget();
        if (target != null) {
            Vec3 dir = target.position().subtract(boss.position());
            float yaw = (float)(Mth.atan2(dir.z, dir.x) * Mth.RAD_TO_DEG) - 90F;
            boss.setYRot(yaw);
            boss.yBodyRot = yaw;
            boss.yHeadRot = yaw;
        }

        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);

        
        boss.serverTriggerAnimation(ANIM_START);
    }


    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        abilityTimer++;

        boss.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        boss.getNavigation().stop();

        
        if (abilityTimer < DASH_START_TICK) {
            
            boss.setDeltaMovement(Vec3.ZERO);
            return;
        }

        
        if (abilityTimer == DASH_START_TICK) {
            ((BloodBossMoveControl) boss.getMoveControl())
                    .addSkillMovement(new GrabDashTask(DASH_DURATION));
        }

        
        if (!grabSuccess && abilityTimer <= DASH_END_TICK) {
            attemptGrabDuringDash(boss);
        }

        
        else if (!grabSuccess && !grabFailed && abilityTimer > DASH_END_TICK) {
            grabFailed = true;
            boss.serverTriggerAnimation(ANIM_FAIL);
        }

        
        if (grabSuccess) {
            handleSlamSequence(level, boss);
        }
    }




    private void attemptGrabDuringDash(BloodBoss boss) {
        Vec3 vel = boss.getDeltaMovement();
        if (vel.lengthSqr() < 1.0E-4) return;

        Vec3 dir = vel.normalize();

        float yawRad = boss.getYRot() * Mth.DEG_TO_RAD;
        Vec3 right = new Vec3(Mth.cos(yawRad), 0, -Mth.sin(yawRad));

        
        Vec3 center = boss.position()
                .add(dir.scale(1.2))
                .add(right.scale(0.5));

        AABB box = AABB.ofSize(center, 3, 3, 3);

        List<LivingEntity> list = boss.level().getEntitiesOfClass(
                LivingEntity.class, box,
                e -> e != boss && e.isAlive() && !e.isSpectator()
        );

        if (!list.isEmpty()) {
            grabbedTarget = list.get(0);
            impactTarget  = grabbedTarget;
            grabSuccess   = true;
            slamTimer     = 0;
            slamYaw       = boss.getYRot();

            slamAngle = 0F;


            ((BloodBossMoveControl) boss.getMoveControl()).clearSkillMovements();
            boss.setDeltaMovement(Vec3.ZERO);
            boss.setNoGravity(true);

            boss.serverTriggerAnimation(ANIM_SLAM);
            boss.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.5f, 0.5f);
        }
    }


    private void handleSlamSequence(ServerLevel level, BloodBoss boss) {
        slamTimer++;

        
        if (slamTimer < 8) {
            boss.setDeltaMovement(0, 0.8, 0);
        } else if (slamTimer < 14) {
            boss.setDeltaMovement(0, -0.05, 0);
        } else if (slamTimer < IMPACT_TIME) {
            boss.setDeltaMovement(0, -2.0, 0);
        }

        float t = slamTimer / (float) IMPACT_TIME;

        
        float angularSpeed = Mth.lerp(t, 0.35F, 0.05F);

        
        slamAngle += angularSpeed;

        
        float theta = -(slamAngle / (slamAngle + angularSpeed * (IMPACT_TIME - slamTimer)))
                * Mth.TWO_PI;

        float yawRad = slamYaw * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));
        Vec3 right   = new Vec3(Mth.cos(yawRad), 0, -Mth.sin(yawRad));

        Vec3 rotateCenter = boss.getBoundingBox().getCenter().add(0, -1.0, 0);
        double radius = 3.0;

        if (grabbedTarget != null && grabbedTarget.isAlive()) {

            Vec3 offset =
                    forward.scale(Mth.cos(theta) * radius)
                            .add(right.scale(Mth.sin(theta) * radius))
                            .add(right.scale(0.5))
                            .add(0, Mth.sin(theta) * radius, 0);

            Vec3 holdPos = rotateCenter.add(offset).add(0, 1.8, 0);

            grabbedTarget.setDeltaMovement(Vec3.ZERO);
            grabbedTarget.fallDistance = 0;

            if (grabbedTarget instanceof ServerPlayer p) {
                p.connection.teleport(
                        holdPos.x, holdPos.y, holdPos.z,
                        grabbedTarget.getYRot(), grabbedTarget.getXRot()
                );
            } else {
                grabbedTarget.setPos(holdPos.x, holdPos.y, holdPos.z);
            }

            if (slamTimer == IMPACT_TIME - 1) {
                grabbedTarget.setDeltaMovement(0, -0.6, 0);
                grabbedTarget = null;
            }
        }

        if (slamTimer >= IMPACT_TIME && !impactDealt) {
            performImpact(level, boss);
        }
    }




    private void performImpact(ServerLevel level, BloodBoss boss) {
        impactDealt = true;
        boss.setNoGravity(false);

        float baseDamage = (float) boss.getAttributeValue(Attributes.ATTACK_DAMAGE);

        // 对主要目标造成伤害
        if (impactTarget != null && impactTarget.isAlive()) {
            boss.applyMainTargetDamage(impactTarget, baseDamage, 2.5f);
        }

        // 对范围内的其他实体造成伤害
        applyAreaOfEffectDamage(level, boss, baseDamage);

        level.sendParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                boss.getX(), boss.getY(), boss.getZ(),
                1, 0, 0, 0, 0
        );

        level.playSound(
                null, boss.getX(), boss.getY(), boss.getZ(),
                SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE,
                1.5f, 0.7f
        );
    }



    @Override
    protected void stop(ServerLevel level, BloodBoss boss, long gameTime) {
        super.stop(level, boss, gameTime);
        boss.getBrain().eraseMemory(ModMemoryModuleType.IS_CASTING_SKILL.get());
        ((BloodBossMoveControl) boss.getMoveControl()).clearSkillMovements();
        boss.setNoGravity(false);

        grabbedTarget = null;
        impactTarget  = null;
        grabSuccess   = false;
        impactDealt   = false;
    }

    @Override protected int getActionDuration() {
        return grabSuccess ? DURATION_SUCCESS : DURATION_FAIL;
    }

    @Override protected int getCooldown() { return Cooldown; }
    @Override protected String getAnimationId() { return ANIM_START; }
    @Override protected void doAction(BloodBoss boss) {}

    public static class GrabDashTask extends SkillMovementTask {
        private Vec3 dir;

        public GrabDashTask(int duration) {
            super(duration);
        }

        @Override
        public void start(Mob mob) {
            float yawRad = mob.getYRot() * Mth.DEG_TO_RAD;
            dir = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));
        }

        @Override
        public Vec3 compute(Mob mob, float progress) {
            return new Vec3(dir.x * 1.3, mob.getDeltaMovement().y, dir.z * 1.3);
        }
    }
}
