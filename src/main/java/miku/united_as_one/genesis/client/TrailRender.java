package miku.united_as_one.genesis.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import miku.united_as_one.genesis.contents.entity.NyanCat;
import miku.united_as_one.genesis.contents.entity.NyanCatRenderer;
import miku.united_as_one.genesis.contents.entity.projectile.ThrownIron;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.UltimateWhisperArrowEntity;
import miku.bai_ze_li.genesis.api.render.particle.GlowCubeParticle;
import miku.bai_ze_li.genesis.api.render.particle.GlowParticleRenderTypes;
import miku.united_as_one.genesis.client.render.cosmic.CosmicBakeModel;
import miku.bai_ze_li.genesis.api.render.effect.SlashEffectManager;
import miku.united_as_one.genesis.client.renderer.projectile.ThrownIronRenderer;
import miku.united_as_one.genesis.client.render.entity.arrow.UltimateWhisperArrowRenderer;
import miku.united_as_one.genesis.mixin.minecraft.client.renderer.GameRendererAccessor;
import miku.united_as_one.genesis.mixin.minecraft.client.renderer.ParticleEngineAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.config.IrisConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.Queue;

public class TrailRender {
    public static boolean IRIS_Setup = ModList.get().isLoaded("oculus");
    private static RenderContext lastContext;

    public static void captureLevelRenderContext(PoseStack poseStack, float partialTick, Camera camera, Matrix4f projectionMatrix) {
        if (!shouldDeferWorldEffects()) {
            lastContext = null;
            CosmicBakeModel.clearDeferredHandItems();
            return;
        }

        CosmicBakeModel.clearDeferredHandItems();
        lastContext = new RenderContext(
                new Matrix4f(poseStack.last().pose()),
                new Matrix4f(projectionMatrix),
                partialTick,
                camera.getPosition()
        );
    }

    public static boolean shouldDeferWorldEffects() {
        if (!IRIS_Setup) {
            return false;
        }

        try {
            IrisConfig irisConfig = Iris.getIrisConfig();
            if (!irisConfig.areShadersEnabled()) {
                return false;
            }
            return IrisApi.getInstance().isShaderPackInUse() || irisConfig.areShadersEnabled();
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void renderTrail(float partialTicks, long finishTimeNano, boolean renderLevel) {
        if (!renderLevel || !shouldDeferWorldEffects()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        RenderSystem.backupProjectionMatrix();
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();

        try {
            Camera camera = minecraft.gameRenderer.getMainCamera();
            RenderContext context = lastContext;
            Vec3 cameraPos = context != null ? context.cameraPos : camera.getPosition();
            Matrix4f projectionMatrix = context != null ? new Matrix4f(context.projection) : createProjectionMatrix(minecraft, camera, partialTicks);
            RenderSystem.setProjectionMatrix(projectionMatrix, VertexSorting.DISTANCE_TO_ORIGIN);

            modelViewStack.setIdentity();
            modelViewStack.mulPoseMatrix(createModelViewMatrix(context, camera));
            RenderSystem.applyModelViewMatrix();

            minecraft.getMainRenderTarget().bindWrite(false);
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers.bufferSource();

            LevelEntityGetter<Entity> entities = minecraft.level.getEntities();
            Iterable<Entity> all = entities.getAll();
            EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();

            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);

            for (Entity entity : all) {
                if (entity == null) continue;

                double x = Mth.lerp(partialTicks, entity.xOld, entity.getX());
                double y = Mth.lerp(partialTicks, entity.yOld, entity.getY());
                double z = Mth.lerp(partialTicks, entity.zOld, entity.getZ());

                double relativeX = x - cameraPos.x;
                double relativeY = y - cameraPos.y;
                double relativeZ = z - cameraPos.z;

                PoseStack entityPoseStack = new PoseStack();
                entityPoseStack.pushPose();

                entityPoseStack.translate(relativeX, relativeY, relativeZ);

                if (entity instanceof NyanCat){
                    EntityRenderer<? super NyanCat> renderer = entityRenderDispatcher.getRenderer(entity);
                    ((NyanCatRenderer)renderer).renderTrail((NyanCat) entity, partialTicks, entityPoseStack, bufferSource, 114514);
                } else if (entity instanceof UltimateWhisperArrowEntity arrow) {
                    UltimateWhisperArrowRenderer.renderTrailOnly(arrow, partialTicks, entityPoseStack, bufferSource);
                } else if (entity instanceof ThrownIron iron) {
                    ThrownIronRenderer.renderTrailOnly(iron, partialTicks, entityPoseStack, bufferSource);
                }

                entityPoseStack.popPose();
            }

            SlashEffectManager.renderDeferred(new PoseStack(), bufferSource, partialTicks);
            bufferSource.endBatch();
            renderGlowCubes(minecraft, camera, partialTicks);
            CosmicBakeModel.flushDeferredHandItems(bufferSource);

        } finally {
            modelViewStack.popPose();
            RenderSystem.restoreProjectionMatrix();
            RenderSystem.applyModelViewMatrix();
            lastContext = null;
        }
    }

    private static Matrix4f createProjectionMatrix(Minecraft minecraft, Camera camera, float partialTicks) {
        double fov = ((GameRendererAccessor)minecraft.gameRenderer).callGetFov(camera, partialTicks, true);
        return minecraft.gameRenderer.getProjectionMatrix(fov);
    }

    private static Matrix4f createModelViewMatrix(RenderContext context, Camera camera) {
        if (context != null) {
            return new Matrix4f(context.modelView);
        }

        PoseStack viewPoseStack = new PoseStack();
        viewPoseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        viewPoseStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));
        return new Matrix4f(viewPoseStack.last().pose());
    }

    private static void renderGlowCubes(Minecraft minecraft, Camera camera, float partialTicks) {
        Map<ParticleRenderType, Queue<Particle>> particles = ((ParticleEngineAccessor) minecraft.particleEngine).getParticles();
        boolean hasGlowCube = false;
        for (Queue<Particle> queue : particles.values()) {
            for (Particle particle : queue) {
                if (particle instanceof GlowCubeParticle) {
                    hasGlowCube = true;
                    break;
                }
            }
            if (hasGlowCube) {
                break;
            }
        }
        if (!hasGlowCube) {
            return;
        }

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        GlowParticleRenderTypes.GLOW_CUBE.begin(bufferBuilder, minecraft.getTextureManager());
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        try {
            for (Queue<Particle> queue : particles.values()) {
                for (Particle particle : queue) {
                    if (particle instanceof GlowCubeParticle glowCube) {
                        glowCube.renderDeferred(bufferBuilder, camera, partialTicks);
                    }
                }
            }
        } finally {
            GlowParticleRenderTypes.GLOW_CUBE.end(tesselator);
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
        }
    }

    private static final class RenderContext {
        private final Matrix4f modelView;
        private final Matrix4f projection;
        private final float partialTick;
        private final Vec3 cameraPos;

        private RenderContext(Matrix4f modelView, Matrix4f projection, float partialTick, Vec3 cameraPos) {
            this.modelView = modelView;
            this.projection = projection;
            this.partialTick = partialTick;
            this.cameraPos = cameraPos;
        }
    }
}
