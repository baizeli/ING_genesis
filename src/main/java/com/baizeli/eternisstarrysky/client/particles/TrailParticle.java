package com.baizeli.eternisstarrysky.client.particles;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;


public abstract class TrailParticle extends Particle {
    
    protected final Vec3[] trailPositions = new Vec3[64];
    protected int trailPointer = -1;
    protected float trailAlpha = 1.0F;
    protected final float trailWidth;
    protected final float trailHeight;
    protected final int maxSamples;
    protected final int sampleStep;
    protected final int ownerId;
    protected final boolean followEntity;
    protected Vec3 originPosition;

    public TrailParticle(ClientLevel world, double x, double y, double z,
                         double xSpeed, double ySpeed, double zSpeed,
                         float red, float green, float blue, float alpha,
                         float trailWidth, float trailHeight, int lifetime,
                         int maxSamples, int sampleStep) {
        this(world, x, y, z, xSpeed, ySpeed, zSpeed, red, green, blue, alpha, trailWidth, trailHeight, lifetime, maxSamples, sampleStep, -1);
    }

    public TrailParticle(ClientLevel world, double x, double y, double z,
                         double xSpeed, double ySpeed, double zSpeed,
                         float red, float green, float blue, float alpha,
                         float trailWidth, float trailHeight, int lifetime,
                         int maxSamples, int sampleStep, int ownerId) {
        super(world, x, y, z);

        
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.alpha = alpha;

        
        this.trailWidth = trailWidth;
        this.trailHeight = trailHeight;
        this.maxSamples = maxSamples;
        this.sampleStep = sampleStep;
        this.lifetime = lifetime;
        this.gravity = 0.0F; 
        this.ownerId = ownerId;
        this.followEntity = ownerId > 0;
        this.originPosition = new Vec3(x, y, z);

        
        this.setSize(0.2F, 0.2F);
    }

