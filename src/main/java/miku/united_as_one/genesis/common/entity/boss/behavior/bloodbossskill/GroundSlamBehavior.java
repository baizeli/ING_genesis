package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.common.entity.TremorAoeEntity;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class GroundSlamBehavior extends AnimatedActionBehavior<BloodBoss> {

    // 冷却时间 (ticks)
    public static final int COOL_DOWN = 16*20;
    // 伤害倍率
    public static final float DAMAGE_MULTIPLIER = 5.0F;

    // 动画时长 (2秒转换为ticks: 2 * 20 = 40 ticks)
    private static final int DURATION = 40;
    // 伤害时间 (1.58秒转换为ticks: 1.58 * 20 = 31.6 ≈ 32 ticks)
    private static final int SLAM_TIME = 32;
    public static final String ANIMATION_ID = "ground_slam"; // 砸地

    private boolean slamDone = false;

    public GroundSlamBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        LivingEntity target = entity.getTarget();
        return target != null && entity.distanceTo(target) <= 8F;
    }

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);
        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);

        slamDone = false;

        // 播放砸地准备音效（蓄力音效）
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.IRON_GOLEM_HURT,
                SoundSource.HOSTILE,
                0.8F,
                0.6F
        );
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);

        // 砸地伤害
        if (abilityTimer >= SLAM_TIME && !slamDone) {
            slamDone = true;
            dealGroundSlamDamage(boss);
            TremorAoeEntity tremor = new TremorAoeEntity(level, 5F, 0.3F, 0.5F);
            tremor.setPos(boss.getX(), boss.getY(), boss.getZ());
            tremor.setOwner(boss);
            level.addFreshEntity(tremor);
        }

        // 在砸地时刻播放强烈音效
        if (abilityTimer == SLAM_TIME) {
            playGroundSlamSound(level, boss);
        }

        // 在砸地前添加蓄力效果（粒子效果等）
        if (abilityTimer == SLAM_TIME - 5) {
            playChargeSound(level, boss);
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

    @Override
    protected int getActionTimestamp() {
        return SLAM_TIME;
    }

    @Override
    protected int getActionDuration() {
        return DURATION;
    }

    @Override
    protected int getCooldown() {
        return COOL_DOWN;
    }

    @Override
    protected String getAnimationId() {
        return ANIMATION_ID;
    }

    @Override
    protected void doAction(BloodBoss entity) {
        // 主要伤害逻辑在tick方法中处理，这里可以留空
    }

    /**
     * 播放蓄力音效
     */
    private void playChargeSound(ServerLevel level, BloodBoss boss) {
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.LIGHTNING_BOLT_IMPACT,
                SoundSource.HOSTILE,
                0.5F,
                0.8F
        );
    }

    /**
     * 播放砸地音效
     */
    private void playGroundSlamSound(ServerLevel level, BloodBoss boss) {
        // 强烈的砸地音效组合
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.HOSTILE,
                2.0F,
                0.5F
        );

        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.ANVIL_LAND,
                SoundSource.HOSTILE,
                1.5F,
                0.7F
        );

        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                SoundSource.HOSTILE,
                1.2F,
                0.6F
        );
    }

    /**
     * 处理砸地伤害 - 正前方2格为中心5 * 5的范围伤害
     */
    private void dealGroundSlamDamage(BloodBoss boss) {
        ServerLevel level = (ServerLevel) boss.level();

        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));

        double groundY = boss.getBoundingBox().minY + 0.1;

        Vec3 attackCenter = new Vec3(
                boss.getX() + forward.x * 2.0,
                groundY,
                boss.getZ() + forward.z * 2.0
        );

        // 地面砸击：范围大，但贴地
        AABB attackBox = AABB.ofSize(
                attackCenter,
                5.0,   // 宽
                0.8,
                5.0
        );

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                attackBox,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity target : targets) {
            applyGroundSlamDamageToTarget(boss, target);
        }

        // 冲击音效
        level.playSound(
                null,
                attackCenter.x,
                attackCenter.y,
                attackCenter.z,
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.HOSTILE,
                1.5F,
                0.6F
        );
    }


    /**
     * 对单个目标应用砸地伤害（500%伤害）
     */
    private void applyGroundSlamDamageToTarget(BloodBoss boss, LivingEntity target) {
        boss.applySkillDamage(target, DAMAGE_MULTIPLIER);

        // 强力击退效果 - 将敌人击飞并向后抛
        Vec3 toTarget = target.position().subtract(boss.position()).normalize();
        double horizontalPower = 1.2; // 强力水平击退
        double verticalPower = 0.8;  // 强力垂直击飞

        // 水平击退（方向为从boss指向目标）
        target.knockback(horizontalPower, toTarget.x, toTarget.z);

        // 垂直击飞效果
        target.setDeltaMovement(target.getDeltaMovement().add(0, verticalPower, 0));

        // 添加击晕或减速效果（如果有相关系统）
        // applyStunEffect(target);
    }

    /**
     * 应用击晕效果（如果需要）
     */
    /*
    private void applyStunEffect(LivingEntity target) {
        // 这里可以添加击晕或减速状态效果
        // 例如：target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));
    }
    */
}
