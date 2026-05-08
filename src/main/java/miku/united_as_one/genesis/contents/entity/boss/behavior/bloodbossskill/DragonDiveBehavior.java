package miku.united_as_one.genesis.contents.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.contents.entity.TremorAoeEntity;
import miku.united_as_one.genesis.registries.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.contents.entity.boss.BloodBoss;
import miku.united_as_one.genesis.contents.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.contents.entity.boss.SkillMovementTask;
import miku.united_as_one.genesis.contents.entity.boss.behavior.AnimatedActionBehavior;
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

    public static final String ANIMATION_ID = "dragon_slam"; 

    private static final int DURATION = 24;
    private static final int COOLDOWN = 5 * 20;

    private boolean impactDone;

    public DragonDiveBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    

    @Override
    protected boolean canStartAction(BloodBoss boss) {
        
        if (boss.onGround()) return false;

        
        double groundY = findGroundY(boss);

        
        if (boss.getY() - groundY < 12.0) return false;

        
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

    

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);

        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);
        impactDone = false;

        boss.setNoGravity(true);

        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.clearSkillMovements();

            
            move.addSkillMovement(new SkillMovementTask(6) {
                @Override
                public Vec3 compute(Mob mob, float progress) {
                    return new Vec3(0, 0.5, 0);
                }
            });

            
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


    

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);


        if (!impactDone && boss.onGround()) {
            impactDone = true;
            doImpact(level, boss);
        }
    }

    

    private void doImpact(ServerLevel level, BloodBoss boss) {
        boss.setNoGravity(false);

        
        AABB box = boss.getBoundingBox().inflate(5.5, 2.5, 5.5);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity target : targets) {
            
            boss.applySkillDamage(target, 2.5F);

            
            double dx = target.getX() - boss.getX();
            double dz = target.getZ() - boss.getZ();
            target.knockback(1.0F, -dx, -dz);
        }

        TremorAoeEntity tremor = new TremorAoeEntity(level, 10.0F, 0.15F, 0.6F);
        tremor.setPos(boss.getX(), boss.getY(), boss.getZ());
        tremor.setOwner(boss);
        level.addFreshEntity(tremor);

        
        level.sendParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                boss.getX(),
                boss.getY(),
                boss.getZ(),
                1,
                0, 0, 0,
                0
        );

        
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


    

    @Override
    protected void stop(ServerLevel level, BloodBoss boss, long gameTime) {
        super.stop(level, boss, gameTime);

        boss.getBrain().eraseMemory(ModMemoryModuleType.IS_CASTING_SKILL.get());
        boss.setNoGravity(false);

        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.clearSkillMovements();
        }
    }

    

    @Override protected int getActionTimestamp() { return 0; }
    @Override protected int getActionDuration() { return DURATION; }
    @Override protected int getCooldown() { return COOLDOWN; }
    @Override protected String getAnimationId() { return ANIMATION_ID; }
    @Override protected void doAction(BloodBoss entity) {}

    

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
