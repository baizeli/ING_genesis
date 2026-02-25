package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import com.google.common.collect.ImmutableMap;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;

public class BloodBossStageTransitionBehavior extends AnimatedActionBehavior<BloodBoss> {

    public static final int STAGE_TRANSITION_DURATION = 21 * 20;
    private int transitionTick = 0;
    int abyssalShroudEffectDuration =0;

    public BloodBossStageTransitionBehavior() {
        super(ImmutableMap.of(
                MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.BOSS_STAGE.get(), MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected void start(ServerLevel level, BloodBoss entity, long gameTime) {
        super.start(level, entity, gameTime);
        if (entity.hasEffect(MobEffectRegistry.ABYSSAL_SHROUD.get())) {
            var abyssalShroudEffect = entity.getEffect(MobEffectRegistry.ABYSSAL_SHROUD.get());
            if (abyssalShroudEffect != null) {
                abyssalShroudEffectDuration = abyssalShroudEffect.getDuration();
            }
        }
        entity.removeEffect(MobEffectRegistry.ABYSSAL_SHROUD.get());
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
        boss.getNavigation().stop();
        boss.setTarget(null);

        transitionTick++;

        double cx = boss.getX();
        double cy = boss.getY() + 1.5;
        double cz = boss.getZ();
        
        if (transitionTick < 120) {
            spawnRainAndLandParticles(level, boss, cx, cy, cz);
        } else if (transitionTick < 220) {
            double t = transitionTick * 0.2;
            spawnSphericalVectorParticles(level, ParticleRegistry.BLOOD_DRIP_HANG.get(), cx, cy+0.5, cz, 6.0, 60, -0.01);

            double rot = t * 0.5;
            double heightOffset = t * 0.05 -2;
            for (int i = 0; i < 20; i++) {
                double angle = i * Math.PI / 10 + rot;
                double radius = 2.0 + Math.sin(t * 0.1) * 0.5;

                double x1 = cx + radius * Math.cos(angle);
                double z1 = cz + radius * Math.sin(angle);
                double y1 = cy + i * 0.3 + heightOffset;
                double x2 = cx + radius * Math.cos(angle + Math.PI);
                double z2 = cz + radius * Math.sin(angle + Math.PI);
                double y2 = cy + i * 0.3 + heightOffset;

                level.sendParticles(
                    ParticleRegistry.BLOOD_DRIP_HANG.get(),
                    x1, y1, z1,
                    1, 0, 0, 0, 0
                );

                level.sendParticles(
                    ParticleRegistry.BLOOD_DRIP_HANG.get(),
                    x2, y2, z2,
                    1, 0, 0, 0, 0
                );
            }
        } else {
            spawnSphericalVectorParticles(level, ParticleRegistry.BLOOD_DRIP_TWIST.get(), cx, cy+4, cz, 4.5, 10, 0.015);

            for (int i = 0; i < 30; i++) {
                double dx = (level.random.nextDouble() - 0.5) * 4;
                double dz = (level.random.nextDouble() - 0.5) * 4;
                level.sendParticles(ParticleRegistry.BLOOD_DRIP_FALL.get(), cx + dx, cy - 3, cz + dz, 1, 0, 0.3, 0, 0);
            }
        }
        super.tick(level, boss, gameTime);
    }

    private void spawnRainAndLandParticles(ServerLevel level, BloodBoss boss, double cx, double cy, double cz) {
        for (int i = 0; i < 25; i++) {
            double r = level.random.nextDouble() * 6;
            double a = level.random.nextDouble() * Math.PI * 2;
            level.sendParticles(ParticleRegistry.BLOOD_DRIP_FALL.get(), cx + Math.cos(a) * r, cy + 6, cz + Math.sin(a) * r, 1, 0, -0.3, 0, 0);
        }
        for (int i = 0; i < 20; i++) {
            double a = i * Math.PI * 2 / 20;
            level.sendParticles(ParticleRegistry.BLOOD_DRIP_LAND.get(), cx + Math.cos(a) * 5, boss.getY(), cz + Math.sin(a) * 5, 1, 0, 0, 0, 0);
        }
    }

    private void spawnSphericalVectorParticles(ServerLevel level, net.minecraft.core.particles.ParticleType<?> type, double cx, double cy, double cz, double radius, int count, double speedFactor) {
        double randomOffset = level.random.nextDouble() * Math.PI * 2;
        for (int i = 0; i < count; i++) {
            double phi = Math.acos(1 - 2.0 * level.random.nextDouble());
            double theta = level.random.nextDouble() * Math.PI * 2 + randomOffset;
            double x = cx + radius * Math.sin(phi) * Math.cos(theta);
            double y = cy + radius * Math.cos(phi);
            double z = cz + radius * Math.sin(phi) * Math.sin(theta);


            double dx = x - cx;
            double dy = y - cy;
            double dz = z - cz;
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            dx /= distance;
            dy /= distance;
            dz /= distance;


            level.sendParticles((net.minecraft.core.particles.ParticleOptions)type, x, y, z, 0, dx * speedFactor, dy * speedFactor, dz * speedFactor, 1.0);
        }
    }

    @Override protected int getActionTimestamp() { return 400; }
    @Override protected int getActionDuration() { return STAGE_TRANSITION_DURATION; }
    @Override protected int getCooldown() { return 0; }
    @Override protected String getAnimationId() { return "stage_transition_animation"; }

    @Override
    protected void doAction(BloodBoss boss) {
        Brain<BloodBoss> brain = boss.getBrain();
        brain.setMemory(ModMemoryModuleType.HAS_PLAYED_STAGE_TRANSITION.get(), true);
        brain.setMemory(ModMemoryModuleType.BOSS_STAGE.get(), 2);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, BloodBoss boss, long gameTime) {
        if (abyssalShroudEffectDuration!=0){
            boss.addEffect(new MobEffectInstance(
                    MobEffectRegistry.ABYSSAL_SHROUD.get(), abyssalShroudEffectDuration, 0, false, false, false
            ));
        }

        transitionTick = 0;
        boss.getBrain().eraseMemory(MemoryModuleType.IS_EMERGING);
        super.stop(level, boss, gameTime);
    }
}