package miku.united_as_one.genesis.client.particles;

import com.mojang.blaze3d.vertex.*;
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
    private final Vec3 direction;

    public CrescentBladeParticle(ClientLevel level, double x, double y, double z, Vec3 dir, float radius) {
        super(level, x, y, z, dir.x, dir.y, dir.z);
        this.radius = radius;
        this.direction = dir.normalize();
        this.setLifetime(500);  // 寿命，可调
        this.setAlpha(1f);
        this.setColor(1f, 1f, 1f);  // 白色，可做 shader 高光
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();
        // 沿方向推进
        this.x += this.direction.x * 0.5;
        this.y += this.direction.y * 0.5;
        this.z += this.direction.z * 0.5;

        // 可加渐隐效果
        this.alpha *= 0.95f;
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
