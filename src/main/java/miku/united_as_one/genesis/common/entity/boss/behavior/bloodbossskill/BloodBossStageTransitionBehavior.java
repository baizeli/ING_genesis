package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import com.google.common.collect.ImmutableMap;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;

public class BloodBossStageTransitionBehavior extends AnimatedActionBehavior<BloodBoss> {

    public static final int STAGE_TRANSITION_DURATION = 21 * 20;
    private int transitionTick = 0;

    public BloodBossStageTransitionBehavior() {
        super(ImmutableMap.of(
                MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.BOSS_STAGE.get(), MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        Brain<BloodBoss> brain = entity.getBrain();

        int stage = brain.getMemory(ModMemoryModuleType.BOSS_STAGE.get()).orElse(1);
        boolean played = brain.getMemory(ModMemoryModuleType.HAS_PLAYED_STAGE_TRANSITION.get()).orElse(false);
        int stun = brain.getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get()).orElse(0);

        return stage == 1 && !played && stun >= 1;
    }

    @Override
    protected void tick(@NotNull ServerLevel level, BloodBoss boss, long gameTime) {
        Brain<BloodBoss> brain = boss.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        boss.setTarget(null);

        transitionTick++;

        double cx = boss.getX();
        double cy = boss.getY() + 1.5;
        double cz = boss.getZ();
        
        if (transitionTick < 120) {
            for (int i = 0; i < 60; i++) {
                double r = level.random.nextDouble() * 6;
                double a = level.random.nextDouble() * Math.PI * 2;

                double x = cx + Math.cos(a) * r;
                double z = cz + Math.sin(a) * r;
                double y = cy + 6;

                level.sendParticles(
                        ParticleRegistry.BLOOD_DRIP_FALL.get(),
                        x, y, z,
                        1, 0, -0.3, 0, 0
                );
            }

            for (int i = 0; i < 40; i++) {
                double a = i * Math.PI * 2 / 40;
                double r = 5;

                level.sendParticles(
                        ParticleRegistry.BLOOD_DRIP_LAND.get(),
                        cx + Math.cos(a) * r,
                        boss.getY(),
                        cz + Math.sin(a) * r,
                        1, 0, 0, 0, 0
                );
            }
        } else if (transitionTick < 320) {
            double t = transitionTick * 0.2;
            
            for (int i = 0; i < 80; i++) {
                double angle = t + i * 0.3;
                double y = cy - 2 + i * 0.05;

                double x1 = cx + Math.cos(angle) * 3;
                double z1 = cz + Math.sin(angle) * 3;

                double x2 = cx - Math.cos(angle) * 3;
                double z2 = cz - Math.sin(angle) * 3;

                level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), x1, y, z1, 1, 0, 0, 0, 0);
                level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), x2, y, z2, 1, 0, 0, 0, 0);
            }
            
            for (int i = 0; i < 120; i++) {
                double phi = Math.acos(2 * level.random.nextDouble() - 1);
                double theta = 2 * Math.PI * level.random.nextDouble();

                double r = 6;

                double x = cx + r * Math.sin(phi) * Math.cos(theta);
                double y = cy + r * Math.cos(phi);
                double z = cz + r * Math.sin(phi) * Math.sin(theta);

                level.sendParticles(
                        ParticleRegistry.BLOOD_DRIP_HANG.get(),
                        x, y, z,
                        1, 0, 0, 0, 0
                );
            }
            
            double rot = t * 0.5;

            for (int xi = -2; xi <= 2; xi++)
                for (int yi = -2; yi <= 2; yi++)
                    for (int zi = -2; zi <= 2; zi++) {

                        double x = cx + xi * Math.cos(rot) - zi * Math.sin(rot);
                        double z = cz + xi * Math.sin(rot) + zi * Math.cos(rot);
                        double y = cy + yi;

                        level.sendParticles(
                                ParticleRegistry.BLOOD_DRIP_HANG.get(),
                                x, y, z,
                                1, 0, 0, 0, 0
                        );
                    }
        } else {
            for (int i = 0; i < 300; i++) {

                double phi = Math.acos(2 * level.random.nextDouble() - 1);
                double theta = 2 * Math.PI * level.random.nextDouble();

                double r = 10;

                double x = cx + r * Math.sin(phi) * Math.cos(theta);
                double y = cy + r * Math.cos(phi);
                double z = cz + r * Math.sin(phi) * Math.sin(theta);

                level.sendParticles(
                        ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                        x, y, z,
                        1, 0, 0.02, 0, 0
                );
            }

            
            for (int i = 0; i < 100; i++) {

                double dx = (level.random.nextDouble() - 0.5) * 4;
                double dz = (level.random.nextDouble() - 0.5) * 4;

                level.sendParticles(
                        ParticleRegistry.BLOOD_DRIP_FALL.get(),
                        cx + dx,
                        cy - 3,
                        cz + dz,
                        1, 0, 0.3, 0, 0
                );
            }

            boss.realSetDeltaMovement(0, 0.05, 0);
        }

        super.tick(level, boss, gameTime);
    }

    @Override
    protected int getActionTimestamp() {
        return 20 * 20;
    }

    @Override
    protected int getActionDuration() {
        return STAGE_TRANSITION_DURATION;
    }

    @Override
    protected int getCooldown() {
        return 0;
    }

    @Override
    protected String getAnimationId() {
        return "stage_transition_animation";
    }

    @Override
    protected void doAction(BloodBoss boss) {
        Brain<BloodBoss> brain = boss.getBrain();

        brain.setMemory(ModMemoryModuleType.HAS_PLAYED_STAGE_TRANSITION.get(), true);
        brain.setMemory(ModMemoryModuleType.BOSS_STAGE.get(), 2);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, BloodBoss boss, long gameTime) {
        transitionTick = 0;
        boss.getBrain().eraseMemory(MemoryModuleType.IS_EMERGING);

        super.stop(level, boss, gameTime);
    }
}
