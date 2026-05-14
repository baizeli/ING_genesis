package miku.united_as_one.genesis.client.render.entity.arrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.entity.arrow.StellarArrowEntity;
import miku.united_as_one.genesis.api.render.TrailHelp;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StellarArrowRenderer extends EntityRenderer<StellarArrowEntity> {
    public StellarArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private static final ResourceLocation ARROW_TEXTURE = ResourceLocation.tryBuild(Genesis.MOD_ID, "textures/entity/feg.png");
    private static final ResourceLocation TRAIL_TEX_1 = ResourceLocation.tryBuild(Genesis.MOD_ID, "textures/images/trail_stellar.png");
    private static final ResourceLocation TRAIL_TEX_2 = ResourceLocation.tryBuild(Genesis.MOD_ID, "textures/images/map_3.png");
    private static final ResourceLocation CONE_TEXTURE = ResourceLocation.tryBuild(Genesis.MOD_ID, "textures/images/gr.png");
    
    private static final float trailWidth = 1.0F;
    private static final float coneLength = 1.9f;
    private static final float coneBaseRadius = 0.8f * trailWidth;
    private static final int coneSides = 8;
    private static final float colorFlowSpeed = 0.1f;
    private static final float colorCycleLength = 2.0f;

    @Override
    public void render(StellarArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        renderTrail(entity, partialTicks, poseStack, buffer);
        
        if (entity.isDying()) {
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
            return;
        }

        // 渲染箭矢实体，时刻面向玩家
        poseStack.pushPose();
        Vec3 cameraPos = this.entityRenderDispatcher.camera.getPosition();
        Vec3 arrowPos = entity.position();
        Vec3 direction = cameraPos.subtract(arrowPos).normalize();
        
        double yaw = Math.atan2(direction.x, direction.z);
        double pitch = -Math.asin(direction.y);
        
        poseStack.translate(arrowPos.x, arrowPos.y, arrowPos.z);
        poseStack.mulPose(Axis.YP.rotation((float) yaw));
        poseStack.mulPose(Axis.XP.rotation((float) pitch));
        poseStack.mulPose(Axis.ZP.rotation(Mth.HALF_PI));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        // 渲染箭矢模型
        renderArrowModel(poseStack, consumer, packedLight);
        
        poseStack.popPose();
    }

    private void renderTrail(StellarArrowEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer) {
        List<Vec3> positions = entity.getTrailPositions();
        if (positions.size() < 3) return;

        List<Vec3> workingTrail = new ArrayList<>();
        for (int i = 1; i < positions.size(); i++) workingTrail.add(positions.get(i));

        if (workingTrail.size() < 2) return;

        Vec3 renderOrigin = workingTrail.get(workingTrail.size() - 1);
        List<Vec3> relativePositions = new ArrayList<>();
        for (Vec3 pos : workingTrail) relativePositions.add(pos.subtract(renderOrigin));

        int fullBright = LightTexture.FULL_BRIGHT;

        // 渲染三层拖尾效果
        renderTrailStrip(relativePositions, poseStack, buffer, fullBright, TRAIL_TEX_1, entity.tickCount + partialTicks);
        renderTrailStrip(relativePositions, poseStack, buffer, fullBright, TRAIL_TEX_2, entity.tickCount + partialTicks);
        renderTrailCone(relativePositions, poseStack, buffer, fullBright, entity.tickCount + partialTicks, false);
        renderTrailCone(relativePositions, poseStack, buffer, fullBright, entity.tickCount + partialTicks, true);
    }

    private void renderTrailCone(List<Vec3> positions, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float time, boolean rt) {
        if (positions.size() < 2) return;
        poseStack.pushPose();
        
        // 使用锥形纹理
        RenderType renderType = RenderType.entityTranslucent(CONE_TEXTURE);
        float scale = rt ? coneBaseRadius : coneBaseRadius + 0.5F;
        float length = rt ? coneLength : coneLength + 0.2F;
        int sides = rt ? coneSides : 32;

        VertexConsumer consumer = buffer.getBuffer(renderType);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();

        Vec3 headPos = positions.get(positions.size() - 1);
        Vec3 neckPos = positions.size() > 1 ? positions.get(positions.size() - 2) : headPos;
        Vec3 bodyPos = positions.size() > 2 ? positions.get(positions.size() - 3) : neckPos;

        Vec3 direction = headPos.subtract(neckPos).normalize();
        if (direction.lengthSqr() < 0.001) direction = neckPos.subtract(bodyPos).normalize();
        if (direction.lengthSqr() < 0.001) direction = new Vec3(0, 0, 1);

        Vec3 coneTop = headPos.add(direction.scale(length * 0.3));
        Vec3 coneBottom = headPos.subtract(direction.scale(length * 0.7));

        Vec3 perpendicular1 = TrailHelp.calculatePerpendicular(direction);
        Vec3 perpendicular2 = direction.cross(perpendicular1).normalize();

        renderConeGeometry(consumer, matrix, normal, coneTop, coneBottom, perpendicular1, perpendicular2, packedLight, time, scale, sides);
        poseStack.popPose();
    }

    private void renderConeGeometry(VertexConsumer consumer, Matrix4f matrix, Matrix3f normal, Vec3 top, Vec3 bottom, Vec3 perp1, Vec3 perp2, int packedLight, float time, float scale, int sides) {
        Vec3[] bottomVertices = new Vec3[sides];
        for (int i = 0; i < sides; i++) {
            float angle = (float) (i * 2.0 * Math.PI / sides);
            Vec3 offset = perp1.scale((float) Math.cos(angle) * scale).add(perp2.scale((float) Math.sin(angle) * scale));
            bottomVertices[i] = bottom.add(offset);
        }

        for (int i = 0; i < sides; i++) {
            int nextI = (i + 1) % sides;
            float[] topColor = calculateFlowingColor(1.0f, time, true);
            float[] bottomColor = calculateFlowingColor(0.7f, time, true);
            TrailHelp.addVertexWithColor(consumer, matrix, normal, top, topColor, 1f, 0.5f, 1.0f, packedLight);
            TrailHelp.addVertexWithColor(consumer, matrix, normal, bottomVertices[i], bottomColor, 0f, (float) i / sides, 0.0f, packedLight);
            TrailHelp.addVertexWithColor(consumer, matrix, normal, bottomVertices[nextI], bottomColor, 0f, (float) nextI / sides, 0.0f, packedLight);
        }
    }

    private void renderTrailStrip(List<Vec3> positions, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ResourceLocation tex, float time) {
        if (positions.size() < 2) return;

        poseStack.pushPose();

        // 使用传入的纹理创建渲染类型
        RenderType renderType = RenderType.entityTranslucent(tex);
        VertexConsumer consumer = buffer.getBuffer(renderType);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();

        int planeCount = 2;

        List<Vec3> directions = TrailHelp.calculateSmoothDirections(positions);
        List<Vec3[]> perpendicularSets = TrailHelp.calculatePerpendicularSets(directions, planeCount);

        for (int plane = 0; plane < planeCount; plane++) {
            renderContinuousTrailPlane(positions, perpendicularSets, plane, consumer, matrix, normal, packedLight, time, tex == TRAIL_TEX_2);
        }

        poseStack.popPose();
    }

    private void renderContinuousTrailPlane(List<Vec3> positions, List<Vec3[]> perpendicularSets, int planeIndex, VertexConsumer consumer, Matrix4f matrix, Matrix3f normal, int packedLight, float time, boolean isShadow) {
        final int SUBDIVISIONS = 5;
        for (int i = 0; i < positions.size() - 1; i++) {
            Vec3 pos1 = positions.get(i);
            Vec3 pos2 = positions.get(i + 1);
            Vec3 perpendicular1 = perpendicularSets.get(i)[planeIndex];
            Vec3 perpendicular2 = perpendicularSets.get(i + 1)[planeIndex];

            for (int sub = 0; sub < SUBDIVISIONS; sub++) {
                float t1 = (float) sub / SUBDIVISIONS;
                float t2 = (float) (sub + 1) / SUBDIVISIONS;

                Vec3 subPos1 = TrailHelp.lerpVec3(pos1, pos2, t1);
                Vec3 subPos2 = TrailHelp.lerpVec3(pos1, pos2, t2);
                Vec3 subPerpendicular1 = TrailHelp.lerpVec3(perpendicular1, perpendicular2, t1).normalize();
                Vec3 subPerpendicular2 = TrailHelp.lerpVec3(perpendicular1, perpendicular2, t2).normalize();

                float subProgress1 = Mth.lerp(t1, (float) i / (positions.size() - 1), (float) (i + 1) / (positions.size() - 1));
                float subProgress2 = Mth.lerp(t2, (float) i / (positions.size() - 1), (float) (i + 1) / (positions.size() - 1));
                
                float width1 = TrailHelp.calculateTrailWidth(subProgress1, 0.05F, trailWidth);
                float width2 = TrailHelp.calculateTrailWidth(subProgress2, 0.05F, trailWidth);
                float alpha1 = TrailHelp.calculateAlpha(subProgress1, isShadow) * 0.6f;
                float alpha2 = TrailHelp.calculateAlpha(subProgress2, isShadow) * 0.6f;

                float[] color1 = calculateFlowingColor(subProgress1, time, true);
                float[] color2 = calculateFlowingColor(subProgress2, time, true);

                TrailHelp.addVertexWithColor(consumer, matrix, normal, subPos1.add(subPerpendicular1.scale(width1)), color1, alpha1, subProgress1, 0.0f, packedLight);
                TrailHelp.addVertexWithColor(consumer, matrix, normal, subPos2.add(subPerpendicular2.scale(width2)), color2, alpha2, subProgress2, 0.0f, packedLight);
                TrailHelp.addVertexWithColor(consumer, matrix, normal, subPos2.subtract(subPerpendicular2.scale(width2)), color2, alpha2, subProgress2, 1.0f, packedLight);
                TrailHelp.addVertexWithColor(consumer, matrix, normal, subPos1.subtract(subPerpendicular1.scale(width1)), color1, alpha1, subProgress1, 1.0f, packedLight);
            }
        }
    }

    private float[] calculateFlowingColor(float progress, float time, boolean isStellar) {
        if (isStellar) {
            // 星辰箭矢：全颜色随机
            Random random = new Random((long) (time * 100 + progress * 1000));
            return new float[]{
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat()
            };
        }
        float timeOffset = time * colorFlowSpeed;
        float colorPosition = (progress * colorCycleLength + timeOffset) % 1.0f;
        if (colorPosition < 0) colorPosition += 1.0f;
        return TrailHelp.interpolateGradientColor(colorPosition, java.util.Arrays.asList(0xFFFFFF));
    }

    @Override
    public ResourceLocation getTextureLocation(StellarArrowEntity entity) {
        return ARROW_TEXTURE;
    }

    private void renderArrowModel(PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        
        // 只渲染一个面 - 永远面向玩家的正面
        float length = 1.0f;
        float width = 0.1f;
        
        // 正面（Z 轴负方向，面向玩家）
        TrailHelp.addVertexWithColor(consumer, matrix, normal, new Vec3(-length/2, -width, 0), new float[]{1,1,1}, 1, 0.5f, 1.0f, packedLight);
        TrailHelp.addVertexWithColor(consumer, matrix, normal, new Vec3(length/2, -width, 0), new float[]{1,1,1}, 1, 0.5f, 0.0f, packedLight);
        TrailHelp.addVertexWithColor(consumer, matrix, normal, new Vec3(length/2, width, 0), new float[]{1,1,1}, 1, 0.0f, 0.0f, packedLight);
        TrailHelp.addVertexWithColor(consumer, matrix, normal, new Vec3(-length/2, width, 0), new float[]{1,1,1}, 1, 0.0f, 1.0f, packedLight);
    }
}
