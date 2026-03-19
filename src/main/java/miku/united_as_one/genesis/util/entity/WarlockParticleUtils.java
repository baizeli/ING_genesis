package miku.united_as_one.genesis.util.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class WarlockParticleUtils {

    public static void spawnEmergingParticles(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SOUL,
                    entity.getX(), entity.getY(), entity.getZ(),
                    6, 0.2, 0.1, 0.2, 0.02);
        }
    }

    public static void spawnSoulSphereBlast(Entity entity, float speed) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            int step = 15;
            for (int phi = 0; phi <= 180; phi += step) {
                double p = Math.toRadians(phi);
                for (int theta = 0; theta < 360; theta += step) {
                    double t = Math.toRadians(theta);

                    double dx = Math.sin(p) * Math.cos(t);
                    double dy = Math.cos(p);
                    double dz = Math.sin(p) * Math.sin(t);

                    serverLevel.sendParticles(ParticleTypes.SCULK_SOUL,
                            entity.getX(), entity.getY() + 1.5, entity.getZ(),
                            0, dx, dy, dz, speed);
                }
            }
        }
    }
}