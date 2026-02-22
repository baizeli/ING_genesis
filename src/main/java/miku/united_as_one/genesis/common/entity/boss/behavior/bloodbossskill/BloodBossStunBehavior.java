package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import com.google.common.collect.ImmutableMap;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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

    // stun 范围伤害倍率
    private static final float STUN_DAMAGE_MULTIPLIER = 1.8f;

    // 垂直判定高度
    private static final double STUN_DAMAGE_HEIGHT = 3.0;


    public BloodBossStunBehavior() {
        super(ImmutableMap.of(
                MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        Brain<BloodBoss> brain = entity.getBrain();
        Integer stunCount = brain.getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get()).orElse(0);
        if (stunCount>=1){
            return false;
        }
        if (stunCount==0){
            float health = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            return health <= maxHealth * 0.7;
        }

        return stunCount > 0;
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss owner, long gameTime) {
        Brain<BloodBoss> brain = owner.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        super.tick(level, owner, gameTime);

        
        for (int i = 0; i < WAVE_START_TIMES.length; i++) {
            if (abilityTimer == WAVE_START_TIMES[i] && currentWave != i + 1) {

                currentWave = i + 1;
                waveTimer = 0;
                waveRadius = 0;
                particleTicks = 0;

                spawnTentacles(level, owner, 10 * (i + 1));

                int[] waveDamageRadius = {8, 14, 22};
                applyStunAreaDamage(level, owner, waveDamageRadius[i]);

                break;
            }

        }

        
        if (currentWave > 0 && waveTimer < WAVE_DURATION) {
            waveTimer++;
            int[] waveMaxRadii = {16, 32, 64};
            int currentMaxRadius = waveMaxRadii[currentWave - 1];
            waveRadius = (int)((float)waveTimer / WAVE_DURATION * currentMaxRadius);

            
            spawnGroundShakeWave(level, owner, waveRadius, currentWave);

            
            float shakeIntensity = 1.0f + (float)currentWave * 0.5f;
            float shakeFrequency = 15 + MAX_WAVE_RADIUS;
            CameraShakeManager.addCameraShake(new CameraShakeData(
                    level,
                    (int)shakeFrequency,
                    owner.position(),
                    MAX_WAVE_RADIUS * shakeIntensity
            ));

            
            if (waveTimer >= WAVE_DURATION) {
                currentWave = 0;
                waveTimer = 0;
                waveRadius = 0;
            }
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

        var targets = level.getEntitiesOfClass(
                net.minecraft.world.entity.LivingEntity.class,
                box,
                e -> e != boss && e.isAlive()
        );

        for (var target : targets) {

            // 距离过滤（圆形判定）
            double dx = target.getX() - boss.getX();
            double dz = target.getZ() - boss.getZ();

            if (dx * dx + dz * dz > radius * radius) continue;

            // 技能伤害
            boss.applySkillDamage(target, STUN_DAMAGE_MULTIPLIER);

            // 轻微震飞
            target.knockback(0.6, -dx, -dz);
        }
    }

    
    private void spawnSkillParticles(ServerLevel level, BloodBoss owner, int particleTicks, int currentWave) {
        Vec3 center = owner.position();
        float progress = (float)particleTicks / PARTICLE_DURATION;

        
        switch (currentWave) {
            case 1:
                
                spawnSpiralParticles(level, center, progress, 10.0f, 0.5f);
                break;
            case 2:
                
                spawnRingParticles(level, center, progress, 20.0f);
                break;
            case 3:
                
                spawnBurstParticles(level, center, progress, 30.0f);
                break;
            default:
                
                if (currentWave == 0 && abilityTimer < WAVE_START_TIMES[0] - 20) {
                    spawnCenterParticles(level, center, progress);
                }
                break;
        }
    }

    
    private void spawnSpiralParticles(ServerLevel level, Vec3 center, float progress, float maxRadius, float height) {
        int spiralPoints = 5; 
        int pointsPerSpiral = 8; 

        for (int s = 0; s < spiralPoints; s++) {
            float spiralOffset = (float)s / spiralPoints * 360.0f;
            float spiralProgress = (progress + spiralOffset / 360.0f) % 1.0f;

            for (int i = 0; i < pointsPerSpiral; i++) {
                float pointProgress = (float)i / pointsPerSpiral;
                float angle = (spiralProgress * 720.0f + pointProgress * 360.0f) * (float)Math.PI / 180.0f;
                float radius = maxRadius * spiralProgress;

                double x = center.x + Math.cos(angle) * radius;
                double y = center.y + height * spiralProgress;
                double z = center.z + Math.sin(angle) * radius;

                
                double dx = -Math.sin(angle) * 0.1;
                double dy = 0.1;
                double dz = Math.cos(angle) * 0.1;

                level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), x, y, z, 1, dx, dy, dz, 0.0);
            }
        }
    }

    
    private void spawnRingParticles(ServerLevel level, Vec3 center, float progress, float maxRadius) {
        int rings = 3; 
        int particlesPerRing = 12; 

        for (int r = 0; r < rings; r++) {
            float ringRadius = maxRadius * (progress + (float)r / rings);
            if (ringRadius > maxRadius) continue;

            for (int i = 0; i < particlesPerRing; i++) {
                float angle = (float)i / particlesPerRing * 360.0f * (float)Math.PI / 180.0f;

                double x = center.x + Math.cos(angle) * ringRadius;
                double y = center.y + 0.5 + Math.sin(progress * Math.PI) * 2.0;
                double z = center.z + Math.sin(angle) * ringRadius;

                
                double dx = Math.cos(angle) * 0.05;
                double dy = 0.05;
                double dz = Math.sin(angle) * 0.05;

                level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), x, y, z, 1, dx, dy, dz, 0.0);
            }
        }
    }

    
    private void spawnBurstParticles(ServerLevel level, Vec3 center, float progress, float maxRadius) {
        int burstCount = 8; 

        for (int b = 0; b < burstCount; b++) {
            
            float phi = (float)Math.acos(2.0 * Math.random() - 1.0);
            float theta = (float)(Math.random() * 2.0 * Math.PI);
            float distance = maxRadius * progress * (0.5f + (float)Math.random() * 0.5f);

            double x = center.x + Math.sin(phi) * Math.cos(theta) * distance;
            double y = center.y + Math.cos(phi) * distance;
            double z = center.z + Math.sin(phi) * Math.sin(theta) * distance;

            
            double dx = (x - center.x) * 0.1;
            double dy = (y - center.y) * 0.1;
            double dz = (z - center.z) * 0.1;

            int particleCount = 1 + (int)(3 * progress);
            for (int p = 0; p < particleCount; p++) {
                double offsetX = (Math.random() - 0.5) * 0.5;
                double offsetY = (Math.random() - 0.5) * 0.5;
                double offsetZ = (Math.random() - 0.5) * 0.5;

                level.sendParticles( ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                        x + offsetX, y + offsetY, z + offsetZ,
                        1, dx * 0.5, dy * 0.5, dz * 0.5, 0.0);
            }
        }
    }

    
    private void spawnCenterParticles(ServerLevel level, Vec3 center, float progress) {
        int particles = 5 + (int)(15 * progress);

        for (int i = 0; i < particles; i++) {
            
            float angle = (float)(Math.random() * 2.0 * Math.PI);
            float distance = 2.0f + 8.0f * progress;
            float heightVariation = (float)Math.sin(progress * Math.PI * 2.0 + i * 0.5) * 1.0f;

            double startX = center.x + Math.cos(angle) * distance;
            double startY = center.y + 1.0 + heightVariation;
            double startZ = center.z + Math.sin(angle) * distance;

            
            double dx = (center.x - startX) * 0.05;
            double dy = (center.y + 0.5 - startY) * 0.05;
            double dz = (center.z - startZ) * 0.05;

            level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                    startX, startY, startZ, 1, dx, dy, dz, 0.0);
        }
    }

    
    private void spawnTentacles(ServerLevel level, BloodBoss owner, int count) {
        
        for (int i = 0; i < count; i++) {
            spawnVoidTentacle(level, owner);
        }
    }

    
    private void spawnVoidTentacle(ServerLevel level, BloodBoss owner) {
        double randomX = owner.getX() + (owner.getRandom().nextDouble() - 0.5) * 64.0;
        double randomZ = owner.getZ() + (owner.getRandom().nextDouble() - 0.5) * 64.0;

        double groundY = findGroundY(owner, level, randomX, randomZ);

        if (groundY > level.getMinBuildHeight()) {
            VoidTentacle voidTentacle = new VoidTentacle(EntityRegistry.BLOOD_TENTACLE.get(), level);
            voidTentacle.setPos(randomX, groundY, randomZ);
            voidTentacle.setOwner(owner);
            level.addFreshEntity(voidTentacle);

            
            for (int i = 0; i < 10; i++) {
                double offsetX = (Math.random() - 0.5) * 2.0;
                double offsetY = Math.random() * 3.0;
                double offsetZ = (Math.random() - 0.5) * 2.0;

                level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                        randomX + offsetX, groundY + offsetY, randomZ + offsetZ,
                        1, 0.0, 0.1, 0.0, 0.0);
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
                            particleX, particleY, particleZ, 1,
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

    @Override
    protected int getActionTimestamp() {
        return 20;
    }

    @Override
    protected int getActionDuration() {
        return STUN_DURATION;
    }

    @Override
    protected int getCooldown() {
        return 0;
    }

    @Override
    protected String getAnimationId() {
        return "stiffness_and_emergence_hell_animation";
    }

    @Override
    protected void doAction(BloodBoss entity) {
    }

    @Override
    protected void stop(@NotNull ServerLevel level, BloodBoss entity, long gameTime) {
        Brain<BloodBoss> brain = entity.getBrain();
        brain.eraseMemory(MemoryModuleType.IS_EMERGING);
        brain.setMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get(), brain.getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get()).orElse(0) + 1);

        
        Vec3 center = entity.position();
        for (int i = 0; i < 50; i++) {
            double angle = Math.random() * 2.0 * Math.PI;
            double distance = Math.random() * 10.0;
            double height = Math.random() * 3.0;

            double x = center.x + Math.cos(angle) * distance;
            double y = center.y + height;
            double z = center.z + Math.sin(angle) * distance;

            double dx = (center.x - x) * 0.05;
            double dy = 0.1;
            double dz = (center.z - z) * 0.05;

            level.sendParticles(ParticleRegistry.BLOOD_DRIP_TWIST.get(), x, y, z, 1, dx, dy, dz, 0.0);
        }

        super.stop(level, entity, gameTime);
    }
}