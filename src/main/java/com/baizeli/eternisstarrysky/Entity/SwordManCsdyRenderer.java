package com.baizeli.eternisstarrysky.entity;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.render.FFRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class SwordManCsdyRenderer extends GeoEntityRenderer<SwordManCsdy> {
    private final ResourceLocation TRAIL_TEXTURE = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "textures/particle/stardust_trail.png");

    public SwordManCsdyRenderer(EntityRendererProvider.Context context) {
        super(context, new SwordManCsdyModel());
    }
    private void renderTrail(SwordManCsdy entityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, float trailA, int packedLightIn) {
        int samples = 0;
        int sampleSize = 16;
        Vec3[] drawFrom = entityIn.getTrailPosition(0, partialTicks);
        VertexConsumer vertexconsumer = bufferIn.getBuffer(FFRenderTypes.getGlowingEffect(TRAIL_TEXTURE));
        renderTrailSegment(entityIn, partialTicks, poseStack, trailA, packedLightIn, samples, sampleSize, drawFrom, vertexconsumer, false);

        int samples2 = 0;
        int sampleSize2 = 16;
        Vec3[] drawFrom2 = entityIn.getTrailPosition2(0, partialTicks);
        VertexConsumer vertexconsumer2 = bufferIn.getBuffer(FFRenderTypes.getGlowingEffect(TRAIL_TEXTURE));
        renderTrailSegment(entityIn, partialTicks, poseStack, trailA, packedLightIn, samples2, sampleSize2, drawFrom2, vertexconsumer2, true);
    }

    private static void renderTrailSegment(SwordManCsdy entityIn, float partialTicks, PoseStack poseStack, float trailA, int packedLightIn, int samples, int sampleSize, Vec3[] drawFrom, VertexConsumer vertexconsumer, boolean isSecondTrail) {
        while (samples < sampleSize) {
            Vec3[] sample = isSecondTrail ? entityIn.getTrailPosition2(samples + 2, partialTicks) : entityIn.getTrailPosition(samples + 2, partialTicks);
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;

            Vec3[] draw1 = drawFrom;
            Vec3[] draw2 = sample;

            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertexconsumer.vertex(matrix4f, (float) draw1[0].x, (float) draw1[0].y, (float) draw1[0].z).color(0.6f, 1f, 1f, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw2[0].x, (float) draw2[0].y, (float) draw2[0].z).color(0.6f, 1f, 1f, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw2[1].x, (float) draw2[1].y, (float) draw2[1].z).color(0.6f, 1f, 1f, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw1[1].x, (float) draw1[1].y, (float) draw1[1].z).color(0.6f, 1f, 1f, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            samples++;
            drawFrom = sample;
        }
    }

    @Override
    public void render(SwordManCsdy entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // 先渲染模型
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        // 然后渲染拖尾
        if (entity.hasTrail()) {
            double x = Mth.lerp(partialTick, entity.xOld, entity.getX());
            double y = Mth.lerp(partialTick, entity.yOld, entity.getY());
            double z = Mth.lerp(partialTick, entity.zOld, entity.getZ());
            poseStack.pushPose();
//            poseStack.scale(32, 32, 32);
            poseStack.translate(-x, -y, -z);
            // 这里调用一个渲染拖尾的方法，这个方法可以写在渲染器中，或者如果模型类中已经有了，可以调用模型类的方法，但需要传递合适的参数
            // 例如：this.model.renderTrail(entity, partialTick, poseStack, bufferSource, 1F, packedLight);
            renderTrail(entity, partialTick, poseStack, bufferSource, 1F, packedLight);
            poseStack.popPose();
        }
    }


}