package miku.united_as_one.genesis.client.renderer.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.common.entity.projectile.ThrownIron;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;

public class ThrownIronRenderer extends EntityRenderer<ThrownIron> {
    public ThrownIronRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ThrownIron entity, float yaw, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        List<Vec3> points = entity.trailPositions;

        if (points.size() >= 3) {
            poseStack.pushPose();
            double renderX = Mth.lerp(partialTicks, entity.xOld, entity.getX());
            double renderY = Mth.lerp(partialTicks, entity.yOld, entity.getY());
            double renderZ = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
            poseStack.translate(-renderX, -renderY, -renderZ);

            VertexConsumer vc = bufferSource.getBuffer(RenderType.lightning());
            Matrix4f matrix = poseStack.last().pose();
            Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

            for (int i = 0; i < points.size() - 2; i++) {
                // 增加细分到 6，让拐弯更丝滑
                int subdivisions = 6;
                for (int j = 0; j < subdivisions; j++) {
                    float t1 = (float) j / subdivisions;
                    float t2 = (float) (j + 1) / subdivisions;

                    Vec3 v1 = getBezier(points.get(i), points.get(i + 1), points.get(i + 2), t1);
                    Vec3 v2 = getBezier(points.get(i), points.get(i + 1), points.get(i + 2), t2);

                    // 整体进度系数 (0 = 末尾, 1 = 头部)
                    float progress = (float) (i * subdivisions + j) / ((points.size() - 1) * subdivisions);

                    // --- 动态宽度计算：头部 0.12f -> 末尾 0.0f ---
                    float coreWidth = 0.05f * progress;
                    float glowWidth = 0.18f * progress;

                    // --- 向量计算 ---
                    Vec3 segmentDir = v2.subtract(v1).normalize();
                    Vec3 lookDir = cameraPos.subtract(v1).normalize();
                    Vec3 sideVec = segmentDir.cross(lookDir);

                    if (sideVec.lengthSqr() < 1.0E-4D) {
                        sideVec = new Vec3(0, 1, 0);
                    } else {
                        sideVec = sideVec.normalize();
                    }

                    // --- 渲染逻辑 ---
                    // 1. 外层：几乎透明且极淡
                    int glowAlpha = (int) (progress * 60); // 调低透明度上限
                    drawBillboardSegment(matrix, vc, v1, v2, sideVec, glowWidth, 255, 255, 255, glowAlpha);

                    // 2. 内层：正常亮白色，但也随长度略微变淡
                    int coreAlpha = (int) (progress * 255);
                    drawBillboardSegment(matrix, vc, v1, v2, sideVec, coreWidth, 255, 255, 255, coreAlpha);
                }
            }
            poseStack.popPose();
        }

        // 渲染铁锭图标
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.scale(1.1F, 1.1F, 1.1F);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                new ItemStack(Items.IRON_INGOT), ItemDisplayContext.GROUND,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), 0);
        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private Vec3 getBezier(Vec3 p0, Vec3 p1, Vec3 p2, float t) {
        double x = Math.pow(1 - t, 2) * p0.x + 2 * (1 - t) * t * p1.x + Math.pow(t, 2) * p2.x;
        double y = Math.pow(1 - t, 2) * p0.y + 2 * (1 - t) * t * p1.y + Math.pow(t, 2) * p2.y;
        double z = Math.pow(1 - t, 2) * p0.z + 2 * (1 - t) * t * p1.z + Math.pow(t, 2) * p2.z;
        return new Vec3(x, y, z);
    }

    private void drawBillboardSegment(Matrix4f matrix, VertexConsumer vc, Vec3 v1, Vec3 v2, Vec3 side, float w, int r, int g, int b, int a) {
        float sx = (float) (side.x * w);
        float sy = (float) (side.y * w);
        float sz = (float) (side.z * w);

        vc.vertex(matrix, (float)v1.x + sx, (float)v1.y + sy, (float)v1.z + sz).color(r, g, b, a).endVertex();
        vc.vertex(matrix, (float)v2.x + sx, (float)v2.y + sy, (float)v2.z + sz).color(r, g, b, a).endVertex();
        vc.vertex(matrix, (float)v2.x - sx, (float)v2.y - sy, (float)v2.z - sz).color(r, g, b, a).endVertex();
        vc.vertex(matrix, (float)v1.x - sx, (float)v1.y - sy, (float)v1.z - sz).color(r, g, b, a).endVertex();
    }

    @Override public @NotNull ResourceLocation getTextureLocation(@NotNull ThrownIron entity) {
        return new ResourceLocation("minecraft", "textures/item/iron_ingot.png");
    }
}