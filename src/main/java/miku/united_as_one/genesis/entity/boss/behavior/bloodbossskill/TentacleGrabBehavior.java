package miku.united_as_one.genesis.entity.boss.behavior.bloodbossskill;


import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.boss.behavior.AnimatedActionBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class TentacleGrabBehavior extends AnimatedActionBehavior<BloodBoss> {

    // 伤害倍率
    public static final float DAMAGE_MULTIPLIER = 3.5F;

    // 动画时长
    private static final int GRAB_WINDUP_DURATION = 5;    // 0.24秒起手动画
    private static final int GRAB_FAIL_DURATION = 7;      // 0.36秒失败动画
    private static final int GRAB_SUCCESS_DURATION = 24;  // 1.19秒成功动画

    // 关键时间点
    private static final int LIFT_TIME = 6;   // 成功动画0.25秒举到头顶
    private static final int SLAM_TIME = 12;   // 成功动画0.5秒摔下

    public static final String ANIM_GRAB_WINDUP = "tentacle_grab_windup";
    public static final String ANIM_GRAB_FAIL = "tentacle_grab_fail";
    public static final String ANIM_GRAB_SUCCESS = "tentacle_grab_success";

    private LivingEntity grabbedTarget;
    private boolean grabSuccess = false;
    private boolean grabAttempted = false;
    private boolean liftDone = false;
    private boolean slamDone = false;

    public TentacleGrabBehavior() {
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
        return target != null && entity.distanceTo(target) <= 8F;
    }

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);
        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);

        grabbedTarget = null;
        grabSuccess = false;
        grabAttempted = false;
        liftDone = false;
        slamDone = false;

        // 播放抓取准备音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.SQUID_AMBIENT,
                SoundSource.HOSTILE,
                1.0F,
                0.6F
        );

        // 开始起手动画
        boss.serverTriggerAnimation(ANIM_GRAB_WINDUP);
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);

        // 在起手动画结束时尝试抓取
        if (abilityTimer >= GRAB_WINDUP_DURATION && !grabAttempted) {
            grabAttempted = true;
            attemptGrab(boss);
        }

        if (grabSuccess) {
            handleSuccessfulGrab(boss);
        }
    }

    @Override
    protected int getActionDuration() {
        if (!grabAttempted) {
            return GRAB_WINDUP_DURATION;
        }
        return grabSuccess ? GRAB_SUCCESS_DURATION : GRAB_FAIL_DURATION;
    }

    @Override
    protected String getAnimationId() {
        if (!grabAttempted) {
            return ANIM_GRAB_WINDUP;
        }
        return grabSuccess ? ANIM_GRAB_SUCCESS : ANIM_GRAB_FAIL;
    }

    /**
     * 尝试抓取目标
     */
    private void attemptGrab(BloodBoss boss) {
        Level level = boss.level();
        // 计算右前方0.5米处位置
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));
        Vec3 right = new Vec3(Mth.cos(yawRad), 0, Mth.sin(yawRad));

        Vec3 grabPos = boss.position()
                .add(forward.scale(1.2))
                .add(right.scale(0.5))
                .add(0, 1.0, 0);

        AABB grabBox = AABB.ofSize(grabPos, 3.0, 3.0, 3.0);

        var targets = boss.level().getEntitiesOfClass(
                LivingEntity.class,
                grabBox,
                e -> e != boss && e.isAlive() && !e.isSpectator()
        );

        if (!targets.isEmpty()) {
            grabbedTarget = targets.get(0);
            grabSuccess = true;
            // 切换到成功动画
            boss.serverTriggerAnimation(ANIM_GRAB_SUCCESS);

            level.playSound(
                    null,
                    boss.getX(),
                    boss.getY(),
                    boss.getZ(),
                    SoundEvents.SLIME_ATTACK,
                    SoundSource.HOSTILE,
                    1.2F,
                    0.8F
            );
        } else {
            // 切换到失败动画
            boss.serverTriggerAnimation(ANIM_GRAB_FAIL);

            level.playSound(
                    null,
                    boss.getX(),
                    boss.getY(),
                    boss.getZ(),
                    SoundEvents.SQUID_SQUIRT,
                    SoundSource.HOSTILE,
                    0.8F,
                    1.0F
            );
        }
    }

    /**
     * 处理成功抓取后的逻辑
     */
    private void handleSuccessfulGrab(BloodBoss boss) {
        ServerLevel level = (ServerLevel) boss.level();

        if (grabbedTarget == null || !grabbedTarget.isAlive()) {
            grabSuccess = false;
            return;
        }

        // 举起目标到头顶
        if (abilityTimer >= GRAB_WINDUP_DURATION + LIFT_TIME && !liftDone) {
            liftDone = true;
            liftTargetToHead(boss);
        }

        // 摔下目标造成伤害
        if (abilityTimer >= GRAB_WINDUP_DURATION + SLAM_TIME && !slamDone) {
            slamDone = true;
            slamTarget(boss);
        }
    }

    /**
     * 将目标举到头顶
     */
    private void liftTargetToHead(BloodBoss boss) {
        Vec3 headPos = boss.position()
                .add(0, boss.getEyeHeight() + 1.5, 0);

        Level level = boss.level();
        grabbedTarget.setDeltaMovement(Vec3.ZERO);
        grabbedTarget.fallDistance = 0;

        if (grabbedTarget instanceof ServerPlayer player) {
            player.connection.teleport(
                    headPos.x, headPos.y, headPos.z,
                    grabbedTarget.getYRot(), grabbedTarget.getXRot()
            );
        } else {
            grabbedTarget.setPos(headPos.x, headPos.y, headPos.z);
        }

        // 播放举起音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.SLIME_SQUISH,
                SoundSource.HOSTILE,
                0.8F,
                0.5F
        );
    }

    /**
     * 摔下目标造成伤害
     */
    private void slamTarget(BloodBoss boss) {
        ServerLevel level = (ServerLevel) boss.level();

        // 计算正前方摔落位置
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));

        Vec3 slamPos = boss.position()
                .add(forward.scale(3.0))
                .add(0, 0.5, 0);

        // 将目标摔到前方
        if (grabbedTarget instanceof ServerPlayer player) {
            player.connection.teleport(
                    slamPos.x, slamPos.y, slamPos.z,
                    grabbedTarget.getYRot(), grabbedTarget.getXRot()
            );
        } else {
            grabbedTarget.setPos(slamPos.x, slamPos.y, slamPos.z);
        }

        // 造成伤害
        boss.applySkillDamage(grabbedTarget, DAMAGE_MULTIPLIER);

        // 强力击飞效果
        Vec3 knockbackDir = forward.scale(1.5).add(0, 0.8, 0);
        grabbedTarget.setDeltaMovement(knockbackDir);

        grabbedTarget = null;

        // 播放摔地音效
        level.playSound(
                null,
                slamPos.x,
                slamPos.y,
                slamPos.z,
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.HOSTILE,
                1.5F,
                0.7F
        );

        level.playSound(
                null,
                slamPos.x,
                slamPos.y,
                slamPos.z,
                SoundEvents.SLIME_SQUISH_SMALL,
                SoundSource.HOSTILE,
                1.2F,
                1.0F
        );
    }

    @Override
    protected void stop(ServerLevel level, BloodBoss boss, long gameTime) {
        super.stop(level, boss, gameTime);
        boss.getBrain().eraseMemory(ModMemoryModuleType.IS_CASTING_SKILL.get());

        // 确保目标被释放
        if (grabbedTarget != null) {
            grabbedTarget.setDeltaMovement(0, 0.3, 0);
            grabbedTarget = null;
        }
    }

    @Override
    protected int getActionTimestamp() {
        return 0; // 时间点逻辑在tick中处理
    }

    @Override
    protected int getCooldown() {
        return 12 * 20; // 12秒冷却
    }

    @Override
    protected void doAction(BloodBoss entity) {
        // 主要逻辑在tick方法中处理
    }
}