    @Override
    public void tick() {
        
        this.updatePosition();

        
        this.recordTrailPosition();

        
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        
        this.applyPhysics();

        
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
        }

        
        this.updateTrailAlpha();
    }

    
    protected void updatePosition() {
        if (this.followEntity) {
            Entity entity = this.level.getEntity(this.ownerId);
            if (entity == null) {
                this.remove();
                return;
            }
            Vec3 entityPos = entity.position();
            this.x = entityPos.x;
            this.y = entityPos.y;
            this.z = entityPos.z;
        }
    }

    
    protected void applyPhysics() {
        this.xd *= 0.98D; 
        this.yd *= 0.98D;
        this.zd *= 0.98D;
        this.yd -= (double) this.gravity; 
    }

    
    protected void updateTrailAlpha() {
        this.trailAlpha = 1.0F - (float) this.age / (float) this.lifetime;
    }

    
    protected void recordTrailPosition() {
        Vec3 currentPos = new Vec3(this.x, this.y, this.z);

        
        if (this.trailPointer == -1) {
            Arrays.fill(this.trailPositions, currentPos);
            this.trailPointer = 0;
            return;
        }

        
        if (++this.trailPointer >= this.trailPositions.length) {
            this.trailPointer = 0;
        }

        this.trailPositions[this.trailPointer] = currentPos;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        if (this.trailPointer < 0) {
            return; 
        }

        
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(this.getTexture()));

        
        double interpolatedX = Mth.lerp(partialTick, this.xo, this.x);
        double interpolatedY = Mth.lerp(partialTick, this.yo, this.y);
        double interpolatedZ = Mth.lerp(partialTick, this.zo, this.z);

        
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        Vec3 cameraPos = camera.getPosition();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        
        float cameraRotation = this.calculateCameraRotation(camera);

        
        Vec3 topOffset = new Vec3(0.0, this.trailHeight / 2.0, 0.0).yRot(cameraRotation);
        Vec3 bottomOffset = new Vec3(0.0, -this.trailHeight / 2.0, 0.0).yRot(cameraRotation);

        
        this.buildTrailMesh(vertexConsumer, poseStack,
                interpolatedX, interpolatedY, interpolatedZ,
                topOffset, bottomOffset, partialTick);

        
        bufferSource.endBatch();
        poseStack.popPose();
    }

    
    protected void buildTrailMesh(VertexConsumer consumer, PoseStack poseStack,
                                  double startX, double startY, double startZ,
                                  Vec3 topOffset, Vec3 bottomOffset, float partialTick) {
        Vec3 currentSegmentStart = new Vec3(startX, startY, startZ);
        int samples = 0;

        while (samples < this.maxSamples) {
            
            Vec3 segmentEnd = this.getTrailPosition(samples * this.sampleStep, partialTick);

            if (segmentEnd == null) break;

            
            float uStart = (float) samples / (float) this.maxSamples;
            float uEnd = uStart + 1.0F / (float) this.maxSamples;

            
            PoseStack.Pose pose = poseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();

            
            this.buildTrailQuad(consumer, poseMatrix, normalMatrix,
                    currentSegmentStart, segmentEnd,
                    topOffset, bottomOffset, uStart, uEnd);

            
            currentSegmentStart = segmentEnd;
            samples++;
        }
    }

    
    protected void buildTrailQuad(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                  Vec3 start, Vec3 end,
                                  Vec3 topOffset, Vec3 bottomOffset,
                                  float uStart, float uEnd) {
        
        consumer.vertex(poseMatrix,
                        (float) (start.x + bottomOffset.x),
                        (float) (start.y + bottomOffset.y),
                        (float) (start.z + bottomOffset.z))
                .color(this.rCol, this.gCol, this.bCol, this.trailAlpha * this.alpha)
                .uv(uStart, 1.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(this.getLightColor(0))
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();

        
        consumer.vertex(poseMatrix,
                        (float) (end.x + bottomOffset.x),
                        (float) (end.y + bottomOffset.y),
                        (float) (end.z + bottomOffset.z))
                .color(this.rCol, this.gCol, this.bCol, this.trailAlpha * this.alpha)
                .uv(uEnd, 1.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(this.getLightColor(0))
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();

        
        consumer.vertex(poseMatrix,
                        (float) (end.x + topOffset.x),
                        (float) (end.y + topOffset.y),
                        (float) (end.z + topOffset.z))
                .color(this.rCol, this.gCol, this.bCol, this.trailAlpha * this.alpha)
                .uv(uEnd, 0.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(this.getLightColor(0))
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();

        
        consumer.vertex(poseMatrix,
                        (float) (start.x + topOffset.x),
                        (float) (start.y + topOffset.y),
                        (float) (start.z + topOffset.z))
                .color(this.rCol, this.gCol, this.bCol, this.trailAlpha * this.alpha)
                .uv(uStart, 0.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(this.getLightColor(0))
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    
    protected Vec3 getTrailPosition(int stepsBack, float partialTick) {
        if (this.trailPointer < 0 || stepsBack >= this.trailPositions.length) {
            return null;
        }

        
        int currentIndex = this.trailPointer;
        int targetIndex = (currentIndex - stepsBack) % this.trailPositions.length;
        if (targetIndex < 0) targetIndex += this.trailPositions.length;

        int prevIndex = (targetIndex - 1) % this.trailPositions.length;
        if (prevIndex < 0) prevIndex += this.trailPositions.length;

        
        Vec3 previousPos = this.trailPositions[prevIndex];
        Vec3 targetPos = this.trailPositions[targetIndex];

        
        Vec3 direction = targetPos.subtract(previousPos);
        return previousPos.add(direction.scale(partialTick));
    }

    
    protected float calculateCameraRotation(Camera camera) {
        return (float) (-(Math.PI / 180.0) * camera.getXRot());
    }

    
    protected abstract ResourceLocation getTexture();

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public boolean shouldCull() {
        return false; 
    }

    

    
    public static abstract class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprites) {
            this.spriteSet = sprites;
        }

        @Override
        public abstract Particle createParticle(SimpleParticleType type, ClientLevel world,
                                                double x, double y, double z,
                                                double xSpeed, double ySpeed, double zSpeed);
    }
}