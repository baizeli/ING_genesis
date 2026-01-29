package miku.united_as_one.genesis.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.entity.boss.behavior.AnimatedActionBehavior;
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

public class VerticalHorizontalSlashBehavior extends AnimatedActionBehavior<BloodBoss> {

    // 冷却时间 (ticks)
    public static final int COOL_DOWN = 15*20;

    // 竖劈伤害倍率
    public static final float VERTICAL_DAMAGE_MULTIPLIER = 3.0F;
    // 横劈伤害倍率
    public static final float HORIZONTAL_DAMAGE_MULTIPLIER = 2.0F;

    // 动画时长 (1.77秒转换为ticks: 1.77 * 20 = 35.4 ≈ 35 ticks)
    private static final int DURATION = 35;
    // 竖劈时间 (0.6秒转换为ticks: 0.6 * 20 = 12 ticks)
    private static final int VERTICAL_SLASH_TIME = 12;
    // 横劈时间 (1.29秒转换为ticks: 1.29 * 20 = 25.8 ≈ 26 ticks)
    private static final int HORIZONTAL_SLASH_TIME = 26;

    // 原名称: "劈斩2"
    public static final String ANIMATION_ID = "slash_two";

    private boolean verticalSlashDone = false;
    private boolean horizontalSlashDone = false;

    public VerticalHorizontalSlashBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        LivingEntity target = entity.getTarget();
        return target != null && entity.distanceTo(target) <= 10F;
    }

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);
        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);

        verticalSlashDone = false;
        horizontalSlashDone = false;

        // 播放技能开始音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE,
                1.0F,
                0.8F
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

        // 竖劈伤害
        if (abilityTimer >= VERTICAL_SLASH_TIME && !verticalSlashDone) {
            verticalSlashDone = true;
            dealVerticalSlashDamage(boss, 0.9F);
        }

        // 横劈伤害
        if (abilityTimer >= HORIZONTAL_SLASH_TIME && !horizontalSlashDone) {
            horizontalSlashDone = true;
            dealHorizontalSlashDamage(boss, 1.1F);
        }

        // 在伤害时刻播放音效
        if (abilityTimer == VERTICAL_SLASH_TIME) {
            level.playSound(
                    null,
                    boss.getX(),
                    boss.getY(),
                    boss.getZ(),
                    SoundEvents.PLAYER_ATTACK_CRIT,
                    SoundSource.HOSTILE,
                    1.5F,
                    0.9F
            );
        } else if (abilityTimer == HORIZONTAL_SLASH_TIME) {
            level.playSound(
                    null,
                    boss.getX(),
                    boss.getY(),
                    boss.getZ(),
                    SoundEvents.PLAYER_ATTACK_SWEEP,
                    SoundSource.HOSTILE,
                    1.5F,
                    1.1F
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
        return VERTICAL_SLASH_TIME;
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
     * 竖劈：前方1 * 4格区域，造成300%伤害
     */
    private void dealVerticalSlashDamage(BloodBoss boss, float pitch) {
        ServerLevel level = (ServerLevel) boss.level();

        // 使用身体朝向计算攻击方向
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));

        // 计算攻击区域（前方1 * 4格）
        Vec3 attackCenter = boss.position()
                .add(forward.x * 2.5, boss.getEyeHeight() / 2, forward.z * 2.5);

        // 创建1 * 4 * 4的攻击区域（窄而长）
        AABB attackBox = AABB.ofSize(attackCenter, 1.0, 4.0, 4.0);

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
                applyVerticalDamageToTarget(boss, target);
            }
        }

        // 播放竖劈音效
        level.playSound(
                null,
                attackCenter.x,
                attackCenter.y,
                attackCenter.z,
                SoundEvents.PLAYER_ATTACK_CRIT,
                SoundSource.HOSTILE,
                1.2F,
                pitch
        );
    }

    /**
     * 横劈：前方4 * 4格区域，造成200%伤害
     */
    private void dealHorizontalSlashDamage(BloodBoss boss, float pitch) {
        ServerLevel level = (ServerLevel) boss.level();

        // 使用身体朝向计算攻击方向
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));

        // 计算攻击区域（前方4 * 4格）
        Vec3 attackCenter = boss.position()
                .add(forward.x * 2.0, boss.getEyeHeight() / 2, forward.z * 2.0);

        // 创建4 * 4 * 4的攻击区域（宽而短）
        AABB attackBox = AABB.ofSize(attackCenter, 4.0, 4.0, 4.0);

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
                applyHorizontalDamageToTarget(boss, target);
            }
        }

        // 播放横劈音效
        level.playSound(
                null,
                attackCenter.x,
                attackCenter.y,
                attackCenter.z,
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE,
                1.5F,
                pitch
        );
    }

    /**
     * 对单个目标应用竖劈伤害（300%）
     */
    private void applyVerticalDamageToTarget(BloodBoss boss, LivingEntity target) {
        boss.applySkillDamage(target, VERTICAL_DAMAGE_MULTIPLIER);

        // 竖劈有更强的垂直击退
        target.knockback(0.4, 0, 0);
        target.setDeltaMovement(target.getDeltaMovement().add(0, 0.3, 0));
    }

    /**
     * 对单个目标应用横劈伤害（200%）
     */
    private void applyHorizontalDamageToTarget(BloodBoss boss, LivingEntity target) {
        boss.applySkillDamage(target, HORIZONTAL_DAMAGE_MULTIPLIER);

        // 横劈有水平击退效果
        Vec3 knockbackVec = target.position().subtract(boss.position()).normalize();
        target.knockback(0.5, knockbackVec.x, knockbackVec.z);
    }
}