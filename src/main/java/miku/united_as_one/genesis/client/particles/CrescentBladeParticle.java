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
        private final float rotationSpeed;
        private float currentRotation = 0.0f;

        public BloodDripTwistParticle(ClientLevel level, double x, double y, double z, Vec3 dir, float radius) {
            super(level, x, y, z, dir, radius);
            this.setLifetime(500);

            // 设置速度参数
            this.xd = dir.x;
            this.yd = dir.y;
            this.zd = dir.z;

            // 设置旋转速度（基于速度大小）
            float speed = (float) dir.length();
            this.rotationSpeed = speed * 20.0f; // 旋转速度与运动速度成正比

            // 禁用物理和重力
            this.hasPhysics = false;
            this.gravity = 0.0f; // 设置重力为0
            this.lifetime=150;
        }

        @Override
        public void tick() {


            // 保存旧位置（用于渲染插值）
            this.xo = this.x;
            this.yo = this.y;
            this.zo = this.z;

            // 应用速度更新位置
            this.x += this.xd;
            this.y += this.yd;
            this.z += this.zd;

            // 更新旋转角度
            this.currentRotation += this.rotationSpeed;
            if (this.currentRotation >= 360.0f) {
                this.currentRotation -= 360.0f;
            }

            // 添加一些阻力效果，使粒子逐渐减速（可选）
            this.xd *= 0.98;
            this.yd *= 0.98;
            this.zd *= 0.98;



            // 在移动过程中生成子粒子的逻辑
            if (this.level.random.nextInt(100) < 30) {
                // 根据当前运动方向计算生成位置
                double angle = this.level.random.nextDouble() * 2 * Math.PI;
                double distance = this.level.random.nextDouble() * 0.5;

                // 计算平面上的偏移坐标（基于当前旋转角度）
                double cosAngle = Math.cos(angle + this.currentRotation * 0.0174533f);
                double sinAngle = Math.sin(angle + this.currentRotation * 0.0174533f);
                double offsetX = cosAngle * distance;
                double offsetZ = sinAngle * distance;

                this.level.addParticle(
                        ParticleRegistry.BLOOD_DRIP_HANG.get(),
                        this.x + offsetX, this.y, this.z + offsetZ,
                        0, 0, 0
                );
            }

            // 粒子生命周期管理
            if (this.age++ >= this.lifetime) {
                // 在粒子消失前生成一些尾迹粒子
                for (int i = 0; i < 5; i++) {
                    double trailX = this.x + (this.level.random.nextDouble() - 0.5) * 0.5;
                    double trailY = this.y + (this.level.random.nextDouble() - 0.5) * 0.5;
                    double trailZ = this.z + (this.level.random.nextDouble() - 0.5) * 0.5;
                    double trailSpeed = 0.02 + this.level.random.nextDouble() * 0.03;

                    this.level.addParticle(
                            ParticleRegistry.BLOOD_DRIP_HANG.get(),
                            trailX, trailY, trailZ,
                            this.xd * trailSpeed, this.yd * trailSpeed, this.zd * trailSpeed
                    );
                }
                this.remove();
            }

        }

        // 可选：添加一个方法来获取当前旋转角度，可以在渲染时使用
        public float getCurrentRotation() {
            return this.currentRotation;
        }

        // 可选：添加一个方法来设置旋转速度
        public void setRotationSpeed(float rotationSpeed) {
            // 由于 rotationSpeed 是 final，我们不能直接修改它
            // 如果需要动态调整旋转速度，需要修改类的设计
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
