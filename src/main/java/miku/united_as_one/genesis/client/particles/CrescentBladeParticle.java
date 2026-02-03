package miku.united_as_one.genesis.client.particles;

import com.mojang.blaze3d.vertex.*;
import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrescentBladeParticle extends Particle {
    public final float radius;

    public CrescentBladeParticle(ClientLevel level, double x, double y, double z, Vec3 dir, float radius) {
        super(level, x, y, z, dir.x, dir.y, dir.z);
        this.radius = radius;
        this.setLifetime(500);  // 寿命，可调
        this.setAlpha(1f);
        this.setColor(1f, 1f, 1f);  // 白色，可做 shader 高光
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();



    }

    @Override
    public void render(VertexConsumer buffer, net.minecraft.client.Camera cam, float partialTick) {

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    // 获取粒子在屏幕上的 2D 位置（用于 mask）
    public Vec3 getScreenPos() {
        return new Vec3(this.x, this.y, this.z);
    }
    @OnlyIn(Dist.CLIENT)
    public static class BloodDripTwistParticle extends CrescentBladeParticle {
        public BloodDripTwistParticle(ClientLevel level, double x, double y, double z, Vec3 dir, float radius) {
            super(level, x, y, z, dir, radius);
            this.setLifetime(500);
        }

        @Override
        public void tick() {
            if (this.level.random.nextInt(100) < 30) {
                // 生成平面上半径为0.5的随机偏移向量
                double angle = this.level.random.nextDouble() * 2 * Math.PI; // 随机角度
                double distance = this.level.random.nextDouble() * 0.5; // 随机距离，最大为0.5

                // 计算平面上的偏移坐标
                double offsetX = Math.cos(angle) * distance;
                double offsetZ = Math.sin(angle) * distance;

                // 确保生成位置在平面上（y轴不变）
                this.level.addParticle(
                    ParticleRegistry.BLOOD_DRIP_HANG.get(),
                    this.x + offsetX, this.y, this.z + offsetZ,
                    0.0, 0.0, 0.0
                );
            }
            if (this.age++ >= this.lifetime) {
                this.remove();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class BloodDripTwistProvider implements net.minecraft.client.particle.ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BloodDripTwistProvider(SpriteSet sprites) {
            this.spriteSet = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            Vec3 direction = new Vec3(dx, dy, dz);
            return new BloodDripTwistParticle(level, x, y, z, direction, 0.5f); // 默认半径0.5
        }
    }




    @OnlyIn(Dist.CLIENT)
    public static class Provider implements net.minecraft.client.particle.ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprites) {
            this.spriteSet = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            Vec3 direction = new Vec3(dx, dy, dz);
            return new CrescentBladeParticle(level, x, y, z, direction, 0.5f); // 默认半径0.5
        }
    }
}
