package miku.united_as_one.genesis.client.particles;

import miku.united_as_one.genesis.Genesis;
import miku.bai_ze_li.genesis.api.render.shader.GenesisShaderInstance;
import miku.bai_ze_li.genesis.api.render.shader.GenesisShaders;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;

public class TestAParticle extends Particle {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "textures/particle/trail.png");

    public final Vec3[] trailPositions = new Vec3[64];
    public int trailPointer = -1;
    public float trailA = 1.0F;
    private float gravity;

    protected TestAParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setLifetime(500);
    }

    @Override
    public void tick() {
        this.trail();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.yd -= (double)this.gravity;
        }
    }

    public void trail() {
        Vec3 trailAt = new Vec3(this.x, this.y, this.z);
        if (this.trailPointer == -1) {
            Arrays.fill(this.trailPositions, trailAt);
        }

        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }

        this.trailPositions[this.trailPointer] = trailAt;
    }


    private static final RenderType SHADER_RENDER_TYPE = RenderType.create(
            "shader_test_a",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> GenesisShaders.getFloridShader()))
                    .setTextureState(new RenderStateShard.TextureStateShard(TEXTURE, false, false))
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)

    );
    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        if (this.removed || this.trailPointer <= -1) {
            return;
        }
        GenesisShaderInstance shader = (GenesisShaderInstance) GenesisShaders.getFloridShader();
        if (shader == null) {
            return; 
        }


        shader.setTime(level.getGameTime()/20f);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer consumer = bufferSource.getBuffer(SHADER_RENDER_TYPE);
        Vec3 cameraPos = camera.getPosition();
        double x = Mth.lerp(partialTick, this.xo, this.x);
        double y = Mth.lerp(partialTick, this.yo, this.y);
        double z = Mth.lerp(partialTick, this.zo, this.z);
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        Vec3 drawFrom = new Vec3(x, y, z);
        float zRot = this.getCameraRot(camera);
        Vec3 topAngleVec = (new Vec3(0.0, this.getTrailHeight() / 2.0D, 0.0)).zRot(zRot);
        Vec3 bottomAngleVec = (new Vec3(0.0, this.getTrailHeight() / -2.0D, 0.0)).zRot(zRot);

        for (int samples = 0; samples < this.sampleSize(); samples++) {
            Vec3 sample = this.getTrailPosition(samples * this.sampleStep(), partialTick);
            float u1 = (float) samples / (float) this.sampleSize();
            float u2 = u1 + 1.0F / (float) this.sampleSize();
            PoseStack.Pose last = poseStack.last();
            Matrix4f matrix4f = last.pose();
            Matrix3f matrix3f = last.normal();

            consumer.vertex(matrix4f, (float)drawFrom.x + (float)bottomAngleVec.x,
                            (float)drawFrom.y + (float)bottomAngleVec.y,
                            (float)drawFrom.z + (float)bottomAngleVec.z)
                    .color(this.rCol, this.gCol, this.bCol, this.trailA)
                    .uv(u1, 1.0F)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(this.getLightColor(partialTick))
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();

            consumer.vertex(matrix4f, (float)sample.x + (float)bottomAngleVec.x,
                            (float)sample.y + (float)bottomAngleVec.y,
                            (float)sample.z + (float)bottomAngleVec.z)
                    .color(this.rCol, this.gCol, this.bCol, this.trailA)
                    .uv(u2, 1.0F)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(this.getLightColor(partialTick))
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();

            consumer.vertex(matrix4f, (float)sample.x + (float)topAngleVec.x,
                            (float)sample.y + (float)topAngleVec.y,
                            (float)sample.z + (float)topAngleVec.z)
                    .color(this.rCol, this.gCol, this.bCol, this.trailA)
                    .uv(u2, 0.0F)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(this.getLightColor(partialTick))
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();

            consumer.vertex(matrix4f, (float)drawFrom.x + (float)topAngleVec.x,
                            (float)drawFrom.y + (float)topAngleVec.y,
                            (float)drawFrom.z + (float)topAngleVec.z)
                    .color(this.rCol, this.gCol, this.bCol, this.trailA)
                    .uv(u1, 0.0F)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(this.getLightColor(partialTick))
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();

            drawFrom = sample;
        }

        bufferSource.endBatch();
        poseStack.popPose();
    }

    public float getCameraRot(Camera camera) {
        return (float) (-(Math.PI / 180.0F) * camera.getXRot());
    }

    public float getTrailHeight() {
        return 0.5F;
    }

    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    public int sampleSize() {
        return 10; 
    }

    public int sampleStep() {
        return 3; 
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.removed) {
            partialTick = 1.0F;
        }

        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprites) {
            this.spriteSet = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TestAParticle particle = new TestAParticle(clientLevel, x, y, z);
            particle.xd = xSpeed;
            particle.yd = ySpeed;
            particle.zd = zSpeed;
            return particle;
        }
    }
}
