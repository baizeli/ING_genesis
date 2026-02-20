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

public class BloodBossEmergingBehavior extends AnimatedActionBehavior<BloodBoss> {
    public static final int EMERGE_SPAWN_DURATION = 175;

    
    private int emergenceTick = 0;

    
    public BloodBossEmergingBehavior() {
        super(ImmutableMap.of(MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT));
    }


    @Override
    protected boolean canStartAction(BloodBoss entity) {
        return entity.getBrain()
                .getMemory(ModMemoryModuleType.BOSS_STAGE.get())
                .orElse(0) == 0;
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);
        emergenceTick++;
        if (emergenceTick <= 10) {

            spawnSphericalTwistParticles(level, boss);
        }

        if (emergenceTick > 20) {

            boss.setVisable(true);
        }
        if (emergenceTick >= 20 && emergenceTick <= 150) { 
            
            spawnFootRingParticles(level, boss);

            
            spawnFallingDripParticles(level, boss);

            
            if (emergenceTick % 3 == 0) { 
                spawnBodySpiralParticles(level, boss);
            }
        }
    }



    
    private void spawnSphericalTwistParticles(ServerLevel level, BloodBoss boss) {
        double yawRad = Math.toRadians(boss.getYRot());
        double behindX = boss.getX() + Math.sin(yawRad) * 5.0;
        double behindZ = boss.getZ() - Math.cos(yawRad) * 5.0;
        double behindY = boss.getY() + 1.8;

        double cx = behindX;
        double cy = behindY;
        double cz = behindZ;
        double radius = 2.5;
        int particleCount = 200;

        for (int i = 0; i < particleCount; i++) {
            double phi = Math.acos(2 * level.random.nextDouble() - 1);
            double theta = 2 * Math.PI * level.random.nextDouble();
            double x = cx + radius * Math.sin(phi) * Math.cos(theta);
            double y = cy + radius * Math.cos(phi);
            double z = cz + radius * Math.sin(phi) * Math.sin(theta);
            level.sendParticles(
                    ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                    x, y, z,
                    1, 0.0, 0.0, 0.0, 0.0
            );
        }
    }

    
    private void spawnFootRingParticles(ServerLevel level, BloodBoss boss) {
        double centerX = boss.getX();
        double centerY = boss.getY(); 
        double centerZ = boss.getZ();
        double radius = 2.5; 
        int points = 40; 

        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2 / points) * i;
            
            double jitter = 0.2;
            double xOffset = (level.random.nextDouble() - 0.5) * jitter;
            double zOffset = (level.random.nextDouble() - 0.5) * jitter;

            double x = centerX + Math.cos(angle) * radius + xOffset;
            double z = centerZ + Math.sin(angle) * radius + zOffset;

            level.sendParticles(
                    ParticleRegistry.BLOOD_DRIP_LAND.get(), 
                    x, centerY, z,
                    1, 0.0, 0.0, 0.0, 0.0
            );
        }
    }

    
    private void spawnFallingDripParticles(ServerLevel level, BloodBoss boss) {
        double centerX = boss.getX();
        double centerY = boss.getY() + boss.getBbHeight(); 
        double centerZ = boss.getZ();
        int count = 15; 

        for (int i = 0; i < count; i++) {
            
            double halfWidth = boss.getBbWidth() / 2.0;
            double x = centerX + (level.random.nextDouble() - 0.5) * halfWidth * 3;
            double z = centerZ + (level.random.nextDouble() - 0.5) * halfWidth * 3;
            
            double y = centerY + 2.0 + level.random.nextDouble() * 2.0;

            level.sendParticles(
                    ParticleRegistry.BLOOD_DRIP_FALL.get(),
                    x, y, z,
                    1,
                    0.0, 
                    -0.3, 
                    0.0, 
                    0.0
            );
        }
    }

    
    private void spawnBodySpiralParticles(ServerLevel level, BloodBoss boss) {
        double centerX = boss.getX();
        double centerY = boss.getY() + boss.getBbHeight() / 2.0; 
        double centerZ = boss.getZ();

        
        double radius = 1.5 + boss.getBbWidth() / 2.0; 
        double height = boss.getBbHeight() * 0.8; 
        int spirals = 2; 
        int pointsPerSpiral = 12; 

        double time = emergenceTick * 0.1; 
        double yOffset = (emergenceTick % 40) * 0.02; 

        for (int s = 0; s < spirals; s++) {
            for (int p = 0; p < pointsPerSpiral; p++) {
                double progress = (double)p / pointsPerSpiral;
                double angle = time + (Math.PI * 2 * progress) + (s * Math.PI); 

                double x = centerX + Math.cos(angle) * radius;
                double z = centerZ + Math.sin(angle) * radius;
                double y = centerY - (height / 2) + (progress * height) + yOffset;

                
                double dx = (centerX - x) * 0.02;
                double dz = (centerZ - z) * 0.02;

                level.sendParticles(
                        ParticleRegistry.BLOOD_DRIP_HANG.get(),
                        x, y, z,
                        1,
                        dx, 0.01, dz, 
                        0.0
                );
            }
        }
    }
    @Override
    protected void stop(ServerLevel level, BloodBoss entity, long gameTime) {
        
        emergenceTick = 0;
        Brain<BloodBoss> brain = entity.getBrain();
        brain.eraseMemory(MemoryModuleType.IS_EMERGING);
        brain.setMemory(ModMemoryModuleType.BOSS_STAGE.get(), 1);
        super.stop(level, entity, gameTime);
    }

    @Override
    protected int getActionTimestamp() {
        return 5;
    }

    @Override
    protected int getActionDuration() {
        return EMERGE_SPAWN_DURATION; 
    }

    @Override
    protected int getCooldown() {
        return 0;
    }


    @Override
    protected String getAnimationId() {
        return "spawn_animation";
    }

    @Override
    protected void doAction(BloodBoss entity) {
        
    }
}