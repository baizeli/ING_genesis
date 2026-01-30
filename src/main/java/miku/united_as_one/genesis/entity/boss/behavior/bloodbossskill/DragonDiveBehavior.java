package miku.united_as_one.genesis.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.entity.boss.SkillMovementTask;
import miku.united_as_one.genesis.entity.boss.behavior.AnimatedActionBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class DragonDiveBehavior extends AnimatedActionBehavior<BloodBoss> {

    public static final String ANIMATION_ID = "dragon_slam"; // 龙！

    private static final int DURATION = 24;
    private static final int COOLDOWN = 5 * 20;

    private boolean impactDone;

    public DragonDiveBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    /* ==================== 启动条件 ==================== */

    @Override
    protected boolean canStartAction(BloodBoss boss) {
        // 必须在空中
        if (boss.onGround()) return false;

        // 查找脚下最近的地面
        double groundY = findGroundY(boss);

        // 离地必须 ≥ 8 格
        if (boss.getY() - groundY < 12.0) return false;

        // 地面附近必须有目标
        AABB box = new AABB(
                boss.getX() - 8, groundY - 2, boss.getZ() - 8,
                boss.getX() + 8, groundY +12, boss.getZ() + 8
        );

        return !boss.level().getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != boss && e.isAlive()
        ).isEmpty();
    }

    /* ==================== 启动 ==================== */

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);

        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);
        impactDone = false;

        boss.setNoGravity(true);

        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.clearSkillMovements();

            // ① 轻微上抬（给动画一个蓄力感）
            move.addSkillMovement(new SkillMovementTask(6) {
                @Override
                public Vec3 compute(Mob mob, float progress) {
                    return new Vec3(0, 0.5, 0);
                }
            });

            // ② 核心：向下俯冲
            move.addSkillMovement(new DragonDiveTask(18, 2.6));
        }
    }
    private double findGroundY(BloodBoss boss) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(
                boss.getX(), boss.getY(), boss.getZ()
        );

        for (int i = 0; i < 200; i++) {
            pos.move(0, -1, 0);
            if (!boss.level().getBlockState(pos).isAir()) {
                return pos.getY() + 1;
            }
        }
        return -1;
    }


    /* ==================== Tick ==================== */

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);


        if (!impactDone && boss.onGround()) {
            impactDone = true;
            doImpact(level, boss);
        }
    }

    /* ==================== 冲击 ==================== */

    private void doImpact(ServerLevel level, BloodBoss boss) {
        boss.setNoGravity(false);

        // 范围：和抓取龙落地差不多的尺寸
        AABB box = boss.getBoundingBox().inflate(5.5, 2.5, 5.5);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity target : targets) {
            // 统一范围伤害
            boss.applySkillDamage(target, 2.5F);

            // 从 Boss 中心向外击飞（抓取龙同款手感）
            double dx = target.getX() - boss.getX();
            double dz = target.getZ() - boss.getZ();
            target.knockback(1.0F, -dx, -dz);
        }

        // 爆炸粒子（一次就够）
        level.sendParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                1,
                0, 0, 0,
                0
        );

        // 爆炸音效
        level.playSound(
                null,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.HOSTILE,
                1.6f,
                0.75f
        );
    }


    /* ==================== 结束 ==================== */

    @Override
    protected void stop(ServerLevel level, BloodBoss boss, long gameTime) {
        super.stop(level, boss, gameTime);

        boss.getBrain().eraseMemory(ModMemoryModuleType.IS_CASTING_SKILL.get());
        boss.setNoGravity(false);

        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.clearSkillMovements();
        }
    }

    /* ==================== 行为参数 ==================== */

    @Override protected int getActionTimestamp() { return 0; }
    @Override protected int getActionDuration() { return DURATION; }
    @Override protected int getCooldown() { return COOLDOWN; }
    @Override protected String getAnimationId() { return ANIMATION_ID; }
    @Override protected void doAction(BloodBoss entity) {}

    /* ==================== 下落 Task ==================== */

    public static class DragonDiveTask extends SkillMovementTask {

        private final double speed;

        public DragonDiveTask(int duration, double speed) {
            super(duration);
            this.speed = speed;
        }

        @Override
        public Vec3 compute(Mob mob, float progress) {
            return new Vec3(0, -speed, 0);
        }
    }
}
