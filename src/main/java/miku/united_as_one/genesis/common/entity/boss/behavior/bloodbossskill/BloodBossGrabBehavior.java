package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.common.entity.ThrowBloodAndWounds;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.common.entity.boss.SkillMovementTask;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class BloodBossGrabBehavior extends AnimatedActionBehavior<BloodBoss> {

    //冷却
    public static final int COOL_DOWN = 8*20;
    //范围伤害倍率
    public static final float DAMAGE_MULTIPLIER_AREA = 2f;
    //主要目标伤害倍率
    public static final float DAMAGE_MULTIPLIER_MAIN = 3.4f;


    //"登!"
    private static final String ANIM_START = "ascend";

    private static final String ANIM_SLAM  = "dragon_slam"; // 龙！
    public static final String ANIM_FAIL = "failure"; // 寄！

    private static final int DASH_DURATION = 9;
    private static final int IMPACT_TIME   = 20;

    private static final int DURATION_SUCCESS = 49;
    private static final int DURATION_FAIL    = 34;

    private static final int DASH_WINDUP = 8;

    private static final int DASH_START_TICK = DASH_WINDUP;
    private static final int DASH_END_TICK   = DASH_WINDUP + DASH_DURATION;



    private LivingEntity grabbedTarget;
    private LivingEntity impactTarget;

    private boolean grabSuccess;
    private boolean impactDealt;

    private int slamTimer;
    private float slamYaw;
    private boolean grabFailed;


    private float slamAngle;

//应该立刻停止
    private boolean shouldStopImmediately;
    public BloodBossGrabBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
        ));
        shouldStopImmediately = false;
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
        boss.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 5f, 1f);
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
    protected boolean canStillUse(ServerLevel level, BloodBoss entity, long gameTime) {

        return super.canStillUse(level, entity, gameTime)&&!shouldStopImmediately;
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        //设置面向目标
        LivingEntity target = boss.getTarget();
        if (target != null){
            boss.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        }


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
            spawnArrowCycle(level, boss);

            spawnArrowArea(level, boss);

            performImpact(level, boss);
        }
    }

    private static void spawnArrowArea(ServerLevel level, BloodBoss boss) {
        int arrowCount = 100;
        double maxRadius = 12.0;
        double baseHeight = 15.0; // 基础高度
        RandomSource random = level.random;

        for (int i = 0; i < arrowCount; i++) {
            double randomValue = random.nextDouble();
            double randomRadius = Math.sqrt(randomValue) * maxRadius;

            if (randomRadius < 1.0) {
                randomRadius = 1.0 + random.nextDouble() * (maxRadius - 1.0);
            }

            float angle = random.nextFloat() * (float) (2 * Math.PI);

            double y = boss.getY() + baseHeight + (random.nextDouble() - 0.5) * 10.0;
            double x = boss.getX() + Math.cos(angle) * randomRadius;
            double z = boss.getZ() + Math.sin(angle) * randomRadius;

            ThrowBloodAndWounds arrow = new ThrowBloodAndWounds(EntityRegistry.THROW_BLOOD_AND_WOUNDS.get(), level);
            arrow.setOwner(boss);
            arrow.setPos(x, y, z);

            double mx = (random.nextDouble() - 0.5) * 0.3;
            double mz = (random.nextDouble() - 0.5) * 0.3;
            double my = -1.5 - random.nextDouble() * 1.0;

            arrow.shoot(mx, my, mz, (float)Math.sqrt(mx*mx + my*my + mz*mz), 0.0F);

            arrow.setPierceLevel((byte) 100);

            level.addFreshEntity(arrow);
        }
    }
    private static void spawnArrowCycle(ServerLevel level, BloodBoss boss) {
        // 在周围一圈天上生成箭并使其垂直钉向地面，覆盖整个区域
        int arrowCount = 32; // 增加箭的数量以更好地覆盖区域
        double radius1 = 12.0; // 扩大半径距离以覆盖更大范围
        double height = 15.0; // 提高高度以确保箭能覆盖更广的区域

        for (int i = 0; i < arrowCount; i++) {
            float angle = (float) (i * (2 * Math.PI / arrowCount));
            double x = boss.getX() + Math.cos(angle) * radius1;
            double z = boss.getZ() + Math.sin(angle) * radius1;
            double y = boss.getY() + height;

            // 创建箭实体
            ThrowBloodAndWounds arrow = new ThrowBloodAndWounds(EntityRegistry.THROW_BLOOD_AND_WOUNDS.get(), level);
            arrow.setOwner(boss);
            arrow.setPos(x, y, z);
            arrow.setDeltaMovement(0, -2.0, 0); // 增加下降速度以更快命中地面
            arrow.setPierceLevel((byte) 0); // 不穿透

            // 发射箭
            level.addFreshEntity(arrow);
        }
    }


    private void performImpact(ServerLevel level, BloodBoss boss) {
        impactDealt = true;
        boss.setNoGravity(false);

        // 对主要目标造成伤害
        if (impactTarget != null && impactTarget.isAlive()) {
            boss.applySkillDamage(impactTarget, DAMAGE_MULTIPLIER_MAIN);
            impactTarget.push(0, 0.5, 0);
        }

        // 对范围内的其他实体造成伤害
        applyAreaOfEffectDamage(level, boss);

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
        this.shouldStopImmediately= true;
    }

    /**
     * 计算并应用范围伤害效果
     * 
     * @param level 游戏世界
     * @param boss 攻击者
     */
    private void applyAreaOfEffectDamage(ServerLevel level, BloodBoss boss) {
        AABB box = boss.getBoundingBox().inflate(5.5, 2.5, 5.5);
        java.util.List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box,
                ent -> ent != boss && ent.isAlive());
        
        for (LivingEntity target : targets) {
            boss.applySkillDamage(target, DAMAGE_MULTIPLIER_AREA);
            double dx = target.getX() - boss.getX();
            double dz = target.getZ() - boss.getZ();
            target.knockback(0.8, -dx, -dz);
        }
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
        shouldStopImmediately = false;
    }

    @Override protected int getActionDuration() {
        return DURATION_SUCCESS;
    }

    @Override protected int getCooldown() { return COOL_DOWN; }
    @Override protected String getAnimationId() { return ANIM_START; }
    @Override protected void doAction(BloodBoss boss) {}

    public static class GrabDashTask extends SkillMovementTask {
        private Vec3 dir;

        public GrabDashTask(int duration) {
            super(duration);
        }

        @Override
        public void start(Mob mob) {
            if (mob.getTarget() != null) {
                Vec3 from = mob.position().add(0, mob.getBbHeight() * 0.5, 0);
                Vec3 to   = mob.getTarget().position().add(0, mob.getTarget().getBbHeight() * 0.5, 0);
                dir = to.subtract(from).normalize();
            } else {
                float yawRad = mob.getYRot() * Mth.DEG_TO_RAD;
                dir = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));
            }
        }

        @Override
        public Vec3 compute(Mob mob, float progress) {
            double speed = 1.35;
            return dir.scale(speed);
        }
    }

}