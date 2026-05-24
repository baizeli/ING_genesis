package miku.united_as_one.genesis.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GlowCubeParticle extends Particle {
    private final float cubeSize;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private final float rotationSpeedX;
    private final float rotationSpeedY;
    private final float rotationSpeedZ;

    protected GlowCubeParticle(ClientLevel level, double x, double y, double z, double red, double green, double blue) {
        super(level, x, y, z);
        this.lifetime = 24 + this.random.nextInt(18);
        this.friction = 0.92F;
        this.gravity = 0.035F;
        this.alpha = 1.0F;
        this.cubeSize = 0.07F + this.random.nextFloat() * 0.12F;

        this.rCol = Mth.clamp((float) red, 0.0F, 1.0F);
        this.gCol = Mth.clamp((float) green, 0.0F, 1.0F);
        this.bCol = Mth.clamp((float) blue, 0.0F, 1.0F);
        if (this.rCol + this.gCol + this.bCol <= 0.001F) {
            this.rCol = 1.0F;
            this.gCol = 1.0F;
            this.bCol = 1.0F;
        }

        double angle = this.random.nextDouble() * Math.PI * 2.0D;
        double horizontalSpeed = 0.06D + this.random.nextDouble() * 0.13D;
        this.xd = Math.cos(angle) * horizontalSpeed;
        this.yd = 0.10D + this.random.nextDouble() * 0.16D;
        this.zd = Math.sin(angle) * horizontalSpeed;

        float fullRotation = (float) (Math.PI * 2.0D);
        this.rotationX = this.random.nextFloat() * fullRotation;
        this.rotationY = this.random.nextFloat() * fullRotation;
        this.rotationZ = this.random.nextFloat() * fullRotation;
        this.rotationSpeedX = (this.random.nextFloat() - 0.5F) * 0.18F;
        this.rotationSpeedY = (this.random.nextFloat() - 0.5F) * 0.18F;
        this.rotationSpeedZ = (this.random.nextFloat() - 0.5F) * 0.18F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            remove();
            return;
        }

        float progress = (float) this.age / (float) this.lifetime;
        if (progress > 0.55F) {
            this.alpha = 1.0F - (progress - 0.55F) / 0.45F;
        }

        this.rotationX += this.rotationSpeedX;
        this.rotationY += this.rotationSpeedY;
        this.rotationZ += this.rotationSpeedZ;

        this.xd *= this.friction;
        this.yd = (this.yd - this.gravity) * this.friction;
        this.zd *= this.friction;
        move(this.xd, this.yd, this.zd);

        if (this.onGround) {
            this.xd *= 0.45D;
            this.zd *= 0.45D;
            this.yd = 0.0D;
        }

        if (this.alpha <= 0.001F) {
            remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vector3f cameraPos = camera.getPosition().toVector3f();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());
        Quaternionf rotation = new Quaternionf().rotateX(this.rotationX).rotateY(this.rotationY).rotateZ(this.rotationZ);
        renderCube(buffer, x, y, z, this.cubeSize, rotation);
    }

    private void renderCube(VertexConsumer buffer, float x, float y, float z, float size, Quaternionf rotation) {
        float halfSize = size / 2.0F;
        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-halfSize, -halfSize, -halfSize),
                new Vector3f(halfSize, -halfSize, -halfSize),
                new Vector3f(halfSize, halfSize, -halfSize),
                new Vector3f(-halfSize, halfSize, -halfSize),
                new Vector3f(-halfSize, -halfSize, halfSize),
                new Vector3f(halfSize, -halfSize, halfSize),
                new Vector3f(halfSize, halfSize, halfSize),
                new Vector3f(-halfSize, halfSize, halfSize)
        };

        for (Vector3f vertex : vertices) {
            vertex.rotate(rotation);
            vertex.add(x, y, z);
        }

        int light = 15728880;
        addQuad(buffer, vertices[4], vertices[5], vertices[6], vertices[7], light);
        addQuad(buffer, vertices[1], vertices[0], vertices[3], vertices[2], light);
        addQuad(buffer, vertices[5], vertices[1], vertices[2], vertices[6], light);
        addQuad(buffer, vertices[0], vertices[4], vertices[7], vertices[3], light);
        addQuad(buffer, vertices[3], vertices[7], vertices[6], vertices[2], light);
        addQuad(buffer, vertices[4], vertices[0], vertices[1], vertices[5], light);
    }

    private void addQuad(VertexConsumer buffer, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, int light) {
        buffer.vertex(v1.x(), v1.y(), v1.z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(v2.x(), v2.y(), v2.z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(v3.x(), v3.y(), v3.z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(v4.x(), v4.y(), v4.z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return GlowParticleRenderTypes.GLOW_CUBE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider(SpriteSet sprites) {
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double red, double green, double blue) {
            return new GlowCubeParticle(level, x, y, z, red, green, blue);
        }
    }
}
