package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

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

public class ZhanZhanCycloneSlashBehavior extends AnimatedActionBehavior<BloodBoss> {

    // 冷却时间 (ticks)
    public static final int COOL_DOWN = 18*20;
    // 伤害倍率
    public static final float DAMAGE_MULTIPLIER = 2.0F;

    // 动画时长 (2.26秒转换为ticks: 2.26 * 20 = 45.2 ≈ 45 ticks)
    private static final int DURATION = 45;

    // 四次斩击时间点
    private static final int FIRST_SLASH_TIME = 10;  // 0.5秒
    private static final int SECOND_SLASH_TIME = 20; // 1.02秒
    private static final int THIRD_SLASH_TIME = 29;  // 1.43秒
    private static final int FOURTH_SLASH_TIME = 35;  // 1.76秒

    // 攻击范围半径
    private static final double FIRST_RANGE = 3.0;   // 3x3
    private static final double SECOND_RANGE = 4.0;  // 4x4
    private static final double THIRD_RANGE = 3.0;   // 3x3
    private static final double FOURTH_RANGE = 5.5;  // 5.5x5.5

    // 原名称: 斩斩旋风劈2
    public static final String ANIMATION_ID = "zhan_zhan_xuan_feng_pi_2";

    private boolean[] slashDone = new boolean[4];

    public ZhanZhanCycloneSlashBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        LivingEntity target = entity.getTarget();
        return target != null && entity.distanceTo(target) <= 12F;
    }

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);
        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);

        // 重置所有斩击状态
        for (int i = 0; i < slashDone.length; i++) {
            slashDone[i] = false;
        }

        // 播放技能开始音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE,
                1.2F,
                0.7F
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

        // 第一次斩击
        if (abilityTimer >= FIRST_SLASH_TIME && !slashDone[0]) {
            slashDone[0] = true;
            dealSlashDamage(boss, FIRST_RANGE, 0.8F, false);
        }

        // 第二次斩击
        if (abilityTimer >= SECOND_SLASH_TIME && !slashDone[1]) {
            slashDone[1] = true;
            dealSlashDamage(boss, SECOND_RANGE, 0.9F, false);
        }

        // 第三次斩击
        if (abilityTimer >= THIRD_SLASH_TIME && !slashDone[2]) {
            slashDone[2] = true;
            dealSlashDamage(boss, THIRD_RANGE, 1.0F, false);
        }

        // 第四次斩击（最后一击，强力击退）
        if (abilityTimer >= FOURTH_SLASH_TIME && !slashDone[3]) {
            slashDone[3] = true;
            dealSlashDamage(boss, FOURTH_RANGE, 1.2F, true);
        }

        // 在每次斩击时刻播放不同的音效
        if (abilityTimer == FIRST_SLASH_TIME) {
            playSlashSound(level, boss, 0.8F);
        } else if (abilityTimer == SECOND_SLASH_TIME) {
            playSlashSound(level, boss, 0.9F);
        } else if (abilityTimer == THIRD_SLASH_TIME) {
            playSlashSound(level, boss, 1.0F);
        } else if (abilityTimer == FOURTH_SLASH_TIME) {
            playFinalSlashSound(level, boss);
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
        return FIRST_SLASH_TIME;
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
     * 播放普通斩击音效
     */
    private void playSlashSound(ServerLevel level, BloodBoss boss, float pitch) {
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE,
                1.0F,
                pitch
        );
    }

    /**
     * 播放最终斩击音效（更强烈）
     */
    private void playFinalSlashSound(ServerLevel level, BloodBoss boss) {
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.PLAYER_ATTACK_CRIT,
                SoundSource.HOSTILE,
                2.0F,
                0.6F
        );

        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.HOSTILE,
                0.8F,
                1.2F
        );
    }

    /**
     * 处理斩击伤害
     *
     * @param boss 攻击者
     * @param range 攻击范围半径
     * @param pitch 音效音调
     * @param isFinalHit 是否为最后一击（强力击退）
     */
    private void dealSlashDamage(BloodBoss boss, double range, float pitch, boolean isFinalHit) {
        ServerLevel level = (ServerLevel) boss.level();

        // 使用身体朝向计算攻击方向
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));

        // 计算攻击区域中心（面前）
        Vec3 attackCenter = boss.position()
                .add(forward.x * 2.0, boss.getEyeHeight() / 2, forward.z * 2.0);

        // 创建攻击区域
        AABB attackBox = AABB.ofSize(attackCenter, range * 2, 4.0, range * 2);

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
                if (isFinalHit) {
                    applyFinalDamageToTarget(boss, target);
                } else {
                    applyNormalDamageToTarget(boss, target);
                }
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
                isFinalHit ? 1.5F : 1.0F,
                pitch
        );
    }

    /**
     * 对单个目标应用普通斩击伤害
     */
    private void applyNormalDamageToTarget(BloodBoss boss, LivingEntity target) {
        boss.applySkillDamage(target, DAMAGE_MULTIPLIER);

        // 普通击退效果
        Vec3 knockbackVec = target.position().subtract(boss.position()).normalize();
        target.knockback(0.3, knockbackVec.x, knockbackVec.z);
    }

    /**
     * 对单个目标应用最终斩击伤害（强力击退）
     */
    private void applyFinalDamageToTarget(BloodBoss boss, LivingEntity target) {
        boss.applySkillDamage(target, DAMAGE_MULTIPLIER);

        // 强力击退效果
        Vec3 knockbackVec = target.position().subtract(boss.position()).normalize();
        double power = 1.2; // 更强的击退力量

        // 水平击退
        target.knockback(power, knockbackVec.x, knockbackVec.z);

        // 额外的垂直击退
        target.setDeltaMovement(target.getDeltaMovement().add(0, 0.5, 0));

        // 添加击退抗性忽略（如果需要）
        // target.hurtMarked = true;
    }
}