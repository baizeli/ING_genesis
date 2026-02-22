package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import com.google.common.collect.ImmutableMap;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BloodBossStunBehavior extends AnimatedActionBehavior<BloodBoss> {

    private int currentWave = 0;
    private int waveTimer = 0;
    private int waveRadius = 0;
    private int particleTicks = 0;

    public static final int STUN_DURATION = (int)(20 * 9.5);
    private static final int[] WAVE_START_TIMES = {(int)(20 * 6.5), (int)(20 * 7.75), 20 * 9};
    private static final int WAVE_DURATION = 20;
    private static final int MAX_WAVE_RADIUS = 20;
    private static final int PARTICLE_DURATION = 40;
    private static final int PARTICLE_INTERVAL = 2;

    private static final float STUN_DAMAGE_MULTIPLIER = 1.8f;
    private static final double STUN_DAMAGE_HEIGHT = 3.0;
    int abyssalShroudEffectDuration =0;

    public BloodBossStunBehavior() {
        super(ImmutableMap.of(MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        Brain<BloodBoss> brain = entity.getBrain();
        Integer stunCount = brain.getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get()).orElse(0);
        if (stunCount >= 1) return false;
        return entity.getHealth() <= entity.getMaxHealth() * 0.7;
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
    protected void tick(ServerLevel level, BloodBoss owner, long gameTime) {
        Brain<BloodBoss> brain = owner.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        owner.getNavigation().stop();
        super.tick(level, owner, gameTime);

        for (int i = 0; i < WAVE_START_TIMES.length; i++) {
            if (abilityTimer == WAVE_START_TIMES[i] && currentWave != i + 1) {
                currentWave = i + 1;
                waveTimer = 0;

                spawnTentacles(level, owner, (5 * (i + 1)) / 2, 8 + i * 8.0F);
                int[] waveDamageRadius = {8, 11, 15};
                applyStunAreaDamage(level, owner, waveDamageRadius[i]);
                break;
            }
        }

        if (currentWave > 0 && waveTimer < WAVE_DURATION) {
            waveTimer++;
            int[] waveMaxRadii =  {8, 11, 15};
            waveRadius = (int)((float)waveTimer / WAVE_DURATION * waveMaxRadii[currentWave - 1]);
            spawnGroundShakeWave(level, owner, waveRadius, currentWave);

            CameraShakeManager.addCameraShake(new CameraShakeData(
                    level, 15 + MAX_WAVE_RADIUS, owner.position(), (1.0f + currentWave * 0.5f) * 10.0f
            ));

            if (waveTimer >= WAVE_DURATION) currentWave = 0;
        }

        if (particleTicks < PARTICLE_DURATION) {
            particleTicks++;
            if (particleTicks % PARTICLE_INTERVAL == 0) {
                spawnSkillParticles(level, owner, particleTicks, currentWave);
            }
        }
    }

    private void applyStunAreaDamage(ServerLevel level, BloodBoss boss, double radius) {
        AABB box = boss.getBoundingBox().inflate(radius, STUN_DAMAGE_HEIGHT, radius);
        var targets = level.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, box, e -> e != boss && e.isAlive());
        for (var target : targets) {
            double dx = target.getX() - boss.getX();
            double dz = target.getZ() - boss.getZ();
            if (dx * dx + dz * dz > radius * radius) continue;
            boss.applySkillDamage(target, STUN_DAMAGE_MULTIPLIER);
            target.knockback(0.6, -dx, -dz);
        }
    }

    private void spawnSkillParticles(ServerLevel level, BloodBoss owner, int particleTicks, int currentWave) {
        Vec3 center = owner.position();
        float progress = (float)particleTicks / PARTICLE_DURATION;
        
        switch (currentWave) {
            case 1 -> spawnSpiralParticles(level, center, progress, 8.0f, 0.5f);
            case 2 -> spawnRingParticles(level, center, progress, 15.0f);
            case 3 -> spawnBurstParticles(level, center, progress, 25.0f);
            default -> {
                if (currentWave == 0 && abilityTimer < WAVE_START_TIMES[0] - 20) spawnCenterParticles(level, center, progress);
            }
        }
    }

    private void spawnSpiralParticles(ServerLevel level, Vec3 center, float progress, float maxRadius, float height) {
        for (int s = 0; s < 3; s++) {
            float spiralProgress = (progress + (float)s / 3.0f) % 1.0f;
            for (int i = 0; i < 6; i++) {
                float angle = (spiralProgress * 720.0f + (float)i / 6.0f * 360.0f) * (float)Math.PI / 180.0f;
                float radius = maxRadius * spiralProgress;
                level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), center.x + Math.cos(angle) * radius, center.y + height * spiralProgress, center.z + Math.sin(angle) * radius, 1, 0, 0.1, 0, 0.0);
            }
        }
    }

    private void spawnRingParticles(ServerLevel level, Vec3 center, float progress, float maxRadius) {
        float ringRadius = maxRadius * progress;
        for (int i = 0; i < 16; i++) {
            double angle = (double)i / 16.0 * Math.PI * 2.0;
            level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), center.x + Math.cos(angle) * ringRadius, center.y + 1.2 + Math.sin(progress * Math.PI), center.z + Math.sin(angle) * ringRadius, 1, 0, 0.05, 0, 0.0);
        }
    }

    private void spawnBurstParticles(ServerLevel level, Vec3 center, float progress, float maxRadius) {
        for (int b = 0; b < 6; b++) {
            float phi = (float)Math.acos(2.0 * Math.random() - 1.0);
            float theta = (float)(Math.random() * 2.0 * Math.PI);
            float distance = maxRadius * progress;
            level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), center.x + Math.sin(phi) * Math.cos(theta) * distance, center.y + Math.cos(phi) * distance+ 1.2, center.z + Math.sin(phi) * Math.sin(theta) * distance, 1, 0, 0, 0, 0.0);
        }
    }

    private void spawnCenterParticles(ServerLevel level, Vec3 center, float progress) {
        int particles = 4 + (int)(8 * progress);
        for (int i = 0; i < particles; i++) {
            float angle = (float)(Math.random() * 2.0 * Math.PI);
            float distance = 1.5f + 4.0f * progress;
            double startX = center.x + Math.cos(angle) * distance;
            double startZ = center.z + Math.sin(angle) * distance;
            level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), startX, center.y + 1.2, startZ, 1, (center.x - startX) * 0.05, 0.1, (center.z - startZ) * 0.05, 0.0);
        }
    }

    private void spawnTentacles(ServerLevel level, BloodBoss owner, int count, double range) {
        for (int i = 0; i < count; i++) {
            double randomX = owner.getX() + (owner.getRandom().nextDouble() - 0.5) * range * 2;
            double randomZ = owner.getZ() + (owner.getRandom().nextDouble() - 0.5) * range * 2;
            double groundY = findGroundY(owner, level, randomX, randomZ);
            if (groundY > level.getMinBuildHeight()) {
                VoidTentacle voidTentacle = new VoidTentacle(EntityRegistry.BLOOD_TENTACLE.get(), level);
                voidTentacle.setPos(randomX, groundY, randomZ);
                voidTentacle.setOwner(owner);
                level.addFreshEntity(voidTentacle);
            }
        }
    }

    private void spawnGroundShakeWave(ServerLevel level, BloodBoss owner, int currentRadius, int waveNumber) {
        Vec3 center = owner.position();

        if (currentRadius >= 4) {
            float circumference = (float)(2 * Math.PI * currentRadius);
            int points = Math.max(8, (int)(circumference / 2));

            for (int i = 0; i < points; i++) {
                float angle = (360.0f / points) * i * (float)Math.PI / 180.0f;

                double offsetX = currentRadius * Math.cos(angle);
                double offsetZ = currentRadius * Math.sin(angle);

                Vec3 pos = new Vec3(center.x + offsetX, center.y, center.z + offsetZ);
                BlockPos groundPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, pos, 4)).below();

                float baseStrength = 0.3f + waveNumber * 0.1f;
                float distanceFactor = 1.0f - (float)currentRadius / MAX_WAVE_RADIUS;
                float randomFactor = owner.getRandom().nextFloat() * 0.2f;
                float impulseStrength = baseStrength + distanceFactor * 0.3f + randomFactor;

                Utils.createTremorBlock(level, groundPos, impulseStrength);

                if (i % 3 == 0) {
                    double particleX = groundPos.getX() + 0.5;
                    double particleY = groundPos.getY() + 1.0;
                    double particleZ = groundPos.getZ() + 0.5;

                    level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                            particleX, particleY+ 1.2, particleZ, 1,
                            0.0, 0.2 + Math.random() * 0.2, 0.0, 0.0);
                }
            }

            if (currentRadius >= MAX_WAVE_RADIUS - 1) {
                for (int i = 0; i < 5; i++) {
                    float angle = owner.getRandom().nextFloat() * 360.0f * (float)Math.PI / 180.0f;
                    double offsetX = currentRadius * Math.cos(angle);
                    double offsetZ = currentRadius * Math.sin(angle);

                    Vec3 pos = new Vec3(center.x + offsetX, center.y, center.z + offsetZ);
                    BlockPos groundPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, pos, 4)).below();

                    float impulseStrength = 0.6f + waveNumber * 0.1f + owner.getRandom().nextFloat() * 0.3f;
                    Utils.createTremorBlock(level, groundPos, impulseStrength);

                    double particleX = groundPos.getX() + 0.5;
                    double particleY = groundPos.getY() + 1.5;
                    double particleZ = groundPos.getZ() + 0.5;

                    level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                            particleX, particleY, particleZ, 3,
                            0.1, 0.3, 0.1, 0.0);
                }
            }
        }
    }

    private double findGroundY(BloodBoss owner, ServerLevel level, double x, double z) {
        int currentY = Mth.floor(owner.getY());
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos((int)x, currentY, (int)z);

        boolean upDirection = true;
        int searchDistance = 0;

        while (searchDistance < level.getHeight()) {
            if (upDirection) {
                int newY = currentY + searchDistance;
                if (newY < level.getMaxBuildHeight()) {
                    pos.setY(newY);
                    BlockState state = level.getBlockState(pos);
                    if (state.isSolid() && state.isCollisionShapeFullBlock(level, pos)) {
                        return newY + 1.0;
                    }
                }
            } else {
                int newY = currentY - searchDistance;
                if (newY > level.getMinBuildHeight()) {
                    pos.setY(newY);
                    BlockState state = level.getBlockState(pos);
                    if (state.isSolid() && state.isCollisionShapeFullBlock(level, pos)) {
                        return newY + 1.0;
                    }
                }
            }

            upDirection = !upDirection;

            if (!upDirection) {
                searchDistance++;
            }
        }

        return level.getMinBuildHeight();
    }

    @Override protected int getActionTimestamp() { return 20; }
    @Override protected int getActionDuration() { return STUN_DURATION; }
    @Override protected int getCooldown() { return 0; }
    @Override protected String getAnimationId() { return "stiffness_and_emergence_hell_animation"; }
    @Override protected void doAction(BloodBoss entity) {}

    @Override
    protected void stop(@NotNull ServerLevel level, BloodBoss entity, long gameTime) {
        if (abyssalShroudEffectDuration!=0){
            entity.addEffect(new MobEffectInstance(
                    MobEffectRegistry.ABYSSAL_SHROUD.get(), abyssalShroudEffectDuration, 0, false, false, false
            ));
        }

        Brain<BloodBoss> brain = entity.getBrain();
        brain.eraseMemory(MemoryModuleType.IS_EMERGING);
        brain.setMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get(), brain.getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get()).orElse(0) + 1);
        super.stop(level, entity, gameTime);
    }
}