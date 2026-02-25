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

public class StompBehavior extends AnimatedActionBehavior<BloodBoss> {

    // 冷却时间 (ticks)
    public static final int COOL_DOWN = 10*20;
    // 伤害倍率
    public static final float DAMAGE_MULTIPLIER = 3.0F;

    // 动画时长 (0.67秒转换为ticks: 0.67 * 20 = 13.4 ≈ 13 ticks)
    private static final int DURATION = 13;
    // 伤害时间 (0.54秒转换为ticks: 0.54 * 20 = 10.8 ≈ 11 ticks)
    private static final int STOMP_TIME = 11;

    public static final String ANIMATION_ID = "stomp_common_phase_one_two"; // 跺脚（一二阶段通用）

    private boolean stompDone = false;

    public StompBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        LivingEntity target = entity.getTarget();
        return target != null && entity.distanceTo(target) <= 6F;
    }

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);
        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);

        stompDone = false;

        // 播放跺脚准备音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.HOGLIN_STEP,
                SoundSource.HOSTILE,
                0.5F,
                0.8F
        );
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);

        // 跺脚伤害
        if (abilityTimer >= STOMP_TIME && !stompDone) {
            stompDone = true;
            TremorAoeEntity tremor = new TremorAoeEntity(level, 4F, 0.2F, 0.3F);
            tremor.setPos(boss.getX(), boss.getY(), boss.getZ());
            tremor.setOwner(boss);
            level.addFreshEntity(tremor);
            dealStompDamage(boss);
        }

        // 在跺脚时刻播放强烈音效
        if (abilityTimer == STOMP_TIME) {
            playStompSound(level, boss);
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
        return STOMP_TIME;
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
     * 播放跺脚音效
     */
    private void playStompSound(ServerLevel level, BloodBoss boss) {
        // 强烈的地面震动音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.HOSTILE,
                0.8F,
                0.6F
        );

        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.IRON_GOLEM_DAMAGE,
                SoundSource.HOSTILE,
                1.2F,
                0.7F
        );
    }

    /**
     * 处理跺脚伤害 - 以身前1格为中心造成5 * 5的范围伤害（贴地）
     */
    private void dealStompDamage(BloodBoss boss) {
        ServerLevel level = (ServerLevel) boss.level();

        // 使用身体朝向计算攻击方向
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));

        double groundY = boss.getBoundingBox().minY + 0.1;

        // 计算攻击区域中心（身前1格，贴地）
        Vec3 attackCenter = new Vec3(
                boss.getX() + forward.x * 1.0,
                groundY,
                boss.getZ() + forward.z * 1.0
        );

        // 创建 5 x 5 x 2 的攻击区域（宽度5，高度2，地面技能）
        AABB attackBox = AABB.ofSize(
                attackCenter,
                5.0,
                2.0,
                5.0
        );

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                attackBox,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity target : targets) {
            // 跺脚是范围攻击，不需要检查朝向
            applyStompDamageToTarget(boss, target);
        }

        // 播放跺脚冲击波音效
        level.playSound(
                null,
                attackCenter.x,
                attackCenter.y,
                attackCenter.z,
                SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                SoundSource.HOSTILE,
                1.0F,
                0.8F
        );

        // 可选：粒子
        // spawnStompParticles(level, attackCenter);
    }


    /**
     * 对单个目标应用跺脚伤害
     */
    private void applyStompDamageToTarget(BloodBoss boss, LivingEntity target) {
        boss.applySkillDamage(target, DAMAGE_MULTIPLIER);

        // 跺脚特有的击退效果 - 将敌人向上击飞
        Vec3 toTarget = target.position().subtract(boss.position()).normalize();
        double horizontalPower = 0.4;
        double verticalPower = 0.6;

        // 水平击退
        target.knockback(horizontalPower, toTarget.x, toTarget.z);

        // 垂直击飞效果
        target.setDeltaMovement(target.getDeltaMovement().add(0, verticalPower, 0));

        // 添加短暂的减速效果（模拟震动）
        // if (target instanceof ServerPlayer) {
        //     // 可以在这里添加状态效果
        // }
    }

    /**
     * 生成跺脚粒子效果（如果需要）
     */
    /*
    private void spawnStompParticles(ServerLevel level, Vec3 center) {
        // 这里可以添加跺脚的地面震动粒子效果
        // 需要根据您的粒子系统来实现
    }
    */
}