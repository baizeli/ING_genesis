package miku.united_as_one.genesis.client.particles;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.api.render.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MagicCircleParticle extends Particle {
    private static final ResourceLocation DEFAULT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Genesis.MODID, "textures/magic_circle.png");

    protected MagicCircleParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setLifetime(500);
    }

    @Override
    public void tick() {
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


    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        Vec3 cameraPos = camera.getPosition();
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(x - cameraPos.x, y + 35 - cameraPos.y, z - cameraPos.z);

        RenderUtils.renderNormalTexturedQuad(poseStack, bufferSource, DEFAULT_TEXTURE, 50, 50, 0, Axis.YP, 0, 0, 0, LightTexture.FULL_BRIGHT);

        bufferSource.endBatch();
        poseStack.popPose();
    }

    public ResourceLocation getTexture() {
        return DEFAULT_TEXTURE;
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
            MagicCircleParticle particle = new MagicCircleParticle(clientLevel, x, y, z);
            return particle;
        }
    }
}
