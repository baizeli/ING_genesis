package miku.united_as_one.genesis.contents.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.registries.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.contents.entity.boss.BloodBoss;
import miku.united_as_one.genesis.contents.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.contents.entity.boss.behavior.AnimatedActionBehavior;
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

public class DoubleSlashBehavior extends AnimatedActionBehavior<BloodBoss> {

    // 冷却时间 (ticks)
    public static final int COOL_DOWN = 8*20;
    // 伤害倍率
    public static final float DAMAGE_MULTIPLIER = 2.0F;

    private static final int DURATION = 46;

    private static final int FIRST_HIT_TIME = 13;

    private static final int SECOND_HIT_TIME = 32;


    public static final String ANIMATION_ID = "double_slash"; // 二连斩

    private boolean firstHitDone = false;
    private boolean secondHitDone = false;

    public DoubleSlashBehavior() {
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

        firstHitDone = false;
        secondHitDone = false;

        // 播放技能开始音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE,
                1.0F,
                1.0F
        );
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);

        // 在技能期间保持面向目标
        LivingEntity target = boss.getTarget();
        if (target != null) {
            boss.getLookControl().setLookAt(target);
        }

        // 第一次伤害
        if (abilityTimer >= FIRST_HIT_TIME && !firstHitDone) {
            firstHitDone = true;
            dealAreaDamage(boss, 0.8F); // 第一次伤害音调稍低
        }

        // 第二次伤害
        if (abilityTimer >= SECOND_HIT_TIME && !secondHitDone) {
            secondHitDone = true;
            dealAreaDamage(boss, 1.2F); // 第二次伤害音调稍高
        }

        // 在伤害时刻播放音效
        if (abilityTimer == FIRST_HIT_TIME || abilityTimer == SECOND_HIT_TIME) {
            level.playSound(
                    null,
                    boss.getX(),
                    boss.getY(),
                    boss.getZ(),
                    SoundEvents.PLAYER_ATTACK_CRIT,
                    SoundSource.HOSTILE,
                    1.5F,
                    abilityTimer == FIRST_HIT_TIME ? 0.8F : 1.2F
            );
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
        return FIRST_HIT_TIME;
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
     * 对面前4x4范围内的敌人造成伤害
     */
    private void dealAreaDamage(BloodBoss boss, float pitch) {
        ServerLevel level = (ServerLevel) boss.level();

        // 使用身体朝向计算攻击方向
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));

        // 计算攻击区域（面前一格为中心，4x4范围）
        Vec3 attackCenter = boss.position()
                .add(forward.x * 1.5, boss.getEyeHeight() / 2, forward.z * 1.5);

        // 创建4x4x4的攻击区域
        AABB attackBox = AABB.ofSize(attackCenter, 5.5, 4.0, 5.5);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                attackBox,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity target : targets) {
            // 检查目标是否在boss的正面方向
            Vec3 toTarget = target.position().subtract(boss.position()).normalize();

            float dot = (float) toTarget.dot(forward);

            if (dot > 0.0F) { // 只攻击面前180度范围内的目标
                applyDamageToTarget(boss, target);
            }
        }

        // 播放攻击音效
        level.playSound(
                null,
                attackCenter.x,
                attackCenter.y,
                attackCenter.z,
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE,
                1.0F,
                pitch
        );
    }

    /**
     * 对单个目标应用伤害
     */
    private void applyDamageToTarget(BloodBoss boss, LivingEntity target) {
        boss.applySkillDamage(target, DAMAGE_MULTIPLIER);

        // 添加轻微的击退效果
        Vec3 knockbackVec = target.position().subtract(boss.position()).normalize();
        target.knockback(0.3, knockbackVec.x, knockbackVec.z);
    }
}