package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
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

public class TentacleAttackBehavior extends AnimatedActionBehavior<BloodBoss> {

    // 伤害倍率
    public static final float DAMAGE_MULTIPLIER = 3.0F;

    // 动画时长 (1.07秒 = 21.4 ticks ≈ 21 ticks)
    private static final int DURATION = 21;
    // 伤害时间 (0.5秒 = 10 ticks)
    private static final int ATTACK_TIME = 10;

    public static final String ANIMATION_ID = "tentacle_attack";

    private boolean attackDone = false;

    public TentacleAttackBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BloodBoss owner) {
        Integer i = owner.getBrain().getMemory(ModMemoryModuleType.BOSS_STAGE.get()).orElse(0);
        if (i<=1){
            return false;
        }
        return super.checkExtraStartConditions(level, owner);
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
        attackDone = false;

        // 播放触手攻击音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.SQUID_SQUIRT,
                SoundSource.HOSTILE,
                1.0F,
                0.8F
        );
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);

        // 触手攻击伤害
        if (abilityTimer >= ATTACK_TIME && !attackDone) {
            attackDone = true;
            dealTentacleDamage(boss);
        }

        // 攻击时刻音效
        if (abilityTimer == ATTACK_TIME) {
            level.playSound(
                    null,
                    boss.getX(),
                    boss.getY(),
                    boss.getZ(),
                    SoundEvents.PLAYER_ATTACK_CRIT,
                    SoundSource.HOSTILE,
                    1.2F,
                    0.9F
            );
        }
    }

    @Override
    protected void stop(ServerLevel level, BloodBoss boss, long gameTime) {
        super.stop(level, boss, gameTime);
        boss.getBrain().eraseMemory(ModMemoryModuleType.IS_CASTING_SKILL.get());
    }

    @Override
    protected int getActionTimestamp() {
        return ATTACK_TIME;
    }

    @Override
    protected int getActionDuration() {
        return DURATION;
    }

    @Override
    protected int getCooldown() {
        return 8 * 20; // 8秒冷却
    }

    @Override
    protected String getAnimationId() {
        return ANIMATION_ID;
    }

    @Override
    protected void doAction(BloodBoss entity) {
        // 主要伤害逻辑在tick方法中处理
    }

    /**
     * 处理触手攻击伤害 - 右前方扇形区域
     */
    private void dealTentacleDamage(BloodBoss boss) {
        ServerLevel level = (ServerLevel) boss.level();

        // 计算右前方位置（基于boss朝向）
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));
        Vec3 right = new Vec3(Mth.cos(yawRad), 0, Mth.sin(yawRad));

        // 右前方0.5米处
        Vec3 attackCenter = boss.position()
                .add(forward.scale(1.5))
                .add(right.scale(0.5))
                .add(0, boss.getEyeHeight() / 2, 0);

        // 扇形攻击区域
        AABB attackBox = AABB.ofSize(attackCenter, 4.0, 3.0, 4.0);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                attackBox,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity target : targets) {
            // 检查目标是否在右前方扇形区域内
            Vec3 toTarget = target.position().subtract(boss.position()).normalize();
            Vec3 rightForward = forward.add(right.scale(0.5)).normalize();
            float dot = (float) toTarget.dot(rightForward);

            if (dot > 0.5F) { // 60度扇形区域
                applyTentacleDamage(boss, target);
            }
        }

        // 播放触手攻击命中音效
        level.playSound(
                null,
                attackCenter.x,
                attackCenter.y,
                attackCenter.z,
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE,
                1.0F,
                1.2F
        );
    }

    /**
     * 应用触手伤害
     */
    private void applyTentacleDamage(BloodBoss boss, LivingEntity target) {
        boss.applySkillDamage(target, DAMAGE_MULTIPLIER);

        // 触手特有的击退效果 - 向右侧击飞
        Vec3 knockbackDir = target.position().subtract(boss.position()).cross(new Vec3(0, 1, 0)).normalize();
        target.knockback(0.6, knockbackDir.x, knockbackDir.z);
    }
}