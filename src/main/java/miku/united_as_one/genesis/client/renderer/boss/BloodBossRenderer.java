package miku.united_as_one.genesis.client.renderer.boss;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.model.boss.BloodBossModel;
import miku.united_as_one.genesis.client.render.FFRenderTypes;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.TrailComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class BloodBossRenderer extends GeoEntityRenderer<BloodBoss> {

    private static final ResourceLocation TRAIL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Genesis.MODID, "textures/misc/blade_trail.png");


    public BloodBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodBossModel());
        this.shadowRadius = 0.65F;
        this.addRenderLayer(new BloodBossGlowLayer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, BloodBoss animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        float scale = 1.75F;
        poseStack.scale(scale, scale, scale);
    }

    private void renderSingleTrail(BloodBoss entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, float alpha, int packedLight) {
        TrailComponent trail = entity.getTrailComponent();
        if (!trail.hasTrail()) {
            return;
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(FFRenderTypes.getGlowingEffect(TRAIL_TEXTURE));
        int sampleCount = 16;

        Vec3[] previousSegment = trail.getTrailPosition(0, partialTicks);
        if (previousSegment == null) return;

        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        for (int i = 0; i < sampleCount; i++) {
            Vec3[] currentSegment = trail.getTrailPosition(i + 1, partialTicks);
            if (currentSegment == null) break;

            float u1 = i / (float) sampleCount;
            float u2 = (i + 1) / (float) sampleCount;

            vertexConsumer.vertex(poseMatrix, (float)previousSegment[0].x, (float)previousSegment[0].y, (float)previousSegment[0].z)
                    .color(0.75F, 0.05F, 0.05F, alpha)
                    .uv(u1, 1.0f)
                    .overlayCoords(NO_OVERLAY)
                    .uv2(packedLight)
                    .normal(normalMatrix, 0.0f, 1.0f, 0.0f)
                    .endVertex();

            vertexConsumer.vertex(poseMatrix, (float)currentSegment[0].x, (float)currentSegment[0].y, (float)currentSegment[0].z)
                    .color(0.75F, 0.05F, 0.05F, alpha)
                    .uv(u2, 1.0f)
                    .overlayCoords(NO_OVERLAY)
                    .uv2(packedLight)
                    .normal(normalMatrix, 0.0f, 1.0f, 0.0f)
                    .endVertex();

            vertexConsumer.vertex(poseMatrix, (float)currentSegment[1].x, (float)currentSegment[1].y, (float)currentSegment[1].z)
                    .color(0.75F, 0.05F, 0.05F, alpha)
                    .uv(u2, 0.0f)
                    .overlayCoords(NO_OVERLAY)
                    .uv2(packedLight)
                    .normal(normalMatrix, 0.0f, 1.0f, 0.0f)
                    .endVertex();

            vertexConsumer.vertex(poseMatrix, (float)previousSegment[1].x, (float)previousSegment[1].y, (float)previousSegment[1].z)
                    .color(0.75F, 0.05F, 0.05F, alpha)
                    .uv(u1, 0.0f)
                    .overlayCoords(NO_OVERLAY)
                    .uv2(packedLight)
                    .normal(normalMatrix, 0.0f, 1.0f, 0.0f)
                    .endVertex();

            previousSegment = currentSegment;
        }
    }

    @Override
    public void render(BloodBoss entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        if (entity.getTrailComponent().hasTrail()) {
            poseStack.pushPose();
            float scale = 1.75F;
            poseStack.scale(scale, scale, scale);
            double x = Mth.lerp(partialTick, entity.xo, entity.getX());
            double y = Mth.lerp(partialTick, entity.yo, entity.getY());
            double z = Mth.lerp(partialTick, entity.zo, entity.getZ());
            poseStack.translate(-x, -y, -z);

            renderSingleTrail(entity, partialTick, poseStack, bufferSource, 1.0F, packedLight);

            poseStack.popPose();
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}