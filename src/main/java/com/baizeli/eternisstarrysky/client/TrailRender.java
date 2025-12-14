package com.baizeli.eternisstarrysky.client;

import com.baizeli.eternisstarrysky.Entity.NyanCat;
import com.baizeli.eternisstarrysky.Entity.NyanCatRenderer;
import com.baizeli.eternisstarrysky.Mixin.minecraft.client.renderer.GameRendererAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class TrailRender {
    public static void renderTrail(float partialTicks, long finishTimeNano, boolean renderLevel) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        
        RenderSystem.backupProjectionMatrix();
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();

        try {
            Camera camera = minecraft.gameRenderer.getMainCamera();
            Vec3 cameraPos = camera.getPosition();

            
            double fov = ((GameRendererAccessor)minecraft.gameRenderer).callGetFov(camera, partialTicks, true);
            Matrix4f projectionMatrix = minecraft.gameRenderer.getProjectionMatrix(fov);
            RenderSystem.setProjectionMatrix(projectionMatrix, VertexSorting.DISTANCE_TO_ORIGIN);

            
            PoseStack viewPoseStack = new PoseStack();
            viewPoseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
            viewPoseStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));

            
            modelViewStack.setIdentity();
            modelViewStack.mulPoseMatrix(viewPoseStack.last().pose());
            RenderSystem.applyModelViewMatrix();

            
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers.bufferSource();

            LevelEntityGetter<Entity> entities = minecraft.level.getEntities();
            Iterable<Entity> all = entities.getAll();
            EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
            
            for (Entity entity :all) {
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
                }

                entityPoseStack.popPose();
            }

            bufferSource.endBatch();

        } finally {
            
            modelViewStack.popPose();
            RenderSystem.restoreProjectionMatrix();
            RenderSystem.applyModelViewMatrix();
        }
    }




}
